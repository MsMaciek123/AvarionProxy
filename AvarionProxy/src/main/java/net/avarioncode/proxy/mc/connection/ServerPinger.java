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
import net.avarioncode.proxy.mc.data.status.ServerStatusInfo;
import net.avarioncode.proxy.mc.netty.NettyPacketCodec;
import net.avarioncode.proxy.mc.netty.NettyVarInt21FrameCodec;
import net.avarioncode.proxy.mc.objects.Player;
import net.avarioncode.proxy.mc.packet.Packet;
import net.avarioncode.proxy.mc.packet.impl.client.status.ClientStatusRequestPacket;
import net.avarioncode.proxy.mc.packet.impl.handshake.HandshakePacket;
import net.avarioncode.proxy.mc.packet.impl.server.status.ServerStatusPongPacket;
import net.avarioncode.proxy.mc.packet.impl.server.status.ServerStatusResponsePacket;
import net.avarioncode.proxy.utils.LazyLoadBase;

import java.net.Proxy;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Data
public class ServerPinger implements IConnector {

    private final Player owner;
    private final boolean showResult;
    private final IConnector otherConnection;
    private final LazyLoadBase<NioEventLoopGroup> CLIENT_NIO_EVENT_LOOP_PING = new LazyLoadBase<NioEventLoopGroup>() {
        @Override
        protected NioEventLoopGroup load() {
            return new NioEventLoopGroup(0, new ThreadFactoryBuilder().setNameFormat("Netty Pinger IO #%d").setDaemon(true).build());
        }
    };
    private Channel channel;

    @Override
    public void connect(String host, int port, Proxy proxy) {
        final Bootstrap bootstrap = new Bootstrap()
                .group(CLIENT_NIO_EVENT_LOOP_PING.getValue())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        final ChannelPipeline pipeline = socketChannel.pipeline();
                        if (proxy != Proxy.NO_PROXY) {
                            pipeline.addFirst(new Socks4ProxyHandler(proxy.address()));
                        }
                        pipeline.addLast("timer", new ReadTimeoutHandler(20));
                        pipeline.addLast("frameCodec", new NettyVarInt21FrameCodec());
                        pipeline.addLast("packetCodec", new NettyPacketCodec(EnumConnectionState.STATUS, EnumPacketDirection.CLIENTBOUND));
                        pipeline.addLast("handler", new SimpleChannelInboundHandler<Packet>() {
                            @Override
                            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                if (showResult) {
                                    owner.sendChatMessage("&7Trwa pingowanie serwera...");
                                }
                                TimeUnit.MILLISECONDS.sleep(150);
                                sendPacket(new HandshakePacket(47, host, port, 1));
                                sendPacket(new ClientStatusRequestPacket());
                            }

                            @Override
                            protected void channelRead0(ChannelHandlerContext channelHandlerContext, Packet packet) throws Exception {
                                if (packet instanceof ServerStatusResponsePacket) {
                                    if (showResult) {
                                        final ServerStatusInfo info = ((ServerStatusResponsePacket) packet).getStatusInfo();
                                        owner.sendChatMessage("&7Online&8: &f%s&8/&f%s", info.getPlayers().getOnlinePlayers(), info.getPlayers().getMaxPlayers());
                                        owner.sendChatMessage("&7Motd&8: &f" + info.getDescription().getFullText());
                                        owner.sendChatMessage("&7Version&8: &f%s &8(&f%s&8)", info.getVersion().getProtocol(), info.getVersion().getName());
                                    }
                                    channel.close();
                                    if (otherConnection != null) {
                                        otherConnection.connect(host, port, proxy);
                                    }
                                } else if (packet instanceof ServerStatusPongPacket) {
                                    channel.close();
                                }
                            }
                        });
                    }
                });
        this.channel = bootstrap.connect(host, port).syncUninterruptibly().channel();
        this.channel.config().setOption(ChannelOption.TCP_NODELAY, true);
        this.channel.config().setOption(ChannelOption.IP_TOS, 0x18);
    }

    public void sendPacket(final Packet packet) {
        this.channel.writeAndFlush(packet).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
    }
}
