package net.avarioncode.proxy.system.command.impl;

import net.avarioncode.proxy.mc.connection.ServerConnector;
import net.avarioncode.proxy.mc.connection.ServerPinger;
import net.avarioncode.proxy.mc.objects.Player;
import net.avarioncode.proxy.system.command.Command;
import net.avarioncode.proxy.utils.NetUtils;
import net.avarioncode.proxy.utils.ProxyUtils;

import java.net.Proxy;

public class CommandJoin extends Command {

    public CommandJoin() {
        super(",connect", "Laczenie z serwerem docelowym", "<host:port> <username> <proxy>");
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
        final String username = args[2];
        final ServerConnector connector = new ServerConnector(sender, username);
        sender.setConnector(connector);

        final Proxy proxy = ProxyUtils.getProxyByName(args[3]);

        if (proxy != Proxy.NO_PROXY) {
            if (NetUtils.checkProxy(proxy, 300) == -1) {
                sender.sendChatMessage("&7Brak odpowiedzi od proxy!");
                return;
            }
        }
        final ServerPinger pinger = new ServerPinger(sender, true, connector);
        pinger.connect(host, port, proxy);
    }
}
