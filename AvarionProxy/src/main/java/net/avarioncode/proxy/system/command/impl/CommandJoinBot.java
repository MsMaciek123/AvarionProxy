package net.avarioncode.proxy.system.command.impl;

import net.avarioncode.proxy.mc.bot.BotManager;
import net.avarioncode.proxy.mc.objects.Player;
import net.avarioncode.proxy.system.command.Command;
import net.avarioncode.proxy.utils.NetUtils;

public class CommandJoinBot extends Command {

    public CommandJoinBot() {
        super(",connectbot", "Laczenie botow na serwer docelowy", "<host:port> <usernames> <amount> <delay> <proxies>");
    }

    @Override
    public void onExecute(String cmd, Player sender) throws Exception {
        final String[] args = cmd.split(" ");
        String host = args[1];
        int port = 25565;
        if (host.contains(":")) {
            final String[] sp = host.split(":", 2);
            host = sp[0];
            port = Integer.parseInt(sp[1]);
        }
        if (NetUtils.checkSocketConnection(host, port, 500) == -1) {
            final String[] resolved = NetUtils.getServerAddress(host);
            host = resolved[0];
            port = Integer.parseInt(resolved[1]);
            if (NetUtils.checkSocketConnection(host, port, 500) == -1) {
                sender.sendChatMessage("&7Nie mozna nawiazac polaczenia z serwerem!");
                return;
            }
        }
        final String usernames = args[2];
        final int amount = Integer.parseInt(args[3]);
        final int delay = Integer.parseInt(args[4]);
        final String proxy = args[5];
        sender.sendChatMessage("&7Trwa wysylanie botow!");
        BotManager.connectSome(host, port, usernames, amount, delay, true, proxy, sender);
    }
}
