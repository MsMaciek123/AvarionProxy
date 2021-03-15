package net.avarioncode.proxy.system.command.impl;

import net.avarioncode.proxy.AvarionProxy;
import net.avarioncode.proxy.mc.objects.Player;
import net.avarioncode.proxy.system.command.Command;

public class CommandList extends Command {

    public CommandList() {
        super(",list", "Wyswietla liste osob na proxy", "");
    }

    @Override
    public void onExecute(String cmd, Player sender) throws Exception {
        sender.sendChatMessage("&7Lista graczy: &f" + AvarionProxy.getServer().getPlayerList().size() + "");
    }
}
