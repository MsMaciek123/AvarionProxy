package net.avarioncode.proxy.events;

import com.darkmagician6.eventapi.events.Event;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.avarioncode.proxy.mc.objects.Player;

@AllArgsConstructor
@Data
public class PlayerDisconnectedEvent implements Event {

    private Player player;

}
