package ch.zhaw.ikitomo.common.tomodachi;

import java.util.Objects;

/**
 * Represents the config of a tomodachi file
 */
public class TomodachiConfigurationFile {
    /**
     * The id
     */
    private String id;
    /**
     * The name
     */
    private String name;

    /**
     * Constructor
     */
    public TomodachiConfigurationFile(String id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * @return The ID
     */
    public String getId() {
        return id;
    }

    /**
     * @return The name
     */
    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof TomodachiConfigurationFile)) {
            return false;
        }
        TomodachiConfigurationFile other = (TomodachiConfigurationFile) obj;
        return Objects.equals(id, other.id) && Objects.equals(name, other.name);
    }

}
