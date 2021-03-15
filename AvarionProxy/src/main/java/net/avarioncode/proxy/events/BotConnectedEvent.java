package net.avarioncode.proxy.events;

import com.darkmagician6.eventapi.events.Event;
import com.darkmagician6.eventapi.events.callables.EventCancellable;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.avarioncode.proxy.mc.objects.Bot;

@Data
@AllArgsConstructor
public class BotConnectedEvent extends EventCancellable implements Event {

    private Bot bot;

}
