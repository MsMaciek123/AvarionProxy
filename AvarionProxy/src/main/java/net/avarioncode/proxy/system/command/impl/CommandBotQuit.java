package net.avarioncode.proxy.system.command.impl;

import net.avarioncode.proxy.mc.objects.Player;
import net.avarioncode.proxy.system.command.Command;

public class CommandBotQuit extends Command {

    public CommandBotQuit() {
        super(",qall", "Odlacza boty od serwera docelowego", "");
    }

    @Override
    public void onExecute(String cmd, Player sender) throws Exception {
        if (sender.getConnector() != null && sender.getConnector().isConnected()) {
            if (sender.getBots().isEmpty()) {
                sender.sendChatMessage("&7Nie masz zadnych polaczonych botow!");
            } else {
                sender.getBots().forEach(bot -> bot.getConnection().close());
                sender.getBots().forEach(bot -> bot.getConnection().close());
                sender.sendChatMessage("&7Rozlaczono wszystkie boty!");
            }
        } else {
            sender.sendChatMessage("&7Nie jestes polaczony z zadnym serwerem!");
        }
    }
}
