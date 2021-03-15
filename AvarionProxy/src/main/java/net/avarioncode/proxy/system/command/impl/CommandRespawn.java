package net.avarioncode.proxy.system.command.impl;

import net.avarioncode.proxy.mc.objects.Player;
import net.avarioncode.proxy.mc.packet.impl.client.play.ClientStatusPacket;
import net.avarioncode.proxy.system.command.Command;

public class CommandRespawn extends Command {

    public CommandRespawn() {
        super(",respawn", "Odradza wszystkie boty", "");
    }

    @Override
    public void onExecute(String cmd, Player sender) throws Exception {
        if (sender.getConnector() != null && sender.getConnector().isConnected()) {
            if (!sender.getBots().isEmpty()) {
                sender.getBots().forEach(b -> {
                    if (b.getConnection().isConnected()) b.getConnection().sendPacket(new ClientStatusPacket(0));
                });
                sender.sendChatMessage("&7Boty zostaly pomyslnie odrodzone!");
            } else {
                sender.sendChatMessage("&7Nie masz zadnych polaczonych botow!");
            }
        } else {
            sender.sendChatMessage("&7Nie jestes polaczony z zadnym serwerem!");
        }
    }
}
