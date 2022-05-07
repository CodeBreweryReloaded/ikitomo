package ch.zhaw.ikitomo.ipc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import ch.zhaw.ikitomo.ipc.IPCCommand.IPCCommandType;

public record IPCCommand(@JsonProperty("command") IPCCommandType command, @JsonProperty("args") List<String> args) {
    /*
     * public IPCCommand(IPCCommandType command) {
     * this(command, Collections.emptyList());
     * }
     */
    @JsonCreator
    public IPCCommand(@JsonProperty("command") IPCCommandType command, @JsonProperty("args") List<String> args) {
        this.command = command;
        this.args = new ArrayList<>(args);
    }

    public List<String> args() {
        return new ArrayList<>(args);
    }

    public enum IPCCommandType {
        SHOW_SETTINGS
    }

    public static IPCCommand newShowSettingsCommand() {
        return new IPCCommand(IPCCommandType.SHOW_SETTINGS, Collections.emptyList());
    }

}
