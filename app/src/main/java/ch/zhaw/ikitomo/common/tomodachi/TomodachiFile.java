package ch.zhaw.ikitomo.common.tomodachi;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
     * The states of the tomodachi
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
     * @return The settings
     */
    public TomodachiSettings getSettings() {
        return settings;
    }

    /**
     * @return The config
     */
    public TomodachiConfigurationFile getConfig() {
        return config;
    }

    /**
     * @return A copy of the states list
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

}
