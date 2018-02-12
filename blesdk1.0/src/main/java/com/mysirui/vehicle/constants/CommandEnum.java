package com.mysirui.vehicle.constants;

/**
 * Created by marlin on 2017/8/21.
 */

public enum CommandEnum {
    LOCK(1),
    UNLOCK(2),
    START(5),
    STOP(6),
    CALL(3),
    OPEN_WINDOW(8),
    CLOES_WINDOW(7);


    private int commandID;

    public int getCommandID() {
        return commandID;
    }

    CommandEnum(int commandID) {
        this.commandID = commandID;
    }
}
