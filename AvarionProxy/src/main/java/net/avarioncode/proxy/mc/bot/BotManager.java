package net.avarioncode.proxy.mc.bot;

import net.avarioncode.proxy.mc.connection.ServerPinger;
import net.avarioncode.proxy.mc.objects.Bot;
import net.avarioncode.proxy.mc.objects.Player;
import net.avarioncode.proxy.utils.ProxyUtils;

import java.net.Proxy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class BotManager {

    public static void connectSome(final String host, final int port, final String usernames, final int amount, final int delay, final boolean ping, final String proxy, final Player sender) {
        final ExecutorService service = Executors.newSingleThreadExecutor();
        service.submit(() -> {
            try {
                for (int i = 0; i < amount; i++) {
                    final String username = (usernames + i);

                    final Proxy foundProxy = ProxyUtils.getProxyByName(proxy);
                    final Bot bot = new Bot(username, sender);
                    if (ping) {
                        final ServerPinger pinger = new ServerPinger(sender, false, bot.getConnection());
                        pinger.connect(host, port, foundProxy);
                    } else {
                        bot.connect(host, port, foundProxy);
                    }
                    TimeUnit.MILLISECONDS.sleep(delay);
                }
                sender.sendHotbar("&7Wyslano &fwszystkie &7boty!");
            } catch (Exception exc) {
                sender.sendChatMessage("&cWystapil blad podczas wysylania botow!");
            }
        });
    }

}
