package net.avarioncode.proxy.utils;

import lombok.SneakyThrows;
import net.avarioncode.proxy.AvarionProxy;
import net.avarioncode.proxy.mc.data.Position;
import net.avarioncode.proxy.mc.data.game.Difficulty;
import net.avarioncode.proxy.mc.data.game.Dimension;
import net.avarioncode.proxy.mc.data.game.Gamemode;
import net.avarioncode.proxy.mc.objects.Player;
import net.avarioncode.proxy.mc.packet.Packet;
import net.avarioncode.proxy.mc.packet.impl.CustomPacket;
import net.avarioncode.proxy.mc.packet.impl.server.play.*;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;

import java.io.File;

public class WorldUtils {

    public static void emptyWorld(Player player) {
        dimSwitch(player, new ServerJoinGamePacket(0, Gamemode.SURVIVAL, Dimension.OVERWORLD, Difficulty.PEACEFULL, 1, "default_1_1", false));
        player.sendPacket(new ServerSpawnPositionPacket(new Position(0, 0, 0)));
        player.sendPacket(new ServerPlayerAbilitiesPacket(false, true, false, false, 2, 2));
        player.sendPacket(new ServerPlayerPosLookPacket(new Position(0, 0, 0), 180, 90, true));
    }

    public static void dimSwitch(Player player, ServerJoinGamePacket packet) {
        player.sendPacket(new ServerRespawnPacket(Dimension.END, Difficulty.PEACEFULL, Gamemode.ADVENTURE, "default_1_1"));
        player.sendPacket(packet);
        player.sendPacket(new ServerRespawnPacket(packet.getDimension(), packet.getDifficulty(), packet.getGamemode(), packet.getLevelType()));
    }

    @SneakyThrows
    public static void lobby(Player player, String fileName) {
        final NBTTagCompound tag = CompressedStreamTools.read(new File(AvarionProxy.class.getSimpleName(), "world/" + fileName));
        assert tag != null;
        final NBTTagCompound chunks = tag.getTagList("Chunks", 10).getCompoundTagAt(0);
        for (int i = 0; i < chunks.getKeySet().size(); i++) {
            final Packet packet = new CustomPacket();
            final byte[] data = chunks.getByteArray(String.valueOf(i));
            final int id = tag.getInteger("Id");
            packet.setCustom(id, data);
            player.sendPacket(packet);
        }
        player.sendPacket(new ServerPlayerAbilitiesPacket(false, false, true, false, 0.1f, 0.1f));
        player.sendPacket(new ServerPlayerPosLookPacket(new Position(783, 107, -1525.5), -180, 0, true));
        player.sendPacket(new ServerSpawnPositionPacket(new Position(783, 107, -1525.5)));
    }
}