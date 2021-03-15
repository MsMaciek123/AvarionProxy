package net.avarioncode.proxy.utils;

import net.avarioncode.proxy.mc.objects.Player;
import net.avarioncode.proxy.mc.packet.impl.server.play.ServerPlayerAbilitiesPacket;

public class PacketUtils {
    public static void speed(Player sender, final int speed) {
        sender.sendPacket(new ServerPlayerAbilitiesPacket(false, false, false, false, 1.0f, (float) speed));
    }
}
