package net.avarioncode.proxy.mc.threads;

import net.avarioncode.proxy.AvarionProxy;

import java.util.TimerTask;

public class MessageThread extends TimerTask {
    public void run() {
        AvarionProxy.getServer().getPlayerList().forEach(p -> p.sendChatMessage("&7Znajdujesz sie na &fAvarionProxy&7!"));
    }
}
