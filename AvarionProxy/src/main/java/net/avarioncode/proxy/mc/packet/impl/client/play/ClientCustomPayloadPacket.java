package net.avarioncode.proxy.mc.packet.impl.client.play;

import net.avarioncode.proxy.mc.packet.Packet;
import net.avarioncode.proxy.mc.packet.PacketBuffer;

import java.util.Arrays;
import java.util.Objects;

public class ClientCustomPayloadPacket extends Packet {
    private String channel;

    private byte[] data;

    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof ClientCustomPayloadPacket))
            return false;
        ClientCustomPayloadPacket other = (ClientCustomPayloadPacket) o;
        if (!other.canEqual(this))
            return false;
        Object this$channel = getChannel(), other$channel = other.getChannel();
        return (Objects.equals(this$channel, other$channel)) && (!!Arrays.equals(getData(), other.getData()));
    }

    protected boolean canEqual(Object other) {
        return other instanceof ClientCustomPayloadPacket;
    }

    public String toString() {
        return "ClientCustomPayloadPacket(channel=" + getChannel() + ", data=" + Arrays.toString(getData()) + ")";
    }

    public String getChannel() {
        return this.channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public byte[] getData() {
        return this.data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public void write(PacketBuffer out) throws Exception {
        out.writeString(this.channel);
        out.writeBytes(this.data);
    }

    public void read(PacketBuffer in) throws Exception {
        this.channel = in.readStringFromBuffer(16);
        this.data = new byte[in.readableBytes()];
        in.readBytes(this.data);
    }
}
