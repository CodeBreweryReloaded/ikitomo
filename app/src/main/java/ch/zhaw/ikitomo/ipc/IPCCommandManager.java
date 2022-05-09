package ch.zhaw.ikitomo.ipc;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Objects;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import ch.zhaw.ikitomo.common.JSONManager;

/**
 * A class which loads and saves {@link IPCCommand}s to and from a JSON string.
 */
public class IPCCommandManager extends JSONManager<IPCCommand> {

    /**
     * Constructor
     */
    public IPCCommandManager() {
        super(IPCCommand.class);
    }

    /**
     * Tries to parse the given JSON string into a {@link IPCCommand}.
     * 
     * @param input The string input
     * @return The parsed command
     * @throws IOException If an exception while parsing happend. Jackson throws
     *                     {@link JsonParseException}, {@link JsonMappingException}
     *                     when something goes wrong while parsing. Both Exception
     *                     extend from {@link IOException}, which gets also thrown
     */
    public IPCCommand loadFromString(String input) throws IOException {
        Objects.requireNonNull(input, "input == null");
        return getMapper().readValue(input, IPCCommand.class);
    }

    /**
     * Saves the given {@link IPCCommand} to a JSON string
     * 
     * @param command The command to save
     * @return The converted string
     * @throws IOException If something goes wrong while converting. See
     *                     {@link #loadFromString(String)} for an explanation for
     *                     why an {@link IOException} is thrown.
     */
    public String saveToString(IPCCommand command) throws IOException {
        Objects.requireNonNull(command, "command == null");
        var writer = new StringWriter();
        save(writer, command);
        return writer.toString();
    }

}
