package net.avarioncode.proxy.mc.packet.impl.client;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.avarioncode.proxy.AvarionProxy;
import net.avarioncode.proxy.mc.objects.Player;
import net.avarioncode.proxy.mc.packet.INetHandler;
import net.avarioncode.proxy.mc.packet.Packet;
import net.avarioncode.proxy.mc.packet.impl.client.play.ClientChatPacket;
import net.avarioncode.proxy.mc.packet.impl.client.play.ClientKeepAlivePacket;
import net.avarioncode.proxy.system.command.CommandManager;
import net.avarioncode.proxy.utils.LogUtil;

@RequiredArgsConstructor
@Data
public class NetHandlerPlayServer implements INetHandler {

    private final Player player;

    @Override
    public void disconnected() {
        player.getBots().forEach(bot -> bot.getConnection().close());
        LogUtil.printMessage("[%s] Wyszedl z proxy!", player.getUsername());
        AvarionProxy.getServer().getPlayerList().forEach(p -> p.sendChatMessage("&f" + player.getUsername() + "&7 wyszedl z proxy!"));
    }

    @Override
    public void handlePacket(Packet packet) {
        if (packet instanceof ClientKeepAlivePacket) {
            final int time = ((ClientKeepAlivePacket) packet).getTime();
            player.setPing((int) (System.currentTimeMillis() - time));
        } else if (packet instanceof ClientChatPacket) {
            final String message = ((ClientChatPacket) packet).getMessage();
            if (message.startsWith(",")) {
                CommandManager.onCommand(message, player);
            } else if (message.startsWith("@")) {
                AvarionProxy.getServer().getPlayerList().forEach(p -> p.sendChatMessageNoPrefix("&f" + player.getUsername() + " &8» &7" + message.substring(1)));
            } else {
                forwardPacket(packet);
            }
        } else {
            forwardPacket(packet);
        }
    }

    private void forwardPacket(final Packet packet) {
        if (player.getConnector() != null && player.getConnector().isConnected()) {
            if (player.isMother()) {
                player.getBots().forEach(bot -> {
                    if (bot.getConnection().isConnected()) {
                        //TODO add Entity Action exclude;
                        bot.getConnection().sendPacket(packet);
                    }
                });
            }
            player.getConnector().sendPacket(packet);
        }
    }
}
