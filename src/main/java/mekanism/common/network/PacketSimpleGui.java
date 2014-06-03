package mekanism.common.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import mekanism.api.Coord4D;
import mekanism.common.Mekanism;
import mekanism.common.tile.TileEntityBasicBlock;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PacketSimpleGui extends MekanismPacket
{
	public Coord4D coord4D;

	public int guiId;
	
	public PacketSimpleGui() {}

	public PacketSimpleGui(Coord4D coord, int gui)
	{
		coord4D = coord;
		guiId = gui;
	}

	public static void openServerGui(int id, EntityPlayerMP playerMP, World world, Coord4D obj)
	{
		playerMP.closeContainer();
		playerMP.getNextWindowId();

		int window = playerMP.currentWindowId;

		Mekanism.packetPipeline.sendTo(new PacketSimpleGui(obj, id), playerMP);

		playerMP.openContainer = Mekanism.proxy.getServerGui(id, playerMP, world, obj.xCoord, obj.yCoord, obj.zCoord);
		playerMP.openContainer.windowId = window;
		playerMP.openContainer.addCraftingToCrafters(playerMP);
	}

	@SideOnly(Side.CLIENT)
	public GuiScreen getGui(int id, EntityPlayer player, World world, Coord4D obj)
	{
		return (GuiScreen)Mekanism.proxy.getClientGui(id, player, world, obj.xCoord, obj.yCoord, obj.zCoord);
	}

	@Override
	public void write(ChannelHandlerContext ctx, ByteBuf dataStream)
	{
		dataStream.writeInt(coord4D.xCoord);
		dataStream.writeInt(coord4D.yCoord);
		dataStream.writeInt(coord4D.zCoord);

		dataStream.writeInt(coord4D.dimensionId);

		dataStream.writeInt(guiId);
	}

	@Override
	public void read(ChannelHandlerContext ctx, EntityPlayer player, ByteBuf dataStream)
	{
		coord4D = new Coord4D(dataStream.readInt(), dataStream.readInt(), dataStream.readInt(), dataStream.readInt());

		guiId = dataStream.readInt();

		if(!player.worldObj.isRemote)
		{
			World worldServer = FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(coord4D.dimensionId);

			if(worldServer != null && coord4D.getTileEntity(worldServer) instanceof TileEntityBasicBlock)
			{
				if(guiId == -1)
				{
					return;
				}

				openServerGui(guiId, (EntityPlayerMP)player, player.worldObj, coord4D);
			}
		}
		else {
			FMLCommonHandler.instance().showGuiScreen(getGui(guiId, player, player.worldObj, coord4D));
		}
	}

	@Override
	public void handleClientSide(EntityPlayer player)
	{

	}

	@Override
	public void handleServerSide(EntityPlayer player)
	{

	}
}
