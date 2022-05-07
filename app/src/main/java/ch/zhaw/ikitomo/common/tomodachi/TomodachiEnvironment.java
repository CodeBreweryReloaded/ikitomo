package ch.zhaw.ikitomo.common.tomodachi;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import ch.zhaw.ikitomo.common.JavaFXUtils;
import ch.zhaw.ikitomo.common.settings.Settings;
import ch.zhaw.ikitomo.common.settings.SettingsManager;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

/**
 * Represents the environment of the tomodachi application
 */
public class TomodachiEnvironment {
    /**
     * The path to the folder where by default all tomodachi folders are stored
     */
    private static final String DEFAULT_TOMODACHI_FOLDER_PATH = "./tomodachi";

    /**
     * The location of the default Tomodachi definition file
     */
    private static final URL DEFAULT_TOMODACHI_LOCATION = TomodachiEnvironment.class
            .getResource("/defaultTomodachi/tomodachi.json");
            
    /**
     * The directory of the default Tomodachi
     */
    private static final URL DEFAULT_TOMODACHI_DIRECTORY = TomodachiEnvironment.class
            .getResource("/defaultTomodachi");

    /**
     * A component for logging messages.
     */
    private static final Logger LOGGER = Logger.getLogger(TomodachiEnvironment.class.getName());

    /**
     * A component for loading and saving settings
     */
    private SettingsManager settingsManager;

    /**
     * A component for loading and saving tomodachi definitions
     */
    private TomodachiManager tomodachiManager;

    /**
     * The settings of the application
     */
    private Settings settings = null;

    /**
     * All available tomodachis
     */
    private ObservableMap<String, TomodachiDefinition> tomodachiDefinitionMap = FXCollections.observableHashMap();

    /**
     * An observable list of all tomodachi definitions. It is dependent on
     * {@link #tomodachiDefinitionMap}
     */
    private ObservableList<TomodachiDefinition> tomodachiDefinitionList = JavaFXUtils
            .observableValuesFromMap(tomodachiDefinitionMap);

    /**
     * A binding of the currently selected tomodachi definition in the
     * {@link #settings} object
     */
    private ObjectBinding<TomodachiDefinition> currentTomodachiDefinition;

    /**
     * The default tomodachi definition. This is loaded in
     * {@link #reloadTomodachis()} and should always be a non-null value
     */
    private TomodachiDefinition defaultTomodachiDefinition = null;

    /**
     * Initializes a new instance of the {@link TomodachiEnvironment} class
     *
     * @param settingsManager the settings manager
     */
    public TomodachiEnvironment(SettingsManager settingsManager, TomodachiManager tomodachiManager) {
        this.settingsManager = settingsManager;
        this.tomodachiManager = tomodachiManager;
        loadSettings();
        loadDefaultTomodachiDefinition();
        updateTomodachiDefinitions(readAvailableTomodachis());
        // settings is set by load()
        this.currentTomodachiDefinition = Bindings.createObjectBinding(
                this::getCurrentTomodachiDefinitionFromTomodachiDefinitionMap, settings.tomodachiIDProperty());
    }

    /**
     * Gets a component for loading and saving settings
     *
     * @return A component for loading and saving settings
     */
    public SettingsManager getSettingsManager() {
        return settingsManager;
    }

    /**
     * Gets a component for loading and saving tomodachi definitions
     *
     * @return A component for loading and saving tomodachi definitions
     */
    public TomodachiManager getTomodachiManager() {
        return tomodachiManager;
    }

    /**
     * Gets the settings of the application
     *
     * @return The settings of the application
     */
    public Settings getSettings() {
        return settings;
    }

    /**
     * Loads the settings from the default location and loads all available
     * tomodachis
     */
    private void loadSettings() {
        try {
            settings = settingsManager.load(SettingsManager.DEFAULT_SETTINGS_PATH);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "The settings could not be loaded.", e);
            settings = Settings.createDefaultSettings();
        }
    }

    /**
     * Loads the default tomodachi from the classpath
     */
    private void loadDefaultTomodachiDefinition() {
        Objects.requireNonNull(DEFAULT_TOMODACHI_LOCATION,
                "The default tomodachi definition could not be loaded because the location is null");
        try {
            var reader = new InputStreamReader(DEFAULT_TOMODACHI_LOCATION.openStream(), StandardCharsets.UTF_8);
            defaultTomodachiDefinition = tomodachiManager.load(reader);
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Could not load default tomodachi from \"" + DEFAULT_TOMODACHI_LOCATION + "\"", e);
        }
    }

    /**
     * Reloads all available tomodachis in a separate thread. The method returns a
     * {@link CompletableFuture} which completes when the reloading is finished. The
     * {@link #tomodachiDefinitionMap} list is updated
     * 
     * @return A future which completes when the operation is finished
     */
    public CompletableFuture<Void> reloadTomodachis() {
        return CompletableFuture.supplyAsync(this::readAvailableTomodachis)
                .thenCompose(list -> JavaFXUtils.runLater(() -> updateTomodachiDefinitions(list)));
    }

    /**
     * Reloads all available tomodachis
     */
    private List<TomodachiDefinition> readAvailableTomodachis() {
        List<TomodachiDefinition> localTomodachiDefinitions = new ArrayList<>();
        File tomodachiRootFolder = new File(DEFAULT_TOMODACHI_FOLDER_PATH);
        File[] tomodachiFolders = tomodachiRootFolder.listFiles();
        if (tomodachiFolders == null) {
            LOGGER.warning("The tomodachi folder \"" + tomodachiRootFolder.getAbsolutePath() + "\"could not be found.");
            return Collections.emptyList();
        }
        for (File tomodachiFolder : tomodachiFolders) {
            try {
                localTomodachiDefinitions.add(tomodachiManager.load(tomodachiFolder.getAbsolutePath()));
            } catch (IOException e) {
                LOGGER.log(Level.WARNING,
                        "The tomodachi definition \"" + tomodachiFolder.getAbsolutePath() + "\" could not be loaded.",
                        e);
            }
        }

        try {
            localTomodachiDefinitions.add(tomodachiManager.load(new File(DEFAULT_TOMODACHI_DIRECTORY.toURI()).getAbsolutePath()));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Default Tomodachi directory could not be opened.", e);
        }
        return localTomodachiDefinitions;
    }

    /**
     * Updates the {@link #tomodachiDefinitionMap} list in the JavaFX thread. Coll
     * 
     * @param list The new list
     */
    private void updateTomodachiDefinitions(List<TomodachiDefinition> list) {
        Map<String, TomodachiDefinition> map = list.stream()
                .collect(Collectors.toMap(TomodachiDefinition::getID, Function.identity()));
        map.putIfAbsent(defaultTomodachiDefinition.getID(), defaultTomodachiDefinition);
        tomodachiDefinitionMap.clear();
        tomodachiDefinitionMap.putAll(map);
    }

    /**
     * Gets all available tomodachi definitions
     * 
     * @return The list of all available tomodachi definitions
     */
    public ObservableList<TomodachiDefinition> getTomodachiDefinitions() {
        return tomodachiDefinitionList;
    }

    /**
     * Gets the tomodachi definition with the given id or null if the id doesn't
     * exist
     * 
     * @param id The id to look up
     * @return The tomodachi definition with the given id or null if the id doesn't
     *         exist
     */
    public TomodachiDefinition getTomodachiDefinition(String id) {
        return tomodachiDefinitionMap.get(id);
    }

    /**
     * Gets the tomodachi definition currently selected in
     * {@link #settings#getCurrentTomodachiDefinition()}.
     * If the currently selected tomodachi id doesn't exist then the
     * {@link #defaultTomodachiDefinition} is returned
     * 
     * @return The tomodachi definition
     */
    public TomodachiDefinition getCurrentTomodachiDefinition() {
        return currentTomodachiDefinition.get();
    }

    /**
     * An internal helper method that gets the tomodachi definition currently
     * selected in the {@link #settings} object.
     * If the currently selected tomodachi id doesn't exist then the
     * {@link #defaultTomodachiDefinition} is returned
     * 
     * @return The tomodachi definition or {@link #defaultTomodachiDefinition} if
     *         the currently selected tomodachi id doesn't exist
     */
    private TomodachiDefinition getCurrentTomodachiDefinitionFromTomodachiDefinitionMap() {
        return tomodachiDefinitionMap.getOrDefault(settings.getTomodachiID(), defaultTomodachiDefinition);
    }

    /**
     * Gets a {@link ObjectBinding} to the currently selected tomodachi definition
     * 
     * @return The binding
     * @see #getCurrentTomodachiDefinition()
     */
    public ObjectBinding<TomodachiDefinition> currentTomodachiDefinitionBinding() {
        return currentTomodachiDefinition;
    }

    /**
     * Saves the settings to the default location
     *
     * @throws IOException Occurs when the settings could not be saved
     */
    public void save() throws IOException {
        settingsManager.save(SettingsManager.DEFAULT_SETTINGS_PATH, settings);
    }

}
