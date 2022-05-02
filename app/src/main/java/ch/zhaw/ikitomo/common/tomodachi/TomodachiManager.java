package ch.zhaw.ikitomo.common.tomodachi;

import java.io.IOException;
import java.nio.file.Path;

/**
 * A helper class to load {@link TomodachiDefinition} objects
 */
public class TomodachiManager {
    /**
     * Private constructor
     */
    private TomodachiManager() {
    }

    /**
     * Loads a {@link TomodachiDefinition} from the given root folder
     * 
     * @param path The path to the root folder of the tomodachi
     * @return The loaded Tomodachi File
     * @throws IOException If the file could not be loaded
     */
    public static TomodachiDefinition load(String rootFolder) throws IOException {
        throw new UnsupportedOperationException("not implemented yet");
    }

    /**
     * Saves the given {@link TomodachiDefinition} to the root folder set in the
     * {@link TomodachiDefinition#getRootFolder()}
     * 
     * @param file the tomodachi file to save
     */
    public static void save(TomodachiDefinition file) {
        Path tomodachiFile = file.getRootFolder().resolve("tomodachi.json");
        throw new UnsupportedOperationException("not implemented yet");
    }
}
