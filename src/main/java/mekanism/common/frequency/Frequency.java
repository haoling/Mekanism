package mekanism.common.frequency;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import mekanism.api.Coord4D;
import mekanism.common.PacketHandler;
import mekanism.common.util.MekanismUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.UsernameCache;

public class Frequency
{
	public static final String TELEPORTER = "Teleporter";
	
	public String name;
	public UUID ownerUUID;
	public String clientOwner;
	
	public boolean valid = true;
	
	public boolean publicFreq;
	
	public Set<Coord4D> activeCoords = new HashSet<Coord4D>();
	
	public Frequency(String n, UUID uuid)
	{
		name = n;
		ownerUUID = uuid;
	}
	
	public Frequency(NBTTagCompound nbtTags)
	{
		read(nbtTags);
	}
	
	public Frequency(ByteBuf dataStream)
	{
		read(dataStream);
	}
	
	public Frequency setPublic(boolean isPublic)
	{
		publicFreq = isPublic;
		
		return this;
	}
	
	public boolean isPublic()
	{
		return publicFreq;
	}
	
	public boolean isPrivate()
	{
		return !publicFreq;
	}
	
	public Coord4D getClosestCoords(Coord4D coord)
	{
		Coord4D closest = null;
		
		for(Coord4D iterCoord : activeCoords)
		{
			if(iterCoord.equals(coord))
			{
				continue;
			}
			
			if(closest == null)
			{
				closest = iterCoord;
				continue;
			}
			
			if(coord.dimensionId != closest.dimensionId && coord.dimensionId == iterCoord.dimensionId)
			{
				closest = iterCoord;
				continue;
			}
			else if(coord.dimensionId == closest.dimensionId && coord.dimensionId != iterCoord.dimensionId)
			{
				continue;
			}
			else {
				if(coord.distanceTo(closest) > coord.distanceTo(iterCoord))
				{
					closest = iterCoord;
					continue;
				}
				else {
					continue;
				}
			}
		}
		
		return closest;
	}
	
	public void write(NBTTagCompound nbtTags)
	{
		nbtTags.setString("name", name);
		nbtTags.setString("ownerUUID", ownerUUID.toString());
		nbtTags.setBoolean("publicFreq", publicFreq);
	}

	protected void read(NBTTagCompound nbtTags)
	{
		name = nbtTags.getString("name");
		if (nbtTags.hasKey("ownerUUID"))
		{
			ownerUUID = UUID.fromString(nbtTags.getString("ownerUUID"));
		} else if (nbtTags.hasKey("owner"))
		{
			String oldOwner = nbtTags.getString("owner");
			for (Map.Entry<UUID, String> entry : UsernameCache.getMap().entrySet())
			{
				if (entry.getValue().equals(oldOwner)){
					ownerUUID = entry.getKey();
					break;
				}
			}
		}
		publicFreq = nbtTags.getBoolean("publicFreq");
	}

	public void write(ArrayList<Object> data)
	{
		data.add(name);
		data.add(ownerUUID.toString());
		data.add(MekanismUtils.getLastKnownUsername(ownerUUID));
		data.add(publicFreq);
	}

	protected void read(ByteBuf dataStream)
	{
		name = PacketHandler.readString(dataStream);
		ownerUUID = UUID.fromString(PacketHandler.readString(dataStream));
		clientOwner = PacketHandler.readString(dataStream);
		publicFreq = dataStream.readBoolean();
	}
	
	@Override
	public int hashCode()
	{
		int code = 1;
		code = 31 * code + name.hashCode();
		code = 31 * code + ownerUUID.hashCode();
		code = 31 * code + (publicFreq ? 1 : 0);
		return code;
	}

	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof Frequency && ((Frequency)obj).name.equals(name) 
				&& ((Frequency)obj).ownerUUID.equals(ownerUUID) && ((Frequency)obj).publicFreq == publicFreq;
	}
}