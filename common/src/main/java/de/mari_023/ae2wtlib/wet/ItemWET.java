package de.mari_023.ae2wtlib.wet;

import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

import de.mari_023.ae2wtlib.terminal.ItemWT;

public class ItemWET extends ItemWT {
    @Override
    public MenuType<?> getMenuType(ItemStack stack) {
        return WETMenu.TYPE;
    }
}
