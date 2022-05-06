package ch.zhaw.ikitomo.common.logging;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Handler;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;

/**
 * A "Handler" that only creates a folder and does not output anything. It can
 * be used log with a {@link java.util.logging.FileHandler} to a folder and
 * automatically creating the folder when it does not exist yet.
 * If the folder already exists, it is not created again and all files are
 * retained.
 */
public class FolderLogCreator extends Handler {

    /**
     * Constructor<br>
     * 
     * There is only one constructor because this class is only useful in the
     * log.properties file
     * 
     * @throws IOException if the folder couldn't be created
     */
    public FolderLogCreator() throws IOException {
        String cname = getClass().getName();

        String folder = LogManager.getLogManager().getProperty(cname + ".folder");
        Path folderPath = Paths.get(folder);

        Files.createDirectories(folderPath);
    }

    @Override
    public void publish(LogRecord logRecord) {
        // the only job of this class is to create a folder
    }

    @Override
    public void flush() {
        // the only job of this class is to create a folder
    }

    @Override
    public void close() throws SecurityException {
        // the only job of this class is to create a folder
    }

}
