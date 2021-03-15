package net.avarioncode.proxy.system.command.impl;

import net.avarioncode.proxy.mc.objects.Player;
import net.avarioncode.proxy.mc.packet.impl.client.play.ClientChatPacket;
import net.avarioncode.proxy.system.command.Command;

public class CommandBotRegister extends Command {

    public CommandBotRegister() {
        super(",register", "Automatyczna rejestracja botow", "");
    }

    @Override
    public void onExecute(String cmd, Player sender) throws Exception {
        if (sender.getConnector() != null && sender.getConnector().isConnected()) {
            if (sender.getBots().size() == 0) {
                sender.sendChatMessage("&7Nie masz zadnych polaczonych botow!", sender, true);
                return;
            }
            sender.getBots().forEach(bot -> {
                bot.getConnection().sendPacket(new ClientChatPacket("/login AvarionProxy123@"));
                bot.getConnection().sendPacket(new ClientChatPacket("/register AvarionProxy123@"));
            });
            sender.sendChatMessage("&7Pomyslnie zarejestrowano &f" + sender.getBots().size() + " &7botow");
        } else {
            sender.sendChatMessage("&7Nie jestes polaczony z zadnym serwerem!");
        }
    }
}