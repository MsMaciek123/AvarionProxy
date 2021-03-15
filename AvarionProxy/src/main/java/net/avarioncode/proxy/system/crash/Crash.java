package net.avarioncode.proxy.system.crash;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.avarioncode.proxy.mc.objects.Player;

@Data
@AllArgsConstructor
public abstract class Crash {

    private String name;

    public abstract void init();

    public abstract void execute(final String message, final Player sender);

}
