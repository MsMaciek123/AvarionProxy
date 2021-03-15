package net.avarioncode.proxy.system.command.impl;

import net.avarioncode.proxy.mc.objects.Player;
import net.avarioncode.proxy.system.command.Command;
import net.avarioncode.proxy.utils.WorldUtils;

public class CommandLobby extends Command {

    public CommandLobby() {
        super(",lobby", "Teleportuje do lobby", "");
    }

    @Override
    public void onExecute(String cmd, Player sender) throws Exception {
        if (sender.getConnector() == null) {
            WorldUtils.emptyWorld(sender);
            WorldUtils.lobby(sender, "Lobby.dat");
            sender.sendChatMessage("&7Teleportowano na lobby!");
        } else {
            sender.sendChatMessage("&7Nie mozesz byc polaczony z zadnym serwerem!");
        }
    }
}
