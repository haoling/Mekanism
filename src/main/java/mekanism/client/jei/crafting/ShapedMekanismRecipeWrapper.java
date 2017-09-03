package mekanism.client.jei.crafting;

import java.util.Arrays;
import java.util.List;

import mekanism.common.recipe.ShapedMekanismRecipe;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import mezz.jei.api.recipe.IStackHelper;
import mezz.jei.api.recipe.wrapper.IShapedCraftingRecipeWrapper;
import net.minecraft.item.ItemStack;

public class ShapedMekanismRecipeWrapper extends BlankRecipeWrapper implements IShapedCraftingRecipeWrapper
{
	private final IJeiHelpers jeiHelpers;
	private final ShapedMekanismRecipe recipe;
	
	public ShapedMekanismRecipeWrapper(IJeiHelpers helpers, ShapedMekanismRecipe r) 
	{
		jeiHelpers = helpers;
		recipe = r;
		
		for(Object input : recipe.getInput()) 
		{
			if(input instanceof ItemStack) 
			{
				ItemStack itemStack = (ItemStack)input;
				
				if(itemStack.stackSize != 1)
				{
					itemStack.stackSize = 1;
				}
			}
		}
	}
	
	@Override
	public void getIngredients(IIngredients ingredients) 
	{
		IStackHelper stackHelper = jeiHelpers.getStackHelper();
		ItemStack recipeOutput = recipe.getRecipeOutput();

		List<List<ItemStack>> inputs = stackHelper.expandRecipeItemStackInputs(Arrays.asList(recipe.getInput()));
		ingredients.setInputLists(ItemStack.class, inputs);
		ingredients.setOutput(ItemStack.class, recipeOutput);
	}

	@Override
	public int getWidth() 
	{
		return recipe.width;
	}

	@Override
	public int getHeight()
	{
		return recipe.height;
	}
}
