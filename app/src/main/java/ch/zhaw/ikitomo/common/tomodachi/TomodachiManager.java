package ch.zhaw.ikitomo.common.tomodachi;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import ch.zhaw.ikitomo.common.JSONManager;

/**
 * A helper class to load {@link TomodachiDefinition} objects
 */
public class TomodachiManager extends JSONManager<TomodachiDefinition> {
    /**
     * Private constructor
     */
    public TomodachiManager() {
        super(TomodachiDefinition.class);
    }

    /**
     * Gets the base name of the tomodachi definition files
     *
     * @return The base name of the tomodachi definition files
     */
    protected String getConfigBaseName() {
        return "tomodachi.json";
    }

    /**
     * Loads a {@link TomodachiDefinition} from the given root folder
     * 
     * @param path The path to the root folder of the tomodachi
     * @return The loaded Tomodachi File
     * @throws IOException If the file could not be loaded
     */
    @Override
    public TomodachiDefinition load(String rootFolder) throws IOException {
        Path rootFolderPath = Paths.get(rootFolder, getConfigBaseName());
        var result = super.load(rootFolderPath.toString());
        result.setRootPath(rootFolderPath);
        return result;
    }

    /**
     * Saves the given {@link TomodachiDefinition} to the root folder set in the
     * {@link TomodachiDefinition#getRootFolder()}
     * 
     * @param definition The tomodachi definition to save
     * @throws IOException Occurs when the file could not be saved
     */
    public void save(TomodachiDefinition definition) throws IOException {
        super.save(definition.getRootFolder().resolve(getConfigBaseName()).toString(), definition);
    }
}
