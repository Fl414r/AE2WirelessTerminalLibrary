package de.mari_023.ae2wtlib.wut;

import java.util.function.BiConsumer;

import org.jetbrains.annotations.Nullable;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

import de.mari_023.ae2wtlib.terminal.IUniversalWirelessTerminalItem;
import de.mari_023.ae2wtlib.terminal.WTMenuHost;

import appeng.menu.ISubMenu;
import appeng.menu.locator.MenuLocator;

public record WTDefinition(ContainerOpener containerOpener, WTMenuHostFactory wTMenuHostFactory, MenuType<?> menuType,
        IUniversalWirelessTerminalItem item, ItemStack universalTerminal, MutableComponent terminalName) {

    @FunctionalInterface
    public interface ContainerOpener {
        boolean tryOpen(Player player, MenuLocator locator, ItemStack stack, boolean returningFromSubmenu);
    }

    @FunctionalInterface
    public interface WTMenuHostFactory {
        WTMenuHost create(final Player ep, @Nullable Integer inventorySlot, final ItemStack is,
                BiConsumer<Player, ISubMenu> returnToMainMenu);
    }
}
