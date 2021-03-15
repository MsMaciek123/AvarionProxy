package net.avarioncode.proxy.events;

import com.darkmagician6.eventapi.events.Event;
import com.darkmagician6.eventapi.events.callables.EventCancellable;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.avarioncode.proxy.mc.objects.Player;

@AllArgsConstructor
@Data
public class PlayerCommandEvent extends EventCancellable implements Event {

    private String command;
    private Player player;

}
