package net.avarioncode.proxy.mc.packet;

import java.util.Arrays;

public enum Face {
    BOTTOM(0),
    TOP(1),
    EAST(2),
    WEST(3),
    NORTH(4),
    SOUTH(5),
    SPECIAL(255);

    private int id;

    Face(int id) {
        this.id = id;
    }

    public static Face getById(int id) {
        return Arrays.<Face>stream(values()).filter(gm -> (gm.id == id)).findFirst().orElse(BOTTOM);
    }

    public int getId() {
        return this.id;
    }
}

