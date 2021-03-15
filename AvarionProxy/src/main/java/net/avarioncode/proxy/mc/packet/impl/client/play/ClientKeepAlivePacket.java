package net.avarioncode.proxy.mc.packet.impl.client.play;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.avarioncode.proxy.mc.packet.Packet;
import net.avarioncode.proxy.mc.packet.PacketBuffer;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ClientKeepAlivePacket extends Packet {

    private int time;

    {
        this.setPacketID(0x00);
    }

    @Override
    public void write(PacketBuffer out) throws Exception {
        out.writeVarIntToBuffer(this.time);
    }

    @Override
    public void read(PacketBuffer in) throws Exception {
        this.time = in.readVarIntFromBuffer();
    }
}
