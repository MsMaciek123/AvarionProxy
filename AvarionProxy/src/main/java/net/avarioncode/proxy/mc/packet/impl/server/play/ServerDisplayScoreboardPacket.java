package net.avarioncode.proxy.mc.packet.impl.server.play;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.avarioncode.proxy.mc.packet.Packet;
import net.avarioncode.proxy.mc.packet.PacketBuffer;

@Getter
@NoArgsConstructor
@AllArgsConstructor

public class ServerDisplayScoreboardPacket extends Packet {
    private int position;
    private String scoreName;

    {
        this.setPacketID(0x3D);
        this.setPacketID(0x38);
        this.setPacketID(0x3B);
    }

    @Override
    public void write(PacketBuffer out) throws Exception {
        out.writeByte(this.position);
        out.writeString(this.scoreName);
    }

    @Override
    public void read(PacketBuffer in) throws Exception {
        this.position = in.readByte();
        this.scoreName = in.readString(32767);
    }
}