package net.avarioncode.proxy.mc.packet.impl.server.play;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.avarioncode.proxy.mc.packet.Packet;
import net.avarioncode.proxy.mc.packet.PacketBuffer;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ServerUpdateScorePacket extends Packet {
    private String scoreName;
    private int action;
    private String objectiveName;
    private int value;

    {
        this.setPacketID(0x3C);
        this.setPacketID(0x42);
        this.setPacketID(0x45);
    }

    @Override
    public void write(PacketBuffer out) throws Exception {
        out.writeString(this.scoreName);
        out.writeByte(this.action);
        out.writeString(this.objectiveName);
        if (action != 1) {
            out.writeVarIntToBuffer(this.value);
        }
    }

    @Override
    public void read(PacketBuffer in) throws Exception {
        this.scoreName = in.readString(128);
        this.action = in.readByte();
        this.objectiveName = in.readString(32767);
        if (action != 1) {
            this.value = in.readVarInt();
        }
    }
}