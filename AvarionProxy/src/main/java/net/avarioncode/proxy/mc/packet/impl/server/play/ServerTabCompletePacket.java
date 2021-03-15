package net.avarioncode.proxy.mc.packet.impl.server.play;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.avarioncode.proxy.mc.packet.Packet;
import net.avarioncode.proxy.mc.packet.PacketBuffer;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ServerTabCompletePacket extends Packet {
    private String[] matches;

    {
        this.setPacketID(0x3A);
        this.setPacketID(0x0E);
    }

    @Override
    public void write(PacketBuffer out) throws Exception {
        out.writeVarIntToBuffer(this.matches.length);
        for (final String match : this.matches) {
            out.writeString(match);
        }
    }

    @Override
    public void read(PacketBuffer in) throws Exception {
        this.matches = new String[in.readVarInt()];
        for (int index = 0; index < this.matches.length; ++index) {
            this.matches[index] = in.readString();
        }
    }
}