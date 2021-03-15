package net.avarioncode.proxy.system.command.impl;

import net.avarioncode.proxy.AvarionProxy;
import net.avarioncode.proxy.mc.objects.Player;
import net.avarioncode.proxy.system.command.Command;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.io.File;

public class CommandChunks extends Command {
    public CommandChunks() {
        super(",chunks", "Pobieranie chunkow z serwera", "<listen/save> <file>");
    }

    @Override
    public void onExecute(String cmd, Player sender) throws Exception {
        final String[] args = cmd.split(" ");
        if (args[1].equalsIgnoreCase("listen")) {
            sender.sendChatMessage("&7Pobieranie chunkow zostalo %s", sender.isListenChunks() ? "&cwylaczone" : "&awlaczone");
            sender.setListenChunks(!sender.isListenChunks());
        } else if (args[1].equalsIgnoreCase("save")) {

            if (args.length != 3) {
                sender.sendChatMessage("&7Nie wprowadziles nazwy pliku!");
                return;
            }

            if (sender.isListenChunks()) {
                sender.sendChatMessage("&7Musisz wylaczyc pobieranie chunkow by je zapisac!");
                return;
            }

            if (sender.getBytes().size() < 1) {
                sender.sendChatMessage("&7Nie masz zadnych pobranych chunkow!");
                return;
            }
            final NBTTagCompound tag = new NBTTagCompound();
            tag.setInteger("Id", 0x26);
            final NBTTagList listTag = new NBTTagList();
            final NBTTagCompound chunks = new NBTTagCompound();
            for (int i = 0; i < sender.getBytes().size(); i++) {
                chunks.setByteArray(String.valueOf(i), sender.getBytes().get(i));
            }
            listTag.appendTag(chunks);
            tag.setTag("Chunks", listTag);
            CompressedStreamTools.write(tag, new File(AvarionProxy.class.getSimpleName() + "/world/" + args[2] + ".dat"));
            sender.getBytes().clear();
            sender.sendChatMessage("&7Pobrane chunki zostaly zapisane w pliku &f" + args[2] + ".dat");
        }
    }
}
