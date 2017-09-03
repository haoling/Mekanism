package mekanism.client.jei.machine.chemical;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import mekanism.api.gas.GasStack;
import mekanism.common.recipe.machines.OxidationRecipe;
import mekanism.common.util.LangUtils;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class ChemicalOxidizerRecipeWrapper extends BlankRecipeWrapper
{
	public OxidationRecipe recipe;
	
	public ChemicalOxidizerRecipeCategory category;
	
	public ChemicalOxidizerRecipeWrapper(OxidationRecipe r, ChemicalOxidizerRecipeCategory c)
	{
		recipe = r;
		category = c;
	}
	
	@Override
	public void getIngredients(IIngredients ingredients) 
	{
		ingredients.setInput(ItemStack.class, recipe.recipeInput.ingredient);
		ingredients.setOutput(GasStack.class, recipe.recipeOutput.output);
	}
	
	@Nullable
	@Override
	public List<String> getTooltipStrings(int mouseX, int mouseY)
	{
		List<String> currenttip = new ArrayList<String>();
		
		if(mouseX >= 134-20 && mouseX <= 150-20 && mouseY >= 14-12 && mouseY <= 72-12)
		{
			currenttip.add(LangUtils.localizeGasStack(recipe.getOutput().output));
		}
		
		return currenttip;
	}
}
