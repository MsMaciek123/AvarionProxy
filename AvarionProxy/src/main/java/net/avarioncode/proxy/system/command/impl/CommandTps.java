package net.avarioncode.proxy.system.command.impl;

import net.avarioncode.proxy.mc.objects.Player;
import net.avarioncode.proxy.system.command.Command;

public class CommandTps extends Command {

    public CommandTps() {
        super(",tps", "Sprawdza tpsy serwera docelowego", "");
    }

    @Override
    public void onExecute(String cmd, Player sender) throws Exception {
        if (sender.getConnector() != null && sender.getConnector().isConnected()) {
            sender.sendChatMessage("&7TPS serwera&8: &f" + sender.getConnector().getLastTps());
        } else {
            sender.sendChatMessage("&7Nie jestes polaczony z zadnym serwerem!");
        }
    }
}
