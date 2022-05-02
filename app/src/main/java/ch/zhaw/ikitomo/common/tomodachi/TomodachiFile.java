package ch.zhaw.ikitomo.common.tomodachi;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import ch.zhaw.ikitomo.common.StateType;

/**
 * Represents a tomodachi file
 */
public class TomodachiFile {

    /**
     * The root folder of the tomodachi file
     * //TODO this field has to be transient
     */
    private Path rootFolder;
    /**
     * The settings of the tomodachi
     */
    private TomodachiSettings settings;
    /**
     * The config of the tomodachi
     */
    private TomodachiConfigurationFile config;
    /**
     * The available states of the tomodachi
     */
    private List<TomodachiStateFile> states = new ArrayList<>();

    /**
     * Constructor
     * 
     * @param settings The settings
     * @param config   The config
     * @param states   The states
     */
    public TomodachiFile(Path rootFolder, TomodachiSettings settings, TomodachiConfigurationFile config,
            List<TomodachiStateFile> states) {
        this.rootFolder = rootFolder;
        this.settings = settings;
        this.config = config;
        this.states.addAll(states);
    }

    /**
     * Returns the root folder. All files must be relative to this folder
     * 
     * @return The rootFolder
     */
    public Path getRootFolder() {
        return rootFolder;
    }

    /**
     * Gets the settings
     *
     * @return The settings
     */
    public TomodachiSettings getSettings() {
        return settings;
    }

    /**
     * Gets the configuration
     *
     * @return The configuration
     */
    public TomodachiConfigurationFile getConfig() {
        return config;
    }

    /**
     * Gets a copy of the list of available states
     *
     * @return A copy of the list of available states
     */
    public List<TomodachiStateFile> getStates() {
        return new ArrayList<>(states);
    }

    @Override
    public int hashCode() {
        return Objects.hash(config, settings, states);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof TomodachiFile)) {
            return false;
        }
        TomodachiFile other = (TomodachiFile) obj;
        return Objects.equals(config, other.config) && Objects.equals(settings, other.settings)
                && Objects.equals(states, other.states);
    }

    // TODO: Remove when reading JSON files becomes possible
    private static int nextID;
    
    public static TomodachiFile createMockObject() {
        var tomodachiSettings = new TomodachiSettings(0.01f, 0.01f);
        int id = nextID++;
        var tomodachiConfig = new TomodachiConfigurationFile("ch.zhaw.mock" + id, "Mock " + id);
        var states = Arrays.asList(new TomodachiStateFile(StateType.IDLE, null));
        return new TomodachiFile(Paths.get("./"), tomodachiSettings, tomodachiConfig, states);
    }

}
