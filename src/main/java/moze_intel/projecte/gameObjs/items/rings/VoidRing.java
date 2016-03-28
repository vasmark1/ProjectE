package moze_intel.projecte.gameObjs.items.rings;

import moze_intel.projecte.gameObjs.ObjHandler;
import moze_intel.projecte.gameObjs.items.GemEternalDensity;
import moze_intel.projecte.utils.PlayerHelper;
import moze_intel.projecte.api.item.IAlchBagItem;
import moze_intel.projecte.api.item.IAlchChestItem;
import moze_intel.projecte.api.item.IExtraFunction;
import moze_intel.projecte.api.item.IPedestalItem;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;

public class VoidRing extends GemEternalDensity implements IPedestalItem, IExtraFunction
{
	public VoidRing()
	{
		this.setUnlocalizedName("void_ring");
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean isHeld)
	{
		super.onUpdate(stack, world, entity, slot, isHeld);
		ObjHandler.blackHole.onUpdate(stack, world, entity, slot, isHeld);
		if (!stack.getTagCompound().hasKey("teleportCooldown"))
		{
			stack.getTagCompound().setByte("teleportCooldown", ((byte) 10));
		}
		stack.getTagCompound().setByte("teleportCooldown", ((byte) (stack.getTagCompound().getByte("teleportCooldown") - 1)));
	}


	@Override
	public void updateInPedestal(World world, BlockPos pos)
	{
		((IPedestalItem) ObjHandler.blackHole).updateInPedestal(world, pos);
	}

	@Override
	public List<String> getPedestalDescription()
	{
		return ((IPedestalItem) ObjHandler.blackHole).getPedestalDescription();
	}

	@Override
	public void doExtraFunction(ItemStack stack, EntityPlayer player)
	{
		if (stack.getTagCompound().getByte("teleportCooldown") > 0 )
		{
			return;
		}

		BlockPos c = PlayerHelper.getBlockLookingAt(player, 64);
		if (c == null)
		{
			c = new BlockPos(PlayerHelper.getLookVec(player, 32).getRight());
		}

		EnderTeleportEvent event = new EnderTeleportEvent(player, c.getX(), c.getY(), c.getZ(), 0);
		if (!MinecraftForge.EVENT_BUS.post(event))
		{
			if (player.isRiding())
			{
				player.dismountRidingEntity();
			}

			player.setPositionAndUpdate(event.getTargetX(), event.getTargetY(), event.getTargetZ());
			player.worldObj.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.entity_endermen_teleport, SoundCategory.PLAYERS, 1, 1);
			player.fallDistance = 0.0F;
			stack.getTagCompound().setByte("teleportCooldown", ((byte) 10));
		}
	}

	@Override
	public boolean updateInAlchBag(ItemStack[] inv, EntityPlayer player, ItemStack stack)
	{
		((IAlchBagItem) ObjHandler.blackHole).updateInAlchBag(inv, player, stack);
		return super.updateInAlchBag(inv, player, stack); // Gem of Eternal Density
	}

	@Override
	public void updateInAlchChest(World world, BlockPos pos, ItemStack stack)
	{
		super.updateInAlchChest(world, pos, stack); // Gem of Eternal Density
		((IAlchChestItem) ObjHandler.blackHole).updateInAlchChest(world, pos, stack);
	}
}
