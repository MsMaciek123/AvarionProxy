package net.avarioncode.proxy.mc.connection;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.proxy.Socks4ProxyHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.avarioncode.proxy.mc.data.network.EnumConnectionState;
import net.avarioncode.proxy.mc.data.network.EnumPacketDirection;
import net.avarioncode.proxy.mc.netty.NettyCompressionCodec;
import net.avarioncode.proxy.mc.netty.NettyPacketCodec;
import net.avarioncode.proxy.mc.netty.NettyVarInt21FrameCodec;
import net.avarioncode.proxy.mc.objects.Player;
import net.avarioncode.proxy.mc.packet.Packet;
import net.avarioncode.proxy.mc.packet.impl.client.login.ClientLoginStartPacket;
import net.avarioncode.proxy.mc.packet.impl.client.play.ClientKeepAlivePacket;
import net.avarioncode.proxy.mc.packet.impl.handshake.HandshakePacket;
import net.avarioncode.proxy.mc.packet.impl.server.login.ServerLoginDisconnectPacket;
import net.avarioncode.proxy.mc.packet.impl.server.login.ServerLoginEncryptionRequestPacket;
import net.avarioncode.proxy.mc.packet.impl.server.login.ServerLoginSetCompressionPacket;
import net.avarioncode.proxy.mc.packet.impl.server.login.ServerLoginSuccessPacket;
import net.avarioncode.proxy.mc.packet.impl.server.play.ServerDisconnectPacket;
import net.avarioncode.proxy.mc.packet.impl.server.play.ServerJoinGamePacket;
import net.avarioncode.proxy.mc.packet.impl.server.play.ServerKeepAlivePacket;
import net.avarioncode.proxy.mc.packet.impl.server.play.ServerTimeUpdatePacket;
import net.avarioncode.proxy.utils.LazyLoadBase;
import net.avarioncode.proxy.utils.WaitTimer;
import net.avarioncode.proxy.utils.WorldUtils;

import java.net.Proxy;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Data
public class ServerConnector implements IConnector {

    private final Player owner;
    private final String username;
    private final ArrayList<Long> tpstimes = new ArrayList<>();
    private final WaitTimer tpsTimer = new WaitTimer();
    private final LazyLoadBase<NioEventLoopGroup> CLIENT_NIO_EVENT_LOOP_PING = new LazyLoadBase<NioEventLoopGroup>() {
        @Override
        protected NioEventLoopGroup load() {
            return new NioEventLoopGroup(0, new ThreadFactoryBuilder().setNameFormat("Netty Client IO #%d").setDaemon(true).build());
        }
    };
    private Channel channel;
    private EnumConnectionState connectionState = EnumConnectionState.LOGIN;
    private boolean connected;
    private double lastTps;
    private long lastPacketTime = 0L;

    @Override
    public void connect(String host, int port, Proxy proxy) {
        final Bootstrap bootstrap = new Bootstrap()
                .group(CLIENT_NIO_EVENT_LOOP_PING.getValue())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) {
                        final ChannelPipeline pipeline = socketChannel.pipeline();
                        if (proxy != Proxy.NO_PROXY) {
                            pipeline.addFirst(new Socks4ProxyHandler(proxy.address()));
                        }
                        pipeline.addLast("timer", new ReadTimeoutHandler(20));
                        pipeline.addLast("frameCodec", new NettyVarInt21FrameCodec());
                        pipeline.addLast("packetCodec", new NettyPacketCodec(EnumConnectionState.LOGIN, EnumPacketDirection.CLIENTBOUND));
                        pipeline.addLast("handler", new SimpleChannelInboundHandler<Packet>() {
                            @Override
                            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                owner.sendChatMessage("&7Laczenie z serwerem...");
                                TimeUnit.MILLISECONDS.sleep(150);
                                sendPacket(new HandshakePacket(47, host, port, 2));
                                sendPacket(new ClientLoginStartPacket(username));
                            }

                            @Override
                            public void channelInactive(ChannelHandlerContext ctx) {
                                if (connected) {
                                    owner.sendChatMessage("&7Serwer przerwal polaczenie!");
                                    WorldUtils.emptyWorld(owner);
                                    WorldUtils.lobby(owner, "Lobby.dat");
                                }
                                connected = false;
                            }

                            @Override
                            protected void channelRead0(ChannelHandlerContext channelHandlerContext, Packet packet) {
                                owner.setLastPacket(packet.getClass().getSimpleName());
                                owner.setPacketID(packet.getPacketID());
                                if (packet instanceof ServerLoginSetCompressionPacket) {
                                    setCompressionThreshold(((ServerLoginSetCompressionPacket) packet).getThreshold());
                                } else if (packet instanceof ServerLoginEncryptionRequestPacket) {
                                    owner.sendChatMessage("&7Proxy nie wspiera Minecraft Premium!");
                                    WorldUtils.emptyWorld(owner);
                                    WorldUtils.lobby(owner, "Lobby.dat");
                                    close();
                                    owner.getBots().forEach(bot -> bot.getConnection().close());
                                } else if (packet instanceof ServerLoginSuccessPacket) {
                                    setConnectionState(EnumConnectionState.PLAY);
                                    owner.sendChatMessage("&7Dolaczono do serwera!");
                                } else if (packet instanceof ServerJoinGamePacket) {
                                    WorldUtils.dimSwitch(owner, (ServerJoinGamePacket) packet);
                                    connected = true;
                                    owner.sendChatMessage("&7Uzyskano polaczenie!");
                                    //AvarionProxy.getServer().getPlayerList().forEach(p -> p.sendChatMessage("&f" + owner.getUsername() + "&7 polaczyl sie z  &f" + host));
                                } else if (packet instanceof ServerDisconnectPacket) {
                                    connected = false;
                                    WorldUtils.emptyWorld(owner);
                                    WorldUtils.lobby(owner, "Lobby.dat");
                                    owner.sendChatMessage("&7Rozlaczono od serwera!");
                                    owner.sendChatMessage("&f" + ((ServerDisconnectPacket) packet).getReason().getFullText());
                                    owner.getBots().forEach(bot -> bot.getConnection().close());
                                    //AvarionProxy.getServer().getPlayerList().forEach(p -> p.sendChatMessage("&f" + owner.getUsername() + "&7 odlaczyl sie od &f" + host));
                                } else if (packet instanceof ServerLoginDisconnectPacket) {
                                    connected = false;
                                    WorldUtils.emptyWorld(owner);
                                    WorldUtils.lobby(owner, "Lobby.dat");
                                    owner.sendChatMessage("&7Rozlaczono od serwera!");
                                    owner.sendChatMessage("&f" + ((ServerLoginDisconnectPacket) packet).getReason().getFullText());
                                    owner.getBots().forEach(bot -> bot.getConnection().close());
                                    //AvarionProxy.getServer().getPlayerList().forEach(p -> p.sendChatMessage("&f" + owner.getUsername() + "&7 odlaczyl sie od &f" + host));
                                } else if (packet instanceof ServerKeepAlivePacket) {
                                    sendPacket(new ClientKeepAlivePacket(((ServerKeepAlivePacket) packet).getKeepaliveId()));
                                } else if (connected && connectionState == EnumConnectionState.PLAY) {
                                    if (packet instanceof ServerTimeUpdatePacket) {
                                        tpstimes.add(Math.max(1000, tpsTimer.getTime()));
                                        long timesAdded = 0;
                                        if (tpstimes.size() > 5) {
                                            tpstimes.remove(0);
                                        }
                                        for (long l : tpstimes) {
                                            timesAdded += l;
                                        }
                                        long roundedTps = timesAdded / tpstimes.size();
                                        lastTps = (20.0 / roundedTps) * 1000.0;
                                        tpsTimer.reset();
                                    }
                                    lastPacketTime = System.currentTimeMillis();
                                    owner.sendPacket(packet);
                                }
                                if (owner.isListenChunks()) {
                                    if (packet.getPacketID() == 0x26) {
                                        byte[] data = packet.getCustomData();
                                        owner.getBytes().add(data);
                                    }
                                }
                            }
                        });
                    }
                });
        this.channel = bootstrap.connect(host, port).syncUninterruptibly().channel();
        this.channel.config().setOption(ChannelOption.TCP_NODELAY, true);
        this.channel.config().setOption(ChannelOption.IP_TOS, 0x18);
    }

    public void setConnectionState(final EnumConnectionState state) {
        ((NettyPacketCodec) channel.pipeline().get("packetCodec")).setConnectionState(state);
        connectionState = state;
    }

    public void setCompressionThreshold(final int threshold) {
        if (connectionState == EnumConnectionState.LOGIN) {
            if (channel.pipeline().get("compression") == null) {
                channel.pipeline().addBefore("packetCodec", "compression", new NettyCompressionCodec(threshold));
            } else {
                ((NettyCompressionCodec) channel.pipeline().get("compression")).setCompressionThreshold(threshold);
            }
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public void close() {
        connected = false;
        this.channel.close();
        //AvarionProxy.getServer().getPlayerList().forEach(p -> owner.sendChatMessage("&f" + owner.getUsername() + "&7 odlaczyl sie od &f" + owner.getHost()));
    }

    public void sendPacket(final Packet packet) {
        this.channel.writeAndFlush(packet).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
    }
}
