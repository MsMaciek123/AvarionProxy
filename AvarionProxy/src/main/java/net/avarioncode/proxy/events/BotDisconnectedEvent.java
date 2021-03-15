package net.avarioncode.proxy.events;

import com.darkmagician6.eventapi.events.Event;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.avarioncode.proxy.mc.objects.Bot;

@AllArgsConstructor
@Data
public class BotDisconnectedEvent implements Event {

    private Bot bot;

}
