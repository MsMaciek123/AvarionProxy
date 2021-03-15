package net.avarioncode.proxy.system;

import net.avarioncode.proxy.utils.LogUtil;

import java.util.Scanner;

public class LowLevelCommandTask implements Runnable {
    @Override
    public void run() {
        final Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            final String input = scanner.nextLine();
            if (input.equalsIgnoreCase("exit") || input.equalsIgnoreCase("stop")) {
                LogUtil.printMessage("Zatrzymywanie proxy...");
                System.exit(0);
            }
        }
    }
}
