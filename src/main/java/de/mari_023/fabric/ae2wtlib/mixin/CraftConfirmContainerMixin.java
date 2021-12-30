package de.mari_023.fabric.ae2wtlib.mixin;

import appeng.api.networking.IGrid;
import appeng.api.networking.crafting.ICraftingCPU;
import appeng.api.networking.crafting.ICraftingLink;
import appeng.api.networking.crafting.ICraftingPlan;
import appeng.api.networking.security.IActionHost;
import appeng.api.networking.security.IActionSource;
import appeng.menu.AEBaseMenu;
import appeng.menu.MenuOpener;
import appeng.menu.me.crafting.CraftConfirmMenu;
import de.mari_023.fabric.ae2wtlib.wct.WCTContainer;
import de.mari_023.fabric.ae2wtlib.wct.WCTGuiObject;
import de.mari_023.fabric.ae2wtlib.wpt.WPTContainer;
import de.mari_023.fabric.ae2wtlib.wpt.WPTGuiObject;
import net.minecraft.screen.ScreenHandlerType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = CraftConfirmMenu.class, remap = false)
public abstract class CraftConfirmContainerMixin {

    @Shadow
    private ICraftingCPU selectedCpu;
    @Shadow
    private ICraftingPlan result;

    @Shadow
    protected abstract IGrid getGrid();

    @Shadow
    protected abstract IActionSource getActionSrc();

    @Shadow
    public abstract void setAutoStart(boolean autoStart);

    @Inject(method = "startJob", at = @At(value = "HEAD"), cancellable = true)
    public void serverPacketData(CallbackInfo ci) {
        ScreenHandlerType<?> originalGui;
        IActionHost ah = ((AEBaseContainerMixin) this).invokeGetActionHost();
        if(ah instanceof WCTGuiObject) originalGui = WCTContainer.TYPE;
        else if(ah instanceof WPTGuiObject) originalGui = WPTContainer.TYPE;
        else return;

        if(result == null || result.simulation()) return;

        ICraftingLink g = getGrid().getCraftingService().submitJob(result, null, selectedCpu, true, getActionSrc());
        setAutoStart(false);
        if(g != null && ((AEBaseMenu) (Object) this).getLocator() != null)
            MenuOpener.open(originalGui, ((AEBaseMenu) (Object) this).getPlayerInventory().player, ((AEBaseMenu) (Object) this).getLocator());
        ci.cancel();
    }
}
