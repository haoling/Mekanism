package mekanism.tools.item;

import java.util.List;

import mekanism.common.Mekanism;
import mekanism.common.util.LangUtils;
import mekanism.common.util.StackUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

public class ItemMekanismSword extends ItemSword
{
	private ToolMaterial toolMaterial;
	
	public ItemMekanismSword(ToolMaterial enumtoolmaterial)
	{
		super(enumtoolmaterial);
		toolMaterial = enumtoolmaterial;
		setCreativeTab(Mekanism.tabMekanism);
	}

	@Override
	public void addInformation(ItemStack itemstack, EntityPlayer entityplayer, List list, boolean flag)
	{
		list.add(LangUtils.localize("tooltip.hp") + ": " + (itemstack.getMaxDamage() - itemstack.getItemDamage()));
	}
	
	@Override
    public boolean getIsRepairable(ItemStack stack1, ItemStack stack2)
    {
        return StackUtils.equalsWildcard(ItemMekanismTool.getRepairStack(toolMaterial), stack2) ? true : super.getIsRepairable(stack1, stack2);
    }
}
