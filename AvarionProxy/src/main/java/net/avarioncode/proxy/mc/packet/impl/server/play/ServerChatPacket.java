package net.avarioncode.proxy.mc.packet.impl.server.play;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.avarioncode.proxy.mc.data.chat.Message;
import net.avarioncode.proxy.mc.data.chat.MessagePosition;
import net.avarioncode.proxy.mc.packet.Packet;
import net.avarioncode.proxy.mc.packet.PacketBuffer;
import net.avarioncode.proxy.utils.LogUtil;

@NoArgsConstructor
@Data
public class ServerChatPacket extends Packet {

    private Message message;
    private MessagePosition position;

    {
        this.setPacketID(0x02);
    }

    public ServerChatPacket(String message) {
        this(message, MessagePosition.CHATBOX);
    }

    public ServerChatPacket(String message, MessagePosition position) {
        this.message = Message.fromString(LogUtil.fixColor(message));
        this.position = position;
    }

    @Override
    public void write(PacketBuffer out) throws Exception {
        out.writeString(message.toJsonString());
        out.writeByte(position.getId());
    }

    @Override
    public void read(PacketBuffer in) throws Exception {
        this.message = Message.fromString(in.readStringFromBuffer(32767));
        this.position = MessagePosition.getById(in.readByte());
    }
}
