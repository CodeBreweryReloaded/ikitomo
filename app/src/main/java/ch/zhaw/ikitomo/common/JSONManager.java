package ch.zhaw.ikitomo.common;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Provides the functionality to manage settings stored in JSON code
 */
public abstract class JSONManager<T> {
    /**
     * The type of the data represented by the json code
     */
    private Class<T> dataType;

    /**
     * A component for deserializing json code
     */
    private ObjectMapper mapper;

    /**
     * Initializes a new instance of the {@link JSONManager} class
     */
    public JSONManager(Class<T> dataType) {
        this.dataType = dataType;
    }

    /**
     * Gets the type of the data represented by the json code
     *
     * @return The type of the data represented by the json code
     */
    protected Class<T> getDataType() {
        return dataType;
    }

    /**
     * Gets a component for deserializing json files
     *
     * @return A component for deserializing json files
     */
    protected ObjectMapper getMapper() {
        if (mapper == null) {
            mapper = initializeMapper();
        }

        return mapper;
    }

    /**
     * Writes the specified {@code data} to a file located at the specified {@code path}
     *
     * @param path The path to save the data to
     * @param data The data to save
     * @throws IOException Occurs when the file could not be written
     */
    public void save(String path, T data) throws IOException {
        getMapper().writeValue(new File(path), data);
    }

    /**
     * Reads the data from the specified {@code path}
     *
     * @param path The path to read the data from
     * @return The data read from the file
     * @throws IOException Occurs when the file could not be read
     */
    public T load(String path) throws IOException {
        return getMapper().readValue(new File(path), getDataType());
    }

    /**
     * Initializes a component for deserializing json files.
     *
     * @return A component for deserializing json files.
     */
    protected ObjectMapper initializeMapper() {
        ObjectMapper parser = new ObjectMapper();
        parser.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        return parser;
    }
}
