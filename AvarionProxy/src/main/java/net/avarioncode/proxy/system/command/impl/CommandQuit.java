package net.avarioncode.proxy.system.command.impl;

import net.avarioncode.proxy.mc.objects.Player;
import net.avarioncode.proxy.system.command.Command;
import net.avarioncode.proxy.utils.WorldUtils;

public class CommandQuit extends Command {

    public CommandQuit() {
        super(",q", "Odlacza od serwera docelowego", "");
    }

    @Override
    public void onExecute(String cmd, Player sender) throws Exception {
        if (sender.getConnector() != null && sender.getConnector().isConnected()) {
            sender.getConnector().close();
            WorldUtils.emptyWorld(sender);
            WorldUtils.lobby(sender, "Lobby.dat");
            sender.getBots().forEach(bot -> bot.getConnection().close());
            sender.sendChatMessage("&7Odlaczono od serwera!");
        } else {
            sender.sendChatMessage("&7Nie jestes polaczony z zadnym serwerem!");
        }
    }
}
