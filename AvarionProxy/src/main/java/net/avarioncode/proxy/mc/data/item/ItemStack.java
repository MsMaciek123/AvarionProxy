package net.avarioncode.proxy.mc.data.item;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

public class ItemStack {
    private final int id;
    private final int amount;
    private NBTTagCompound stackTagCompound;
    private int data;
    private int itemDamage;
    private NBTTagCompound nbt;

    public ItemStack(final int id) {
        this.id = id;
        this.amount = 1;
    }

    public ItemStack(final int id, final int amount) {
        this.id = id;
        this.amount = amount;
        this.data = 0;
    }

    public ItemStack(final int id, final int amount, final int data) {
        this.id = id;
        this.amount = amount;
        this.data = data;
    }

    public ItemStack(final int id, final int amount, final int data, final NBTTagCompound nbt) {
        this.id = id;
        this.amount = amount;
        this.data = data;
        this.nbt = nbt;
    }

    public void setItemDamage(int meta) {
        this.itemDamage = meta;
        if (this.itemDamage < 0)
            this.itemDamage = 0;
    }

    public boolean hasTagCompound() {
        return (this.stackTagCompound != null);
    }

    public NBTTagCompound getTagCompound() {
        return this.stackTagCompound;
    }

    public void setTagCompound(NBTTagCompound nbt) {
        this.stackTagCompound = nbt;
    }

    public void setTagInfo(String key, NBTBase value) {
        if (this.stackTagCompound == null)
            setTagCompound(new NBTTagCompound());
        this.stackTagCompound.setTag(key, value);
    }

    public int getId() {
        return this.id;
    }

    public int getAmount() {
        return this.amount;
    }

    public int getData() {
        return this.data;
    }

    public NBTTagCompound getNBT() {
        return this.nbt;
    }

}
