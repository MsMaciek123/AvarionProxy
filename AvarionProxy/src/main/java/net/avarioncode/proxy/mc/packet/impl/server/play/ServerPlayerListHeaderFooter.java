package net.avarioncode.proxy.mc.packet.impl.server.play;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.avarioncode.proxy.mc.data.chat.Message;
import net.avarioncode.proxy.mc.packet.Packet;
import net.avarioncode.proxy.mc.packet.PacketBuffer;
import net.avarioncode.proxy.utils.LogUtil;

@NoArgsConstructor
@Data
public class ServerPlayerListHeaderFooter extends Packet {

    private Message header, footer;

    {
        this.setPacketID(0x47);
    }

    public ServerPlayerListHeaderFooter(String header, String footer) {
        this.header = Message.fromString(LogUtil.fixColor(header));
        this.footer = Message.fromString(LogUtil.fixColor(footer));
    }

    @Override
    public void write(PacketBuffer out) throws Exception {
        out.writeString(header.toJsonString());
        out.writeString(footer.toJsonString());
    }

    @Override
    public void read(PacketBuffer in) throws Exception {
        this.header = Message.fromString(in.readStringFromBuffer(32767));
        this.footer = Message.fromString(in.readStringFromBuffer(32767));
    }
}
