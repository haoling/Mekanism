package mekanism.common.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import mekanism.api.Coord4D;
import mekanism.common.Mekanism;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public final class MinerUtils
{
	public static final String[] GET_SILK_TOUCH_DROP = { "getSilkTouchDrop", "func_180643_i" };
	public static List<Block> specialSilkIDs = ListUtils.asList(Blocks.ICE);

	private static Method getSilkTouchDrop = null;
	static {
		try
		{
			getSilkTouchDrop = ReflectionHelper.findMethod(Block.class, null, GET_SILK_TOUCH_DROP, IBlockState.class);
		} catch (ReflectionHelper.UnableToFindMethodException e){
			Mekanism.logger.error("Unable to find method Block.getSilkTouchDrop");
		}
	}

	public static List<ItemStack> getDrops(World world, Coord4D obj, boolean silk)
	{
		IBlockState state = obj.getBlockState(world);
		Block block = state.getBlock();

		if(block == null || block.isAir(state, world, obj.getPos()))
		{
			return new ArrayList<>();
		}

		if(silk && block.canSilkHarvest(world, obj.getPos(), state, null))
		{
			List<ItemStack> ret = new ArrayList<>();
			if (getSilkTouchDrop != null)
			{
				try
				{
					Object it = getSilkTouchDrop.invoke(block, state);
					if (it != null && it instanceof ItemStack && ((ItemStack)it).stackSize > 0)
					{
						ret.add((ItemStack)it);
					}
				} catch (InvocationTargetException|IllegalAccessException e){
					Mekanism.logger.error("Block.getSilkTouchDrop errored", e);
				}
			}
			else//fallback to old method
			{
				Item item = Item.getItemFromBlock(block);
				if (item != null)
				{
					int meta = item.getHasSubtypes() ? block.getMetaFromState(state) : 0;
					ret.add(new ItemStack(item, 1, meta));
				}
			}

			if (ret.size() > 0)
			{
				List<ItemStack> blockDrops = block.getDrops(world, obj.getPos(), state, 0);

				if(specialSilkIDs.contains(block) || (blockDrops != null && blockDrops.size() > 0))
				{
					net.minecraftforge.event.ForgeEventFactory.fireBlockHarvesting(ret, world, obj.getPos(), state, 0, 1.0f, true, null);
					return ret;
				}
			}
		} else {
			List<ItemStack> blockDrops = block.getDrops(world, obj.getPos(), state, 0);
			if (blockDrops.size() > 0)
			{
				net.minecraftforge.event.ForgeEventFactory.fireBlockHarvesting(blockDrops, world, obj.getPos(), state, 0, 1.0f, true, null);
			}
			return blockDrops;
		}

		return new ArrayList<>();
	}
}
