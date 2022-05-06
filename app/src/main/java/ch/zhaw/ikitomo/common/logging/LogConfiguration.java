package ch.zhaw.ikitomo.common.logging;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class LogConfiguration {
    private static final Logger LOGGER = Logger.getLogger(LogConfiguration.class.getName());
    private static final String DEFAULT_CLASSPATH_LOG_CONFIG_PATH = "/log.properties";

    public static void init() {
        Locale.setDefault(Locale.ROOT);
        String logConfigPathStr = System.getProperty("java.util.logging.config.file", "log.properties");
        Path logConfigPath = Path.of(logConfigPathStr);
        try {
            readLogConfiguration(logConfigPath);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Could not read log configuration file", e);
        }
        LOGGER.finest("test");
        LOGGER.finer("test");
        LOGGER.fine("test");
        LOGGER.info("test");
        LOGGER.warning("test");
        LOGGER.severe("test");
    }

    private static void readLogConfiguration(Path path) throws IOException {
        if (Files.isReadable(path)) {
            try (InputStream in = Files.newInputStream(path)) {
                readLogConfig(in, path.toString());
            }
        } else {
            try (InputStream in = LogConfiguration.class.getResourceAsStream(DEFAULT_CLASSPATH_LOG_CONFIG_PATH)) {
                if (in == null) {
                    throw new IOException(
                            "Could not open default log configuration file from \"" + DEFAULT_CLASSPATH_LOG_CONFIG_PATH
                                    + "\"");
                }
                readLogConfig(in, "resource://" + DEFAULT_CLASSPATH_LOG_CONFIG_PATH);
            }
        }
    }

    private static void readLogConfig(InputStream inputStream, String path) throws SecurityException, IOException {
        LogManager.getLogManager().readConfiguration(inputStream);
        LOGGER.log(Level.INFO, "Read log configuration from \"{0}\"", path);
    }
}
