package net.avarioncode.proxy.mc.packet.impl.client;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.avarioncode.proxy.AvarionProxy;
import net.avarioncode.proxy.mc.objects.Player;
import net.avarioncode.proxy.mc.packet.INetHandler;
import net.avarioncode.proxy.mc.packet.Packet;
import net.avarioncode.proxy.mc.packet.impl.client.status.ClientStatusPingPacket;
import net.avarioncode.proxy.mc.packet.impl.client.status.ClientStatusRequestPacket;
import net.avarioncode.proxy.mc.packet.impl.server.status.ServerStatusPongPacket;
import net.avarioncode.proxy.mc.packet.impl.server.status.ServerStatusResponsePacket;

@RequiredArgsConstructor
@Data
public class NetHandlerStatusServer implements INetHandler {

    private final Player owner;

    @Override
    public void disconnected() {
    }

    @Override
    public void handlePacket(Packet packet) {
        if (packet instanceof ClientStatusRequestPacket) {
            owner.sendPacket(new ServerStatusResponsePacket(AvarionProxy.getServer().getStatusInfo()));
            owner.getChannel().close();
        } else if (packet instanceof ClientStatusPingPacket) {
            owner.sendPacket(new ServerStatusPongPacket(((ClientStatusPingPacket) packet).getTime()));
            owner.getChannel().close();
        }
    }
}
