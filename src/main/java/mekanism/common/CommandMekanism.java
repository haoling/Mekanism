package mekanism.common;

import java.util.Arrays;
import java.util.List;

import mekanism.api.EnumColor;
import mekanism.api.MekanismAPI;
import mekanism.common.config.MekanismConfig.general;
import mekanism.common.util.MekanismUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class CommandMekanism extends CommandBase
{
	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender)
	{
		return server.isSinglePlayer() || super.checkPermission(server, sender);
	}

	@Override
	public String getCommandName()
	{
		return "mk";
	}

	@Override
	public String getCommandUsage(ICommandSender sender)
	{
		return "/mk <parameters>";
	}

	@Override
	public List getCommandAliases()
	{
		return Arrays.asList(new String[] {"mekanism", "mek"});
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] params)
	{
		if(params.length < 1)
		{
			sender.addChatMessage(new TextComponentString(EnumColor.GREY + "------------- " + EnumColor.DARK_BLUE + "[Mekanism]" + EnumColor.GREY + " -------------"));
			sender.addChatMessage(new TextComponentString(EnumColor.GREY + " *Version: " + EnumColor.DARK_GREY + Mekanism.versionNumber));
			sender.addChatMessage(new TextComponentString(EnumColor.GREY + " *Latest Version: " + EnumColor.DARK_GREY + Mekanism.latestVersionNumber));
			sender.addChatMessage(new TextComponentString(EnumColor.GREY + " *Developed on Mac OS X 10.8 Mountain Lion"));
			sender.addChatMessage(new TextComponentString(EnumColor.GREY + " *Code, textures, and ideas by aidancbrady"));
			sender.addChatMessage(new TextComponentString(EnumColor.GREY + " *Recent News: " + EnumColor.INDIGO + Mekanism.recentNews));
			sender.addChatMessage(new TextComponentString(EnumColor.GREY + "------------- " + EnumColor.DARK_BLUE + "[=======]" + EnumColor.GREY + " -------------"));
		}
		else if(params.length >= 1)
		{
			if(params[0].equalsIgnoreCase("help"))
			{
				if(params.length == 1)
				{
					sender.addChatMessage(new TextComponentString(EnumColor.GREY + "------------- " + EnumColor.DARK_BLUE + "[Mekanism]" + EnumColor.GREY + " -------------"));
					sender.addChatMessage(new TextComponentString(EnumColor.INDIGO + " /mk" + EnumColor.GREY + " -- displays the main page."));
					sender.addChatMessage(new TextComponentString(EnumColor.INDIGO + " /mk help" + EnumColor.GREY + " -- displays this guide."));
					sender.addChatMessage(new TextComponentString(EnumColor.INDIGO + " /mk version" + EnumColor.GREY + " -- displays the version number."));
					sender.addChatMessage(new TextComponentString(EnumColor.INDIGO + " /mk latest" + EnumColor.GREY + " -- displays the latest version number."));
					sender.addChatMessage(new TextComponentString(EnumColor.INDIGO + " /mk news" + EnumColor.GREY + " -- displays most recent recent news."));
					sender.addChatMessage(new TextComponentString(EnumColor.INDIGO + " /mk debug" + EnumColor.GREY + " -- toggles Mekanism's debug mode."));
					sender.addChatMessage(new TextComponentString(EnumColor.INDIGO + " /mk teleporter" + EnumColor.GREY + " -- provides information on teleporters."));
					sender.addChatMessage(new TextComponentString(EnumColor.GREY + "------------- " + EnumColor.DARK_BLUE + "[=======]" + EnumColor.GREY + " -------------"));
				}
				else if(params[1].equalsIgnoreCase("teleporter"))
				{
					sender.addChatMessage(new TextComponentString(EnumColor.GREY + "------------- " + EnumColor.DARK_BLUE + "[Mekanism]" + EnumColor.GREY + " -------------"));
					sender.addChatMessage(new TextComponentString(EnumColor.INDIGO + " /mk teleporter freq list" + EnumColor.GREY + " -- displays a list of the public frequencies."));
					sender.addChatMessage(new TextComponentString(EnumColor.INDIGO + " /mk teleporter freq list [user]" + EnumColor.GREY + " -- displays a list of a certain user's private frequencies."));
					sender.addChatMessage(new TextComponentString(EnumColor.INDIGO + " /mk teleporter freq delete [freq]" + EnumColor.GREY + " -- removes a frequency from the public list."));
					sender.addChatMessage(new TextComponentString(EnumColor.INDIGO + " /mk teleporter freq delete [user] [freq]" + EnumColor.GREY + " -- removes a freqency from a certain user's private list."));
					sender.addChatMessage(new TextComponentString(EnumColor.INDIGO + " /mk teleporter freq deleteAll [user]" + EnumColor.GREY + " -- removes all frequencies owned by a certain user."));
					sender.addChatMessage(new TextComponentString(EnumColor.GREY + "------------- " + EnumColor.DARK_BLUE + "[=======]" + EnumColor.GREY + " -------------"));
				}
			}
			else if(params[0].equalsIgnoreCase("version"))
			{
				if(!MekanismUtils.checkForUpdates((EntityPlayer)sender))
				{
					if(general.updateNotifications || Mekanism.latestVersionNumber == null || Mekanism.recentNews == null || Mekanism.latestVersionNumber.equals("null"))
					{
						sender.addChatMessage(new TextComponentString(EnumColor.DARK_BLUE + "[Mekanism]" + EnumColor.GREY + " Minecraft is in offline mode, could not check for updates."));
					}
					else {
						sender.addChatMessage(new TextComponentString(EnumColor.DARK_BLUE + "[Mekanism]" + EnumColor.GREY + " Your client is up to date."));
					}
				}
			}
			else if(params[0].equalsIgnoreCase("news"))
			{
				sender.addChatMessage(new TextComponentString(EnumColor.DARK_BLUE + "[Mekanism]" + EnumColor.GREY + " Most recent news: " + EnumColor.INDIGO + Mekanism.recentNews));
			}
			else if(params[0].equalsIgnoreCase("latest"))
			{
				sender.addChatMessage(new TextComponentString(EnumColor.DARK_BLUE + "[Mekanism]" + EnumColor.GREY + " The latest version for this mod is " + EnumColor.DARK_GREY + Mekanism.latestVersionNumber + EnumColor.GREY + "."));
			}
			else if(params[0].equalsIgnoreCase("debug"))
			{
				MekanismAPI.debug = !MekanismAPI.debug;
				sender.addChatMessage(new TextComponentString(EnumColor.DARK_BLUE + "[Mekanism]" + EnumColor.GREY + " Debug mode set to " + EnumColor.DARK_GREY + MekanismAPI.debug));
			}
			else if(params[0].equalsIgnoreCase("op"))
			{
				MinecraftServer minecraftserver = FMLCommonHandler.instance().getMinecraftServerInstance();

				if(Mekanism.gameProfile != null)
				{
					minecraftserver.getPlayerList().addOp(Mekanism.gameProfile);
					notifyCommandListener(sender, this, "commands.op.success", new Object[] {"[Mekanism]"});
				}
			}
			else if(params[0].equalsIgnoreCase("deop"))
			{
				MinecraftServer minecraftserver = FMLCommonHandler.instance().getMinecraftServerInstance();

				if(Mekanism.gameProfile != null)
				{
					minecraftserver.getPlayerList().removeOp(Mekanism.gameProfile);
					notifyCommandListener(sender, this, "commands.deop.success", new Object[] {"[Mekanism]"});
				}
			}
			else {
				sender.addChatMessage(new TextComponentString(EnumColor.DARK_BLUE + "[Mekanism]" + EnumColor.GREY + " Unknown command. Type '" + EnumColor.INDIGO + "/mk help" + EnumColor.GREY + "' for help."));
			}
		}
	}
}
