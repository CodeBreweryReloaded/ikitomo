package ch.zhaw.ikitomo.common.tomodachi;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a the definition of a tomodachi
 */
public class TomodachiDefinition {
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
    public TomodachiDefinition(Path rootFolder, TomodachiSettings settings, TomodachiConfigurationFile config,
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
        if (!(obj instanceof TomodachiDefinition)) {
            return false;
        }
        TomodachiDefinition other = (TomodachiDefinition) obj;
        return Objects.equals(config, other.config) && Objects.equals(settings, other.settings)
                && Objects.equals(states, other.states);
    }

}
