package net.avarioncode.proxy.mc.packet.impl.client.play;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.avarioncode.proxy.mc.data.Position;
import net.avarioncode.proxy.mc.data.item.ItemStack;
import net.avarioncode.proxy.mc.packet.Face;
import net.avarioncode.proxy.mc.packet.Packet;
import net.avarioncode.proxy.mc.packet.PacketBuffer;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ClientPlayerPlaceBlockPacket extends Packet {
    private Position position;
    private Face face;
    private ItemStack held;
    private float cursorX;
    private float cursorY;
    private float cursorZ;
    private int hand;

    {
        this.setPacketID(0x08);
    }

    public ClientPlayerPlaceBlockPacket(Position position, Face face, ItemStack held, float cursorX, float cursorY, float cursorZ) {
        this.position = position;
        this.face = face;
        this.held = held;
        this.cursorX = cursorX;
        this.cursorY = cursorY;
        this.cursorZ = cursorZ;
    }

    public ClientPlayerPlaceBlockPacket(Position position, Face face, int hand, float cursorX, float cursorY, float cursorZ) {
        this.position = position;
        this.face = face;
        this.hand = hand;
        this.cursorX = cursorX;
        this.cursorY = cursorY;
        this.cursorZ = cursorZ;
    }

    @Override
    public void write(PacketBuffer out) throws Exception {
        out.writePosition(position);
        out.writeByte(this.face.getId());
        out.writeItemStackToBuffer(held);
        out.writeByte((int) (this.cursorX * 16.0F));
        out.writeByte((int) (this.cursorY * 16.0F));
        out.writeByte((int) (this.cursorZ * 16.0F));
    }

    @Override
    public void read(PacketBuffer in) throws Exception {
        this.position = in.readPosition();
        this.face = Face.getById(in.readUnsignedByte());
        this.held = in.readItemStackFromBuffer();
        this.cursorX = (float) in.readByte() / 16.0F;
        this.cursorY = (float) in.readByte() / 16.0F;
        this.cursorZ = (float) in.readByte() / 16.0F;
    }
}