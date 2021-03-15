package net.avarioncode.proxy.mc.packet.impl.server.play;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.avarioncode.proxy.mc.data.chat.Message;
import net.avarioncode.proxy.mc.packet.Packet;
import net.avarioncode.proxy.mc.packet.PacketBuffer;
import net.avarioncode.proxy.utils.LogUtil;

@Data
@NoArgsConstructor
public class ServerDisconnectPacket extends Packet {

    private Message reason;

    {
        this.setPacketID(0x40);
    }

    public ServerDisconnectPacket(String reason) {
        this.reason = Message.fromString(LogUtil.fixColor(reason));
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
