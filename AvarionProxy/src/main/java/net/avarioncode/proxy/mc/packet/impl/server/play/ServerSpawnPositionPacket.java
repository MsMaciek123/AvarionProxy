package net.avarioncode.proxy.mc.packet.impl.server.play;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.avarioncode.proxy.mc.data.Position;
import net.avarioncode.proxy.mc.packet.Packet;
import net.avarioncode.proxy.mc.packet.PacketBuffer;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ServerSpawnPositionPacket extends Packet {

    private Position position;

    {
        this.setPacketID(0x05);
    }

    @Override
    public void write(PacketBuffer out) throws Exception {
        out.writePosition(this.position);
    }

    @Override
    public void read(PacketBuffer in) throws Exception {
        this.position = in.readPosition();
    }
}
