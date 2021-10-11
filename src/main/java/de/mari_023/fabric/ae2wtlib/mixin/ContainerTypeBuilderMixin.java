package de.mari_023.fabric.ae2wtlib.mixin;

import appeng.api.features.IWirelessTermHandler;
import appeng.container.ContainerLocator;
import appeng.container.implementations.ContainerTypeBuilder;
import de.mari_023.fabric.ae2wtlib.util.InventoryTerminalHelper;
import de.mari_023.fabric.ae2wtlib.wut.WUTHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ContainerTypeBuilder.class, remap = false)
public class ContainerTypeBuilderMixin<I> {

    @Shadow
    @Final
    private Class<I> hostInterface;

    @Inject(method = "getHostFromPlayerInventory", at = @At(value = "HEAD"), cancellable = true)
    public void serverPacketData(PlayerEntity player, ContainerLocator locator, CallbackInfoReturnable<I> cir) {
        ItemStack it = InventoryTerminalHelper.getTerminal(player, locator.getItemIndex());

        if(it.isEmpty()) return;

        String currentTerminal = WUTHandler.getCurrentTerminal(it);
        if(WUTHandler.terminalNames.contains(currentTerminal))
            cir.setReturnValue(hostInterface.cast(WUTHandler.wirelessTerminals.get(currentTerminal).wtguiObjectFactory.create((IWirelessTermHandler) it.getItem(), it, player, locator.getItemIndex())));
    }
}
