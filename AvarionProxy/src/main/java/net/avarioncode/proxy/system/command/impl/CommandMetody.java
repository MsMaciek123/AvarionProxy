package net.avarioncode.proxy.system.command.impl;

import net.avarioncode.proxy.mc.objects.Player;
import net.avarioncode.proxy.system.command.Command;

public class CommandMetody extends Command {

    public CommandMetody() {
        super(",metody", "Wyswietla metody crashowania", "");
    }

    @Override
    public void onExecute(String cmd, Player sender) throws Exception {
        sender.sendChatMessage("&7Dostepne metody: &fmagic1&7, &fmagic2&7, &fmagic3&7, &fhydra1&7, &fhydra2&7, &fburn1&7, &fbypass&7");
    }
}
