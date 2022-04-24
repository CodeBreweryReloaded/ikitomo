package ch.zhaw.ikitomo.common.tomodachi;

import java.util.Objects;

/**
 * Represents the config of a tomodachi file
 */
public class TomodachiConfigurationFile {
    /**
     * the id
     */
    private String id;
    /**
     * the name
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
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */

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
