package net.avarioncode.proxy.system.command.impl;

import net.avarioncode.proxy.AvarionProxy;
import net.avarioncode.proxy.mc.objects.Player;
import net.avarioncode.proxy.system.command.Command;

public class CommandAlert extends Command {

    public CommandAlert() {
        super(",alert", "Wyswietla alert wszystkim uzytkownikom", "<message>");
    }

    @Override
    public void onExecute(String cmd, Player sender) throws Exception {
        final String message = cmd.split(",alert ", 2)[1];
        if (sender.getUsername().equalsIgnoreCase("KomoraGazowa") || sender.getUsername().equalsIgnoreCase("Meeku__")) {
            AvarionProxy.getServer().getPlayerList().forEach(p -> p.sendTitle("&8[&4&lALERT&8]", message, 30, 30, 30));
        } else {
            sender.sendChatMessage("&7Nie masz uprawnien do uzycia tej komendy!");
        }
    }
}
