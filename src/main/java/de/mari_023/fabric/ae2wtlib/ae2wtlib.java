package de.mari_023.fabric.ae2wtlib;

import appeng.container.AEBaseContainer;
import de.mari_023.fabric.ae2wtlib.wct.ItemMagnetCard;
import de.mari_023.fabric.ae2wtlib.wct.ItemWCT;
import de.mari_023.fabric.ae2wtlib.wct.WCTContainer;
import de.mari_023.fabric.ae2wtlib.wit.ItemWIT;
import de.mari_023.fabric.ae2wtlib.wit.WITContainer;
import de.mari_023.fabric.ae2wtlib.wpt.ItemWPT;
import de.mari_023.fabric.ae2wtlib.wpt.WPTContainer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ae2wtlib implements ModInitializer {

    public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.build(new Identifier("ae2wtlib", "general"), () -> new ItemStack(ae2wtlib.CRAFTING_TERMINAL));

    public static final ItemWCT CRAFTING_TERMINAL = new ItemWCT();
    public static final ItemWPT PATTERN_TERMINAL = new ItemWPT();
    public static final ItemWIT INTERFACE_TERMINAL = new ItemWIT();

    public static final ItemInfinityBooster INFINITY_BOOSTER = new ItemInfinityBooster();
    public static final ItemMagnetCard MAGNET_CARD = new ItemMagnetCard();

    @Override
    public void onInitialize() {
        Registry.register(Registry.ITEM, new Identifier("ae2wtlib", "infinity_booster_card"), INFINITY_BOOSTER);
        Registry.register(Registry.ITEM, new Identifier("ae2wtlib", "magnet_card"), MAGNET_CARD);
        Registry.register(Registry.ITEM, new Identifier("ae2wtlib", "wireless_crafting_terminal"), CRAFTING_TERMINAL);
        Registry.register(Registry.ITEM, new Identifier("ae2wtlib", "wireless_pattern_terminal"), PATTERN_TERMINAL);
        Registry.register(Registry.ITEM, new Identifier("ae2wtlib", "wireless_interface_terminal"), INTERFACE_TERMINAL);
        WCTContainer.TYPE = registerScreenHandler("wireless_crafting_terminal", WCTContainer::fromNetwork);
        WPTContainer.TYPE = registerScreenHandler("wireless_pattern_terminal", WPTContainer::fromNetwork);
        WITContainer.TYPE = registerScreenHandler("wireless_interface_terminal", WITContainer::fromNetwork);
        //ItemComponentCallbackV2.event(UNIVERSAL_TERMINAL).register(((item, itemStack, componentContainer) -> componentContainer.put(CuriosComponent.ITEM, new ICurio() {})));

        ServerPlayNetworking.registerGlobalReceiver(new Identifier("ae2wtlib", "general"), (server, player, handler, buf, sender) -> {
            buf.retain();
            server.execute(() -> {
                String Name = buf.readString();
                byte value = buf.readByte();
                final ScreenHandler c = player.currentScreenHandler;
                if(Name.startsWith("PatternTerminal.") && c instanceof WPTContainer) {
                    final WPTContainer cpt = (WPTContainer) c;
                    switch(Name) {
                        case "PatternTerminal.CraftMode":
                            cpt.getPatternTerminal().setCraftingRecipe(value != 0);
                            break;
                        case "PatternTerminal.Encode":
                            cpt.encode();
                            break;
                        case "PatternTerminal.Clear":
                            cpt.clear();
                            break;
                        case "PatternTerminal.Substitute":
                            cpt.getPatternTerminal().setSubstitution(value != 0);
                            break;
                    }
                } else if(Name.startsWith("CraftingTerminal.") && c instanceof WCTContainer) {
                    final WCTContainer cpt = (WCTContainer) c;
                    if(Name.equals("CraftingTerminal.Delete")) {
                        cpt.deleteTrashSlot();
                    }
                }
                buf.release();
            });
        });
        ServerPlayNetworking.registerGlobalReceiver(new Identifier("ae2wtlib", "patternslotpacket"), (server, player, handler, buf, sender) -> {
            buf.retain();
            server.execute(() -> {
                if (player.currentScreenHandler instanceof WPTContainer) {
                    final WPTContainer patternTerminal = (WPTContainer) player.currentScreenHandler;
                    patternTerminal.craftOrGetItem(buf);
                }
                buf.release();
            });
        });
    }

    public static <T extends AEBaseContainer> ScreenHandlerType<T> registerScreenHandler(String Identifier, ScreenHandlerRegistry.ExtendedClientHandlerFactory<T> factory) {
        return ScreenHandlerRegistry.registerExtended(new Identifier("ae2wtlib", Identifier), factory);
    }
}