package ch.zhaw.ikitomo.ipc;

import java.util.ArrayList;
import java.util.List;

public record IPCCommand(String command, List<String> args) {

    public IPCCommand(String command, List<String> args) {
        this.command = command;
        this.args = new ArrayList<>(args);
    }

    public List<String> args() {
        return new ArrayList<>(args);
    }

}
