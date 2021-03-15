package net.avarioncode.proxy.mc.packet.impl.server.login;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.avarioncode.proxy.mc.data.chat.Message;
import net.avarioncode.proxy.mc.packet.Packet;
import net.avarioncode.proxy.mc.packet.PacketBuffer;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ServerLoginDisconnectPacket extends Packet {

    private Message reason;

    {
        this.setPacketID(0x00);
    }

    @Override
    public void write(PacketBuffer out) throws Exception {
        out.writeString(reason.toJsonString());
    }

    @Override
    public void read(PacketBuffer in) throws Exception {
        this.reason = Message.fromString(in.readStringFromBuffer(32767));
    }
}
