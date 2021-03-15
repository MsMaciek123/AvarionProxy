package net.avarioncode.proxy.system.command.impl;

import net.avarioncode.proxy.mc.objects.Player;
import net.avarioncode.proxy.mc.packet.impl.client.play.ClientChatPacket;
import net.avarioncode.proxy.system.command.Command;

public class CommandBroadcast extends Command {

    public CommandBroadcast() {
        super(",bc", "Boty wysylaja twoja wiadomosc", "<message>");
    }

    @Override
    public void onExecute(String cmd, Player sender) throws Exception {
        if (sender.getConnector() != null && sender.getConnector().isConnected()) {
            final String message = cmd.split(",bc ", 2)[1];
            if (sender.getBots().isEmpty()) {
                sender.sendChatMessage("&7Nie masz zadnych polaczonych botow!");
            } else {
                sender.getBots().forEach(bot -> {
                    if (bot.getConnection().isConnected()) {
                        bot.getConnection().sendPacket(new ClientChatPacket(message));
                    }
                });
            }
        } else {
            sender.sendChatMessage("&7Nie jestes polaczony z zadnym serwerem!");
        }
    }
}
