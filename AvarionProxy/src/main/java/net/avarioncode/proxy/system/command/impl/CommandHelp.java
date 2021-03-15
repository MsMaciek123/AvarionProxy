package net.avarioncode.proxy.system.command.impl;

import net.avarioncode.proxy.mc.objects.Player;
import net.avarioncode.proxy.system.command.Command;
import net.avarioncode.proxy.system.command.CommandManager;

public class CommandHelp extends Command {

    public CommandHelp() {
        super(",help", "Wyswietla liste komend", "");
    }

    @Override
    public void onExecute(String cmd, Player sender) throws Exception {
        CommandManager.commands.forEach(command -> sender.sendChatMessage("&f%s &7%s &8- &7%s", command.getPrefix(), command.getUsage(), command.getDesc()));
    }
}
