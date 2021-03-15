package net.avarioncode.proxy.utils;

import net.avarioncode.proxy.mc.data.scoreboard.ObjectiveMode;
import net.avarioncode.proxy.mc.data.scoreboard.ObjectiveType;
import net.avarioncode.proxy.mc.objects.Player;
import net.avarioncode.proxy.mc.packet.impl.server.play.ServerDisplayScoreboardPacket;
import net.avarioncode.proxy.mc.packet.impl.server.play.ServerScoreboardObjectivePacket;
import net.avarioncode.proxy.mc.packet.impl.server.play.ServerUpdateScorePacket;

import java.text.DecimalFormat;

public final class ScoreboardUtil {
    private static final DecimalFormat format = new DecimalFormat("##.##");

    public static void sendScoreboard(Player player) {
        String sidebarName = format.format(System.currentTimeMillis());
        player.sendPacket(new ServerScoreboardObjectivePacket(sidebarName, ObjectiveMode.CREATE, LogUtil.fixColor("&dAvarionProxy"), ObjectiveType.INTEGER));
        player.sendPacket(new ServerUpdateScorePacket(LogUtil.fixColor(""), 0, sidebarName, -1));
        player.sendPacket(new ServerUpdateScorePacket(LogUtil.fixColor(""), 0, sidebarName, -2));
        player.sendPacket(new ServerUpdateScorePacket(LogUtil.fixColor(" "), 0, sidebarName, -3));
        player.sendPacket(new ServerDisplayScoreboardPacket(1, sidebarName));
    }

    public static void sendEmptyScoreboard(Player player) {
        String sidebarName = format.format(System.currentTimeMillis());
        player.sendPacket(new ServerScoreboardObjectivePacket(sidebarName, ObjectiveMode.CREATE, LogUtil.fixColor("&dAvarionProxy"), ObjectiveType.INTEGER));
        player.sendPacket(new ServerDisplayScoreboardPacket(1, "emptySidebar"));
        player.sendPacket(new ServerScoreboardObjectivePacket("emptySidebar", ObjectiveMode.CREATE, LogUtil.fixColor("&dAvarionProxy"), ObjectiveType.HEARTS));
        player.sendPacket(new ServerDisplayScoreboardPacket(2, "emptySidebar"));
    }

    public static void updateScoreboard(Player player) {
        sendEmptyScoreboard(player);
        sendScoreboard(player);
    }
}
