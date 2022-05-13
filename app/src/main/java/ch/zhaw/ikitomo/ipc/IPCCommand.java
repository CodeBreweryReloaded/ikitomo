package ch.zhaw.ikitomo.ipc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import ch.zhaw.ikitomo.ipc.IPCCommand.IPCCommandType;

/**
 * A command which is sent and received by the
 * {@link ch.zhaw.ikitomo.ipc.IPCManager.IPCClient} and
 * {@link ch.zhaw.ikitomo.ipc.IPCManager.IPCServer}
 * 
 * @param command The IPC command type
 * @param args    the arguments for the command
 */
public record IPCCommand(@JsonProperty("command") IPCCommandType command, @JsonProperty("args") List<String> args) {
    /**
     * Constructor
     * 
     * @param command The IPC command type
     * @param args    the arguments for the command
     */
    @JsonCreator
    public IPCCommand(@JsonProperty("command") IPCCommandType command, @JsonProperty("args") List<String> args) {
        this.command = command;
        this.args = new ArrayList<>(args);
    }

    /**
     * Returns a copy of the arguments list
     * 
     * @returns A list of arguments
     */
    public List<String> args() {
        return new ArrayList<>(args);
    }

    /**
     * All possible types for IPC commands
     */
    public enum IPCCommandType {
        SHOW_SETTINGS
    }

    /**
     * Creates a new {@link ch.zhaw.ikitomo.ipc.IPCCommand} with the
     * {@link IPCCommandType#SHOW_SETTINGS} type
     * 
     * @return The newly created command
     */
    public static IPCCommand newShowSettingsCommand() {
        return new IPCCommand(IPCCommandType.SHOW_SETTINGS, Collections.emptyList());
    }

}
