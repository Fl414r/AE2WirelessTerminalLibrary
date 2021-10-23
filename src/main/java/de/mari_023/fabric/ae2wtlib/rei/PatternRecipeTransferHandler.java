package de.mari_023.fabric.ae2wtlib.rei;

import de.mari_023.fabric.ae2wtlib.wpt.WPTContainer;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.plugin.common.BuiltinPlugin;
import net.minecraft.text.TranslatableText;

public class PatternRecipeTransferHandler extends RecipeTransferHandler<WPTContainer> {

    PatternRecipeTransferHandler() {
        super(WPTContainer.class);
    }


    @Override
    protected Result doTransferRecipe(WPTContainer container, Display recipe) {
        if(container.isCraftingMode() && recipe.getCategoryIdentifier() != BuiltinPlugin.CRAFTING) {
            return Result.createFailed(new TranslatableText("jei.appliedenergistics2.requires_processing_mode"));
        }
        if(recipe.getOutputEntries().isEmpty()) {
            return Result.createFailed(new TranslatableText("jei.appliedenergistics2.no_output"));
        }
        return null;
    }
}