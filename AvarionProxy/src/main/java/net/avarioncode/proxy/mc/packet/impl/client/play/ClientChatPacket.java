package net.avarioncode.proxy.mc.packet.impl.client.play;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.avarioncode.proxy.mc.packet.Packet;
import net.avarioncode.proxy.mc.packet.PacketBuffer;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ClientChatPacket extends Packet {

    private String message;

    {
        this.setPacketID(0x01);
    }

    @Override
    public void write(PacketBuffer out) throws Exception {
        out.writeString(this.message);
    }

    @Override
    public void read(PacketBuffer in) throws Exception {
        this.message = in.readStringFromBuffer(32767);
    }
}
