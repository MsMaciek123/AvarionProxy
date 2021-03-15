package net.avarioncode.proxy.system.command;

import com.darkmagician6.eventapi.EventManager;
import net.avarioncode.proxy.events.PlayerCommandEvent;
import net.avarioncode.proxy.mc.objects.Player;
import net.avarioncode.proxy.utils.LogUtil;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CommandManager {

    public static final List<Command> commands = new ArrayList<>();
    //private static final HashMap<String, Long> cooldown = new HashMap<>();

    public static void init() {
        new Reflections("net.avarioncode.proxy.system.command.impl").getSubTypesOf(Command.class).forEach(command -> {
            try {
                commands.add(command.newInstance());
            } catch (Exception ignored) {
            }
        });
    }

    public static void registerCommand(final Command command) {
        commands.add(command);
    }

    public static void onCommand(final String message, final Player sender) {
        final String[] args = message.split(" ");
        final Optional<Command> commandOptional = commands.stream().filter(cmd -> cmd.getPrefix().equalsIgnoreCase(args[0])).findFirst();
        /*if (cooldown.containsKey(sender.getUsername())) {
            final long secondsLeft = cooldown.get(sender.getUsername()) / 1000L + 2 - System.currentTimeMillis() / 1000L;
            if (secondsLeft > 0L) {
                sender.sendChatMessage("&7Nastepna komende mozesz uzyc za &f" + secondsLeft + "s");
                return;
            }
        }
        cooldown.put(sender.getUsername(), System.currentTimeMillis());

         */
        if (commandOptional.isPresent()) {
            try {
                final PlayerCommandEvent event = new PlayerCommandEvent(message, sender);
                EventManager.call(event);
                if (!event.isCancelled()) {
                    commandOptional.get().onExecute(message, sender);
                    LogUtil.printMessage("[%s] " + message, sender.getUsername());
                }
            } catch (final Exception exc) {
                sender.sendChatMessage("&7Poprawne uzycie&8: &f" + commandOptional.get().getPrefix() + " " + commandOptional.get().getUsage());
            }
        } else {
            sender.sendChatMessage("&7Nie odnaleziono wybranej komendy!");
        }
    }

}
