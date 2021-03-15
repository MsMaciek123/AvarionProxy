package net.avarioncode.proxy.system.crash;

import net.avarioncode.proxy.mc.objects.Player;
import net.avarioncode.proxy.system.command.Command;
import net.avarioncode.proxy.system.command.CommandManager;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CrashRegistry {

    public static List<Crash> crashList = new ArrayList<>();

    public static void init() {
        CommandManager.registerCommand(new Command(",crash", "Komenda do crashowania", "<method> <amount>") {
            @Override
            public void onExecute(String cmd, Player sender) {
                try {
                    execute(cmd, sender);
                } catch (final Throwable t) {
                    t.printStackTrace();
                }
            }
        });
        new Reflections("net.avarioncode.proxy.system.crash.impl").getSubTypesOf(Crash.class).forEach(crash -> {
            try {
                Crash c = crash.newInstance();
                crashList.add(c);
                c.init();
            } catch (Exception ignored) {
            }
        });
    }

    public static void execute(final String message, final Player sender) {
        final String[] args = message.split(" ");
        if (sender.getConnector() != null && sender.getConnector().isConnected()) {
            if (args.length != 3) {
                sender.sendChatMessage("&7Poprawne uzycie: &f,crash <method> <amount>");
                return;
            }
            int packets = Integer.parseInt(args[2]);
            if (packets > 300) {
                sender.sendChatMessage("&7Maksymalna ilosc pakietow wynosi &f300&7!");
                return;
            }
            final Optional<Crash> crashOptional = crashList.stream().filter(c -> c.getName().equalsIgnoreCase(args[1])).findFirst();
            if (crashOptional.isPresent()) {
                try {
                    crashOptional.get().execute(message, sender);
                } catch (final Exception exc) {
                    sender.sendChatMessage("&7Wystapil blad podczas wysylania pakietow!");
                }
            } else {
                sender.sendChatMessage("&7Metoda nie zostala odnaleziona!");
            }
        } else {
            sender.sendChatMessage("&7Nie jestes polaczony z zadnym serwerem!");
        }
    }
}
