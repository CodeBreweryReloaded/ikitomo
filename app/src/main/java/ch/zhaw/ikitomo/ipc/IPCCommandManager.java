package ch.zhaw.ikitomo.ipc;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Objects;

import com.fasterxml.jackson.databind.ObjectMapper;

import ch.zhaw.ikitomo.common.JSONManager;

public class IPCCommandManager extends JSONManager<IPCCommand> {

    protected IPCCommandManager() {
        super(IPCCommand.class);
    }

    public IPCCommand loadFromString(String input) throws IOException {
        Objects.requireNonNull(input, "input == null");
        return getMapper().readValue(input, IPCCommand.class);
    }

    public String saveToString(IPCCommand command) throws IOException {
        Objects.requireNonNull(command, "command == null");
        var writer = new StringWriter();
        save(writer, command);
        return writer.toString();
    }

    @Override
    protected ObjectMapper initializeMapper() {
        ObjectMapper mapper = super.initializeMapper();
        return mapper;
    }

}
