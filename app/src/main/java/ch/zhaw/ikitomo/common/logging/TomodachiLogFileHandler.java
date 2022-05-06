package ch.zhaw.ikitomo.common.logging;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.ErrorManager;
import java.util.logging.FileHandler;
import java.util.logging.Filter;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;

public class TomodachiLogFileHandler extends Handler {
    private FileHandler fileHandler;
    private String folder;

    public TomodachiLogFileHandler() throws IOException, SecurityException {
        super();
        LogManager manager = LogManager.getLogManager();
        String cname = getClass().getName();
        folder = manager.getProperty(cname + ".folder");
        if (folder == null) {
            folder = "";
        }
    }

    /**
     * @param encoding
     * @throws SecurityException
     * @throws UnsupportedEncodingException
     * @see java.util.logging.StreamHandler#setEncoding(java.lang.String)
     */

    public void setEncoding(String encoding) throws SecurityException, UnsupportedEncodingException {
        fileHandler.setEncoding(encoding);
    }

    /**
     * @param newFormatter
     * @throws SecurityException
     * @see java.util.logging.Handler#setFormatter(java.util.logging.Formatter)
     */

    public void setFormatter(Formatter newFormatter) throws SecurityException {
        fileHandler.setFormatter(newFormatter);
    }

    /**
     * @return
     * @see java.util.logging.Handler#getFormatter()
     */

    public Formatter getFormatter() {
        return fileHandler.getFormatter();
    }

    /**
     * @param record
     * @return
     * @see java.util.logging.StreamHandler#isLoggable(java.util.logging.LogRecord)
     */

    public boolean isLoggable(LogRecord record) {
        return fileHandler.isLoggable(record);
    }

    /**
     * @return
     * @see java.util.logging.Handler#getEncoding()
     */

    public String getEncoding() {
        return fileHandler.getEncoding();
    }

    /**
     * @param newFilter
     * @throws SecurityException
     * @see java.util.logging.Handler#setFilter(java.util.logging.Filter)
     */

    public void setFilter(Filter newFilter) throws SecurityException {
        fileHandler.setFilter(newFilter);
    }

    /**
     * 
     * @see java.util.logging.StreamHandler#flush()
     */

    public void flush() {
        fileHandler.flush();
    }

    /**
     * @return
     * @see java.util.logging.Handler#getFilter()
     */

    public Filter getFilter() {
        return fileHandler.getFilter();
    }

    /**
     * @param em
     * @see java.util.logging.Handler#setErrorManager(java.util.logging.ErrorManager)
     */

    public void setErrorManager(ErrorManager em) {
        fileHandler.setErrorManager(em);
    }

    /**
     * @return
     * @see java.util.logging.Handler#getErrorManager()
     */

    public ErrorManager getErrorManager() {
        return fileHandler.getErrorManager();
    }

    /**
     * @return
     * @see java.lang.Object#toString()
     */

    public String toString() {
        return fileHandler.toString();
    }

    /**
     * @param newLevel
     * @throws SecurityException
     * @see java.util.logging.Handler#setLevel(java.util.logging.Level)
     */

    public void setLevel(Level newLevel) throws SecurityException {
        fileHandler.setLevel(newLevel);
    }

    /**
     * @return
     * @see java.util.logging.Handler#getLevel()
     */

    public Level getLevel() {
        return fileHandler.getLevel();
    }

    /**
     * @param record
     * @see java.util.logging.FileHandler#publish(java.util.logging.LogRecord)
     */

    public void publish(LogRecord record) {
        fileHandler.publish(record);
    }

    /**
     * @throws SecurityException
     * @see java.util.logging.FileHandler#close()
     */

    public void close() throws SecurityException {
        fileHandler.close();
    }

}
