package net.avarioncode.proxy.system.command.impl;

import net.avarioncode.proxy.mc.objects.Player;
import net.avarioncode.proxy.system.command.Command;

public class CommandLastPacket extends Command {
    public CommandLastPacket() {
        super(",lastpacket", "Wyswietla ostatni otrzymany pakiet od serwera", "");
    }

    @Override
    public void onExecute(String cmd, Player sender) throws Exception {
        sender.setDebugInfo(!sender.isDebugInfo());
        sender.sendChatMessage("&7Tryb lastpacket zostal %s", sender.isDebugInfo() ? "&awlaczone" : "&cwylaczone");
    }
}
