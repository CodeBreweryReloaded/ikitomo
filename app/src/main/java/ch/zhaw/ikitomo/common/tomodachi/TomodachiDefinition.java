package ch.zhaw.ikitomo.common.tomodachi;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Represents a tomodachi file
 * import com.fasterxml.jackson.annotation.JsonIgnore;
 * 
 * /**
 * Represents a the definition of a tomodachi
 */
public class TomodachiDefinition {
    /**
     * The root folder of the tomodachi file
     */
    @JsonIgnore
    private Path rootFolder;

    /**
     * The id of the tomodachi
     */
    private String id;

    /**
     * The name of the tomodachi
     */
    private String name;

    /**
     * The settings of the tomodachi
     */
    private TomodachiSettings settings;

    /**
     * The path to the icon
     */
    private String icon;

    /**
     * The available states of the tomodachi
     */
    private List<TomodachiStateDefinition> states = new ArrayList<>();

    /**
     * Constructor for jackson
     */
    private TomodachiDefinition() {
    }

    /**
     * Constructor
     * 
     * @param settings The settings
     * @param config   The config
     * @param states   The states
     */
    public TomodachiDefinition(Path rootFolder, String id, String name, TomodachiSettings settings, String icon,
            List<TomodachiStateDefinition> states) {
        this.rootFolder = rootFolder;
        this.id = id;
        this.name = name;
        this.settings = settings;
        this.icon = icon;
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
     * Sets the root folder. This method is package-private that only
     * {@link TomodachiManager} can set it (and other package classes)
     * 
     * @param rootFolder the root folder
     */
    void setRootPath(Path rootFolder) {
        this.rootFolder = rootFolder;
    }

    /**
     * Gets the id of the tomodachi
     * 
     * @return The id
     */
    public String getID() {
        return id;
    }

    /**
     * Gets the name of the tomodachi
     * 
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the path to the icon. If {@link #isResource()} returns true the icon has
     * to be loaded from the classpath
     * 
     * @return The path to the icon
     */
    public String getIcon() {
        return icon;
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
     * Gets a copy of the list of available states
     *
     * @return A copy of the list of available states
     */
    public List<TomodachiStateDefinition> getStates() {
        return new ArrayList<>(states);
    }

    /**
     * Checks if this Definition is a resource (part of the program) or an external
     * file
     * 
     * @return True if it is resource, false if it is a file
     */
    public boolean isResource() {
        return getRootFolder() == null;
    }

    @Override
    public int hashCode() {
        return Objects.hash(icon, id, name, rootFolder, settings, states);
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
        return Objects.equals(icon, other.icon) && Objects.equals(id, other.id) && Objects.equals(name, other.name)
                && Objects.equals(rootFolder, other.rootFolder) && Objects.equals(settings, other.settings)
                && Objects.equals(states, other.states);
    }

}
