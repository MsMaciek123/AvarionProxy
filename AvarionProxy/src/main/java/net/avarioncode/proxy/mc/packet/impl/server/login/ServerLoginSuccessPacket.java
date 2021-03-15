package net.avarioncode.proxy.mc.packet.impl.server.login;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.avarioncode.proxy.mc.packet.Packet;
import net.avarioncode.proxy.mc.packet.PacketBuffer;

import java.util.UUID;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ServerLoginSuccessPacket extends Packet {

    private UUID uuid;
    private String username;

    {
        this.setPacketID(0x02);
    }

    @Override
    public void write(PacketBuffer out) throws Exception {
        out.writeString(this.uuid.toString());
        out.writeString(this.username);
    }

    @Override
    public void read(PacketBuffer in) throws Exception {
        this.uuid = UUID.fromString(in.readStringFromBuffer(86));
        this.username = in.readStringFromBuffer(32);
    }
}
