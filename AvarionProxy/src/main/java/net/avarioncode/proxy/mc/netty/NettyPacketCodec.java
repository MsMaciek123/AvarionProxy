package net.avarioncode.proxy.mc.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.codec.DecoderException;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.avarioncode.proxy.mc.data.network.EnumConnectionState;
import net.avarioncode.proxy.mc.data.network.EnumPacketDirection;
import net.avarioncode.proxy.mc.packet.Packet;
import net.avarioncode.proxy.mc.packet.PacketBuffer;
import net.avarioncode.proxy.mc.packet.impl.CustomPacket;
import net.avarioncode.proxy.mc.packet.registry.PacketRegistry;

import java.util.List;

@AllArgsConstructor
@Data
public class NettyPacketCodec extends ByteToMessageCodec<Packet> {

    private EnumConnectionState connectionState;
    private EnumPacketDirection packetDirection;

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Packet packet, ByteBuf byteBuf) throws Exception {
        final PacketBuffer packetbuffer = new PacketBuffer(byteBuf);
        if (packet.isCustom()) {
            packetbuffer.writeVarIntToBuffer(packet.getPacketID());
            packetbuffer.writeBytes(packet.getCustomData());
        } else {
            packetbuffer.writeVarIntToBuffer(packet.getPacketID());
            packet.write(packetbuffer);
        }
        packet = null;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List list) throws Exception {
        if (!byteBuf.isReadable()) return;
        try {
            final PacketBuffer packetBuffer = new PacketBuffer(byteBuf);

            final int packetId = packetBuffer.readVarIntFromBuffer();

            Packet packet = PacketRegistry.getPacket(connectionState, packetDirection, packetId);

            if (packet == null) {
                packet = new CustomPacket();
                final byte[] data = new byte[packetBuffer.readableBytes()];
                packetBuffer.readBytes(data);
                packet.setCustom(packetId, data);
            } else {
                packet.read(packetBuffer);
            }

            if (packetBuffer.isReadable()) {
                throw new DecoderException(String.format("Packet (%s) was larger than i expected found %s bytes extra", packet.getClass().getSimpleName(), packetBuffer.readableBytes()));
            }
            list.add(packet);
            byteBuf.clear();
            packet = null;
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
    }
}
