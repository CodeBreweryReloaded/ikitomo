package ch.zhaw.ikitomo.common.logging;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * A class which initializes the logging system. It has to be called with
 * {@link LoggingConfiguration#init()} to start initializing
 */
public class LogConfiguration {
    /**
     * The logger
     */
    private static final Logger LOGGER = Logger.getLogger(LogConfiguration.class.getName());

    /**
     * The log config which resides in the classpath
     */
    private static final String DEFAULT_CLASSPATH_LOG_CONFIG_PATH = "/log.properties";

    /**
     * A private constructor to prevent instantiation
     */
    private LogConfiguration() {
    }

    /**
     * Initializes the logging system. The path to the configuration file is loaded
     * from the "java.util.logging.config.file" system property. If the property is
     * not set, the default path "log.properties" is used
     */
    public static void init() {
        Locale.setDefault(Locale.ROOT);
        String logConfigPathStr = System.getProperty("java.util.logging.config.file", "log.properties");
        Path logConfigPath = Path.of(logConfigPathStr);
        try {
            readLogConfiguration(logConfigPath);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Could not read log configuration file", e);
        }
    }

    /**
     * Tries to read the log configuration from the given path. If the path doesn't
     * exist it is read instead from {@link #DEFAULT_CLASSPATH_LOG_CONFIG_PATH}. If
     * this fails as well then an {@link IOException} is thrown
     * 
     * @param path The path to the log configuration file
     * @throws IOException if the log configuration file couldn't be read
     */
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

    /**
     * Reads the log configuration from the given input stream
     * 
     * @param inputStream The input stream to read from
     * @param path        The path where the log configuration is read from. This is
     *                    only used for logging
     * @throws SecurityException If a security manager exists and if the caller does
     *                           not have LoggingPermission("control").
     * @throws IOException       If the input stream cannot be read
     */
    private static void readLogConfig(InputStream inputStream, String path) throws SecurityException, IOException {
        LogManager logManager = LogManager.getLogManager();
        logManager.readConfiguration(inputStream);
        LOGGER.log(Level.INFO, "Read log configuration from \"{0}\"", path);
    }

}
