package net.avarioncode.proxy.mc.packet.impl.server.play;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.avarioncode.proxy.mc.packet.Packet;
import net.avarioncode.proxy.mc.packet.PacketBuffer;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ServerTimeUpdatePacket extends Packet {

    private long worldAge;
    private long dayTime;

    {
        this.setPacketID(0x03);
    }

    @Override
    public void write(PacketBuffer out) throws Exception {
        out.writeLong(this.worldAge);
        out.writeLong(this.dayTime);
    }

    @Override
    public void read(PacketBuffer in) throws Exception {
        this.worldAge = in.readLong();
        this.dayTime = in.readLong();
    }
}
