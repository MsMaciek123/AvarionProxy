package net.avarioncode.proxy.mc.packet.impl.client.play;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.avarioncode.proxy.mc.data.item.ItemStack;
import net.avarioncode.proxy.mc.packet.Packet;
import net.avarioncode.proxy.mc.packet.PacketBuffer;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClientCreativeInventoryAction extends Packet {

    private int slot;
    private ItemStack itemStack;

    {
        this.setPacketID(0x10);
    }

    @Override
    public void write(PacketBuffer out) throws Exception {
        out.writeShort(slot);
        out.writeItemStackToBuffer(itemStack);
    }

    @Override
    public void read(PacketBuffer in) throws Exception {
        this.slot = in.readShort();
        this.itemStack = in.readItemStackFromBuffer();
    }
}
