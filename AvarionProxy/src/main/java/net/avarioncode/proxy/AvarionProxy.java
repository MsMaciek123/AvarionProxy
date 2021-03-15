package net.avarioncode.proxy;

import lombok.Getter;
import net.avarioncode.proxy.api.ProxyAPI;
import net.avarioncode.proxy.mc.data.chat.Message;
import net.avarioncode.proxy.mc.data.status.PlayerInfo;
import net.avarioncode.proxy.mc.data.status.ServerStatusInfo;
import net.avarioncode.proxy.mc.data.status.VersionInfo;
import net.avarioncode.proxy.mc.objects.GameProfile;
import net.avarioncode.proxy.mc.packet.registry.PacketRegistry;
import net.avarioncode.proxy.mc.server.MinecraftServer;
import net.avarioncode.proxy.mc.threads.MessageThread;
import net.avarioncode.proxy.system.LowLevelCommandTask;
import net.avarioncode.proxy.system.command.CommandManager;
import net.avarioncode.proxy.system.crash.CrashRegistry;
import net.avarioncode.proxy.utils.ImageUtil;
import net.avarioncode.proxy.utils.LogUtil;
import net.avarioncode.proxy.utils.ProxyChecker;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.Proxy;
import java.util.List;
import java.util.Timer;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AvarionProxy {

    public static ProxyChecker PLChecker;
    public static ProxyChecker GLChecker;
    public static String version = "0.4-DEV";
    @Getter
    private static MinecraftServer server;

    public void run() throws Exception {
        PacketRegistry.load();
        final Timer timer = new Timer();
        timer.scheduleAtFixedRate(new MessageThread(), 80000L, 80000L);
        LogUtil.printMessage("[AvarionProxy] Proxy uzywa biblioteki z LightProxy!");
        LogUtil.printMessage("[AvarionProxy] Ladowanie socks proxy...");
        final List<Proxy> PL_Proxies = ProxyAPI.getProxies(ProxyAPI.getURL("socks4", "PL"));
        final List<Proxy> GL_Proxies = ProxyAPI.getProxies(ProxyAPI.getURL("socks4", null));
        LogUtil.printMessage("[AvarionProxy] Proxy Status: PL: %s, TOP: %s", PL_Proxies.size(), GL_Proxies.size());
        PLChecker = new ProxyChecker().checkArray(PL_Proxies);
        GLChecker = new ProxyChecker().checkArray(GL_Proxies);
        final ExecutorService commandTask = Executors.newSingleThreadExecutor();
        commandTask.submit(new LowLevelCommandTask());
        server = new MinecraftServer(16301).bind("[AvarionProxy] Serwer zostal uruchomiony: 127.0.0.1:%s");
        CommandManager.init();
        CrashRegistry.init();
        final File statusFile = new File("AvarionProxy/icon/server-icon.png");
        BufferedImage bufferedImage = null;
        if (statusFile.exists()) {
            bufferedImage = ImageIO.read(new File("AvarionProxy/icon/server-icon.png"));
        }
        final VersionInfo versionInfo = new VersionInfo(LogUtil.fixColor("&7Online: &f" + getServer().playerList.size()), 399);
        GameProfile[] profiles = {new GameProfile(LogUtil.fixColor("                      &dAvarionProxy                 \n\n&8>> &7Autor biblioteki: &fdickmeister     \n&8>> &7Wersja proxy: &f" + version + "     \n&8>> &7Proxy socks PL: &f" + PL_Proxies.size() + "     \n&8>> &7Proxy socks TOP: &f" + GL_Proxies.size() + "\n"), UUID.randomUUID())};
        final PlayerInfo playerInfo = new PlayerInfo(0, 0, profiles);
        final Message desc = Message.fromString(LogUtil.fixColor("&dAvarionProxy &8» &7Wersja &f" + version + "\n&dAvarionProxy &8» &7kraking is my paszyn"));
        server.setStatusInfo(new ServerStatusInfo(versionInfo, playerInfo, desc, statusFile.exists() ? ImageUtil.iconToString(bufferedImage) : null));
    }

}
