package net.avarioncode.proxy.mc.packet.impl.server.play;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.avarioncode.proxy.mc.data.scoreboard.ObjectiveMode;
import net.avarioncode.proxy.mc.data.scoreboard.ObjectiveType;
import net.avarioncode.proxy.mc.packet.Packet;
import net.avarioncode.proxy.mc.packet.PacketBuffer;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ServerScoreboardObjectivePacket extends Packet {
    private String objectiveName;
    private ObjectiveMode objectiveMode;
    private String objectiveValue;
    private ObjectiveType objectiveType;

    {
        this.setPacketID(0x3B);
        this.setPacketID(0x3F);
        this.setPacketID(0x42);
    }


    @Override
    public void write(PacketBuffer out) throws Exception {
        out.writeString(this.objectiveName);
        out.writeByte(this.objectiveMode.getId());
        if (this.objectiveMode.getId() == 0 || this.objectiveMode.getId() == 2) {
            out.writeString(this.objectiveValue);
            out.writeString(this.objectiveType.getValue());
        }
    }

    @Override
    public void read(PacketBuffer in) throws Exception {
        this.objectiveName = in.readString(128);
        this.objectiveMode = ObjectiveMode.getById(in.readByte());
        if (this.objectiveMode.getId() == 0 || this.objectiveMode.getId() == 2) {
            this.objectiveValue = in.readString(32767);
            this.objectiveType = ObjectiveType.getByValue(in.readString(32));
        }
    }
}