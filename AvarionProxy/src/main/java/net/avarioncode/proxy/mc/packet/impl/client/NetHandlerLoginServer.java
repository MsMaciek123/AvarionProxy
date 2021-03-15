package net.avarioncode.proxy.mc.packet.impl.client;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.avarioncode.proxy.AvarionProxy;
import net.avarioncode.proxy.mc.data.network.EnumConnectionState;
import net.avarioncode.proxy.mc.objects.Player;
import net.avarioncode.proxy.mc.packet.INetHandler;
import net.avarioncode.proxy.mc.packet.Packet;
import net.avarioncode.proxy.mc.packet.impl.client.login.ClientLoginStartPacket;
import net.avarioncode.proxy.mc.packet.impl.server.login.ServerLoginDisconnectPacket;
import net.avarioncode.proxy.mc.packet.impl.server.login.ServerLoginSuccessPacket;
import net.avarioncode.proxy.utils.LogUtil;
import net.avarioncode.proxy.utils.WorldUtils;

import java.util.UUID;

@RequiredArgsConstructor
@Data
public class NetHandlerLoginServer implements INetHandler {

    private final Player player;

    @Override
    public void disconnected() {
        LogUtil.printMessage("[%s] Rozlaczono podczas dolaczania!", player.getUsername());
    }

    @Override
    public void handlePacket(Packet packet) {

        if (packet instanceof ClientLoginStartPacket) {
            final String playerName = ((ClientLoginStartPacket) packet).getUsername();
            if (AvarionProxy.getServer().getPlayerList().size() > 1) {
                for (Player p : AvarionProxy.getServer().getPlayerList()) {
                    if (p.getUsername() != null && p.getUsername().equals(playerName)) {
                        player.sendPacket(new ServerLoginDisconnectPacket());
                        return;
                    }
                }
            }
            player.setUsername(((ClientLoginStartPacket) packet).getUsername());
            player.setCompressionThreshold(256);
            player.sendPacket(new ServerLoginSuccessPacket(UUID.randomUUID(), player.getUsername()));
            LogUtil.printMessage("[%s] Dolaczyl na proxy!", player.getUsername());
            player.setConnectionState(EnumConnectionState.PLAY);
            player.setPacketHandler(new NetHandlerPlayServer(player));
            WorldUtils.emptyWorld(player);
            WorldUtils.lobby(player, "Lobby.dat");
            AvarionProxy.getServer().playerList.add(player);
            player.sendTitle("&dAvarionProxy", "&7Witaj &f" + player.getUsername(), 30, 40, 30);
            AvarionProxy.getServer().getPlayerList().forEach(p -> p.sendChatMessage("&f" + player.getUsername() + "&7 dolaczyl na proxy!"));
        }
    }
}
