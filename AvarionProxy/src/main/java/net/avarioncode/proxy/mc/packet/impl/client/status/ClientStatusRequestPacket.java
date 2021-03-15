package net.avarioncode.proxy.mc.packet.impl.client.status;

import net.avarioncode.proxy.mc.packet.Packet;
import net.avarioncode.proxy.mc.packet.PacketBuffer;

public class ClientStatusRequestPacket extends Packet {

    {
        this.setPacketID(0x00);
    }

    @Override
    public void write(PacketBuffer out) throws Exception {
    }

    @Override
    public void read(PacketBuffer in) throws Exception {
    }
}
