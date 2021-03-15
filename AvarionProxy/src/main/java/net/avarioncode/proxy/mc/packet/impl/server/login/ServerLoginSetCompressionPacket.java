package net.avarioncode.proxy.mc.packet.impl.server.login;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.avarioncode.proxy.mc.packet.Packet;
import net.avarioncode.proxy.mc.packet.PacketBuffer;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ServerLoginSetCompressionPacket extends Packet {

    private int threshold;

    {
        this.setPacketID(0x03);
    }

    @Override
    public void write(PacketBuffer out) throws Exception {
        out.writeVarIntToBuffer(this.threshold);
    }

    @Override
    public void read(PacketBuffer in) throws Exception {
        this.threshold = in.readVarIntFromBuffer();
    }
}
