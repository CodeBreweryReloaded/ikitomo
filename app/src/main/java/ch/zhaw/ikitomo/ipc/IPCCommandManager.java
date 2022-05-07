package ch.zhaw.ikitomo.ipc;

import java.io.IOException;
import java.io.StringReader;
import java.util.Objects;

import ch.zhaw.ikitomo.common.JSONManager;

public class IPCCommandManager extends JSONManager<IPCCommand> {

    protected IPCCommandManager() {
        super(IPCCommand.class);
    }

    public IPCCommand loadFromString(String input) {
        Objects.requireNonNull(input, "input == null");
        try {
            return load(new StringReader(input));
        } catch (IOException e) {
            throw new IllegalStateException("Couldn't read from string reader", e);
        }
    }

}
