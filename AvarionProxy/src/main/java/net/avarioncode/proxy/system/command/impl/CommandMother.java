package net.avarioncode.proxy.system.command.impl;

import net.avarioncode.proxy.mc.objects.Player;
import net.avarioncode.proxy.system.command.Command;

public class CommandMother extends Command {

    public CommandMother() {
        super(",mother", "Boty odtwarzaja twoje ruchy", "");
    }

    @Override
    public void onExecute(String cmd, Player sender) throws Exception {
        sender.setMother(!sender.isMother());
        sender.sendChatMessage("&7Tryb mother zostal %s", sender.isMother() ? "&awlaczony" : "&cwylaczony");
    }
}
