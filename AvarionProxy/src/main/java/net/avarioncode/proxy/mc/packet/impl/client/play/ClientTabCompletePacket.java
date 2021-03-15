package net.avarioncode.proxy.mc.packet.impl.client.play;

import lombok.Getter;
import lombok.NoArgsConstructor;
import net.avarioncode.proxy.mc.data.Position;
import net.avarioncode.proxy.mc.packet.Packet;
import net.avarioncode.proxy.mc.packet.PacketBuffer;

@Getter
@NoArgsConstructor
public class ClientTabCompletePacket extends Packet {
    private String text;
    private Position position;
    private boolean assumeCMD;

    {
        this.setPacketID(0x14);
    }

    public ClientTabCompletePacket(final String text) {
        this(text, null);
    }

    public ClientTabCompletePacket(final String text, final Position position) {
        this.text = text;
        this.position = position;
    }

    public ClientTabCompletePacket(final String text, final boolean assumeCMD, final Position position) {
        this.text = text;
        this.assumeCMD = assumeCMD;
        this.position = position;
    }

    @Override
    public void write(PacketBuffer out) throws Exception {
        out.writeString(this.text);
        out.writeBoolean(this.position != null);
        if (this.position != null) {
            out.writePosition(position);
        }
    }

    @Override
    public void read(PacketBuffer in) throws Exception {
        this.text = in.readString();
        this.position = (in.readBoolean() ? in.readPosition() : null);
    }
}