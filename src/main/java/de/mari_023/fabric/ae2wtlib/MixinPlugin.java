package de.mari_023.fabric.ae2wtlib;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class MixinPlugin implements IMixinConfigPlugin {

    @Override
    public void onLoad(String mixinPackage) {}

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if(targetClassName.equals("de.mari_023.fabric.ae2wtlib.terminal.ItemWT") && mixinClassName.equals("de.mari_023.fabric.ae2wtlib.mixin.TrinketWT")) {
            return Config.allowTrinket();
        }
        if(targetClassName.equals("de.mari_023.fabric.ae2wtlib.wct.WCTContainer") && mixinClassName.equals("de.mari_023.fabric.ae2wtlib.mixin.TrinketWCTContainer")) {
            return Config.allowTrinket();
        }
        if(mixinClassName.equals("de.mari_023.fabric.ae2wtlib.mixin.HandledScreenMixin")) {
            return Config.allowTrinket();
        }
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
}