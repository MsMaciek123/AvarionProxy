package net.avarioncode.proxy.mc.objects;

import com.darkmagician6.eventapi.EventManager;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import lombok.Data;
import net.avarioncode.proxy.AvarionProxy;
import net.avarioncode.proxy.events.PacketReceivedEvent;
import net.avarioncode.proxy.mc.connection.ServerConnector;
import net.avarioncode.proxy.mc.data.Position;
import net.avarioncode.proxy.mc.data.chat.MessagePosition;
import net.avarioncode.proxy.mc.data.game.TitleAction;
import net.avarioncode.proxy.mc.data.network.EnumConnectionState;
import net.avarioncode.proxy.mc.netty.NettyCompressionCodec;
import net.avarioncode.proxy.mc.netty.NettyPacketCodec;
import net.avarioncode.proxy.mc.packet.INetHandler;
import net.avarioncode.proxy.mc.packet.Packet;
import net.avarioncode.proxy.mc.packet.impl.client.NetHandlerLoginServer;
import net.avarioncode.proxy.mc.packet.impl.client.NetHandlerStatusServer;
import net.avarioncode.proxy.mc.packet.impl.handshake.HandshakePacket;
import net.avarioncode.proxy.mc.packet.impl.server.login.ServerLoginSetCompressionPacket;
import net.avarioncode.proxy.mc.packet.impl.server.play.ServerChatPacket;
import net.avarioncode.proxy.mc.packet.impl.server.play.ServerTitlePacket;

import java.util.ArrayList;
import java.util.List;

@Data
public class Player {

    public String host;
    private List<String> players = new ArrayList<>();
    private Channel channel;
    private EnumConnectionState connectionState;
    private INetHandler packetHandler;
    private String username;
    private int ping;
    private ServerConnector connector;
    private String lastPacket;
    private boolean debugInfo = true;
    private int packetID;
    private boolean pluginsState;
    private boolean playersState;
    private ServerData serverData;
    private boolean mother;
    private boolean listenChunks;
    private Position position;
    private List<byte[]> bytes = new ArrayList<>();
    private List<Bot> bots = new ArrayList<>();

    public void resetTitle() {
        sendPacket(new ServerTitlePacket(TitleAction.RESET));
    }

    public void sendTitle(final String header, final String footer) {
        this.sendTitle(header, footer, 10, 10, 10);
    }

    public void sendTitle(final String header, final String footer, final int fadeIn, final int stay, final int fadeOut) {
        if (header != null) sendPacket(new ServerTitlePacket(TitleAction.TITLE, header));
        if (footer != null) sendPacket(new ServerTitlePacket(TitleAction.SUBTITLE, footer));
        sendPacket(new ServerTitlePacket(TitleAction.TIMES, fadeIn, stay, fadeOut));
    }

    public void sendHotbar(final String message, final Object... obj) {
        sendPacket(new ServerChatPacket(String.format(message, obj), MessagePosition.HOTBAR));
    }

    public void setPluginsState(boolean pluginsState) {
        this.pluginsState = pluginsState;
    }

    public final void tick() {
        if (this.getConnector() != null && this.getConnector().isConnected()) {
            final int packetTime = (int) (System.currentTimeMillis() - this.getConnector().getLastPacketTime());
            if (packetTime > 2000) {
                sendTitle("&dAvarionProxy", String.format("&7Serwer nie odpowiada od: &f%sms", packetTime), 0, 10, 0);
            }
            if (debugInfo) {
                if (packetTime > 2000) {
                    sendHotbar("&7Ostatni otrzymany pakiet&8: &f%s &8(&7MS: &c&l%s&8)", lastPacket, packetTime);
                } else {
                    sendHotbar("&7Ostatni otrzymany pakiet&8: &f%s &8(&7MS: &f%s&8)", lastPacket, packetTime);
                }
            }
        }
    }

    public void sendChatMessage(final String message, final Object... args) {
        sendPacket(new ServerChatPacket("&dAvarionProxy &8» &7" + String.format(message, args)));
    }

    public void sendChatMessageNoPrefix(final String message, final Object... args) {
        sendPacket(new ServerChatPacket(String.format(message, args)));
    }


    public void packetReceived(final Packet packet) {
        final PacketReceivedEvent event = new PacketReceivedEvent(packet, this);
        EventManager.call(event);
        if (!event.isCancelled()) {
            if (packet instanceof HandshakePacket) {
                final HandshakePacket handshake = (HandshakePacket) packet;
                switch (handshake.getNextState()) {
                    case 1:
                        setConnectionState(EnumConnectionState.STATUS);
                        packetHandler = new NetHandlerStatusServer(this);
                        break;
                    case 2:
                        setConnectionState(EnumConnectionState.LOGIN);
                        packetHandler = new NetHandlerLoginServer(this);
                        break;
                }
                if (connectionState == EnumConnectionState.HANDSHAKE) {
                    channel.close();
                }
            } else {
                packetHandler.handlePacket(packet);
            }
        }
    }

    public void disconnected() {
        if (this.getConnector() != null && this.getConnector().isConnected()) {
            this.getConnector().close();
        }
        AvarionProxy.getServer().playerList.remove(this);
        if (packetHandler != null)
            packetHandler.disconnected();
    }

    public void setConnectionState(final EnumConnectionState state) {
        ((NettyPacketCodec) channel.pipeline().get("packetCodec")).setConnectionState(state);
        connectionState = state;
    }

    public void setCompressionThreshold(final int threshold) {
        if (connectionState == EnumConnectionState.LOGIN) {
            sendPacket(new ServerLoginSetCompressionPacket(threshold));
            if (channel.pipeline().get("compression") == null) {
                channel.pipeline().addBefore("packetCodec", "compression", new NettyCompressionCodec(threshold));
            } else {
                ((NettyCompressionCodec) channel.pipeline().get("compression")).setCompressionThreshold(threshold);
            }
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public void sendPacket(final Packet packet) {
        this.channel.writeAndFlush(packet).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
    }


}
