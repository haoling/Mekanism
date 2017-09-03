package mekanism.common.content.transporter;

import java.util.HashMap;
import java.util.Map;

import mekanism.common.content.transporter.Finder.FirstFinder;
import mekanism.common.util.InventoryUtils;
import mekanism.common.util.StackUtils;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.IItemHandler;

public class TransitRequest
{
	public Map<ItemStack, Integer> itemMap = new HashMap<>();
	
	public boolean isEmpty()
	{
		return itemMap.isEmpty();
	}
	
	public void setItem(ItemStack stack, int slot)
	{
		itemMap.put(stack, slot);
	}
	
	public ItemStack getSingleStack()
	{
		return itemMap.keySet().iterator().next();
	}
	
	public boolean hasType(ItemStack stack)
	{
		for(ItemStack s : itemMap.keySet())
		{
			if(InventoryUtils.areItemsStackable(stack, s))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public static TransitRequest getFromTransport(TransporterStack stack)
	{
		return getFromStack(stack.itemStack);
	}
	
	public static TransitRequest getFromStack(ItemStack stack)
	{
		TransitRequest ret = new TransitRequest();
		ret.setItem(stack, -1);
		return ret;
	}
	
	public static TransitRequest getTopStacks(TileEntity tile, EnumFacing side, int amount)
	{
		return getTopStacks(tile, side, amount, new FirstFinder());
	}
	
	public static TransitRequest getTopStacks(TileEntity tile, EnumFacing side, int amount, Finder finder)
	{
		TransitRequest ret = new TransitRequest();

  		if(tile instanceof ISidedInventory)
		{
			ISidedInventory sidedInventory = (ISidedInventory)tile;
			int[] slots = sidedInventory.getSlotsForFace(side.getOpposite());

			if(slots != null)
			{
				for(int get = slots.length - 1; get >= 0; get--)
				{
					int slotID = slots[get];

					ItemStack invStack = sidedInventory.getStackInSlot(slotID);

					if(invStack != null && invStack.stackSize > 0)
					{
						ItemStack toSend = invStack.copy();
						toSend.stackSize = Math.min(amount, toSend.stackSize);

						if(!ret.hasType(toSend) && sidedInventory.canExtractItem(slotID, toSend, side.getOpposite()) && finder.modifies(toSend))
						{
							ret.setItem(toSend, slotID);
						}
					}
				}
			}
		}
		else if(tile instanceof IInventory)
		{
			IInventory inventory = InventoryUtils.checkChestInv((IInventory)tile);
			
			for(int i = inventory.getSizeInventory() - 1; i >= 0; i--)
			{
				if(inventory.getStackInSlot(i)!= null && inventory.getStackInSlot(i).stackSize > 0)
				{
					ItemStack toSend = inventory.getStackInSlot(i).copy();
					toSend.stackSize = (Math.min(amount, toSend.stackSize));

					if(!ret.hasType(toSend) && finder.modifies(toSend))
					{
						ret.setItem(toSend, i);
					}
				}
			}
		}
		else if(InventoryUtils.isItemHandler(tile, side.getOpposite()))
  		{
  			IItemHandler inventory = InventoryUtils.getItemHandler(tile, side.getOpposite());
  			
  			for(int i = inventory.getSlots() - 1; i >= 0; i--)
			{
				ItemStack stack = inventory.extractItem(i, amount, true);
				
				if(stack != null && !ret.hasType(stack) && finder.modifies(stack))
				{
					ret.setItem(stack, i);
				}
			}
  		}

		return ret;
	}
	
	public static class TransitResponse
	{
		public static final TransitResponse EMPTY = new TransitResponse(-1, null);
		
		public int slotID;
		public ItemStack stack = null;
		
		public TransitResponse(int s, ItemStack i)
		{
			slotID = s;
			stack = i;
		}
		
		public boolean isEmpty()
		{
			return stack == null;
		}
		
		public ItemStack getRejected(ItemStack orig)
		{
			return StackUtils.size(orig, orig.stackSize - (stack != null ? stack.stackSize : 0));
		}
		
		public InvStack getInvStack(TileEntity tile, EnumFacing side)
		{
			return new InvStack(tile, slotID, stack, side);
		}
	}
}
