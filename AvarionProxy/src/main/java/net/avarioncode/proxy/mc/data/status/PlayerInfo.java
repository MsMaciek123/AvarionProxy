package net.avarioncode.proxy.mc.data.status;

import lombok.Data;
import net.avarioncode.proxy.mc.objects.GameProfile;

@Data
public class PlayerInfo {
    private int onlinePlayers, maxPlayers;
    private GameProfile[] players;

    public PlayerInfo(final int onlinePlayers, final int maxPlayers, final GameProfile... players) {
        this.onlinePlayers = onlinePlayers;
        this.maxPlayers = maxPlayers;
        this.players = players;
    }
}
