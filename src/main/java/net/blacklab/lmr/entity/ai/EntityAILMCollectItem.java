package net.blacklab.lmr.entity.ai;

import net.blacklab.lib.minecraft.vector.VectorUtil;
import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.blacklab.lmr.util.EnumSound;
import net.blacklab.lmr.util.helper.ItemHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.math.MathHelper;

import java.util.List;

public class EntityAILMCollectItem extends EntityAIBase {

	protected EntityLittleMaid theMaid;
	protected float moveSpeed;
	protected EntityItem targetItem;
	protected boolean lastAvoidWater;


	public EntityAILMCollectItem(EntityLittleMaid pEntityLittleMaid, float pmoveSpeed) {
		theMaid = pEntityLittleMaid;
		moveSpeed = pmoveSpeed;
		setMutexBits(3);
	}


	@Override
	public boolean shouldExecute() {
		if(theMaid.isMaidWaitEx()) return false;
		if (theMaid.maidInventory.getFirstEmptyStack() > -1) {
			List llist = theMaid.world.getEntitiesWithinAABB(EntityItem.class, theMaid.getEntityBoundingBox().grow(8F, 2D, 8F));
			if (!llist.isEmpty()) {
				int li = theMaid.getRNG().nextInt(llist.size());
				EntityItem ei = (EntityItem)llist.get(li);
				EntityPlayer ep = theMaid.getMaidMasterEntity() != null ? theMaid.getMaidMasterEntity() : theMaid.world.getClosestPlayerToEntity(theMaid, 16F);

				NBTTagCompound p = new NBTTagCompound();
				ei.writeEntityToNBT(p);
				if (!ei.isDead && ei.onGround && p.getShort("PickupDelay") <= 0 && !ei.isBurning()
						&& canEntityItemBeSeen(ei) && (ep == null ||
						ep.getDistanceSq(
								ei.posX + MathHelper.sin(ep.rotationYaw * 0.01745329252F) * 2.0D,
								ei.posY,
								ei.posZ - MathHelper.cos(ep.rotationYaw * 0.01745329252F) * 2.0D) > 7.5D))
				{
					ItemStack lstack = ei.getItem();
					if (!ItemHelper.isSugar(lstack.getItem())) {
						if (!theMaid.isActiveModeClass()) {
							return false;
						}
						if ((!theMaid.getActiveModeClass().checkItemStack(lstack))) {
							return false;
						}
					}
					theMaid.playLittleMaidSound(EnumSound.findTarget_I, false);
					targetItem = ei;
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public void startExecuting() {
		super.startExecuting();
		/*
		lastAvoidWater = theMaid.getNavigator().getAvoidsWater();
		theMaid.getNavigator().setAvoidsWater(true);
		*/
	}

	@Override
	public boolean shouldContinueExecuting() {
		return !targetItem.isDead && (theMaid.maidInventory.getFirstEmptyStack() > -1) && theMaid.getDistanceSq(targetItem) < 100D;
	}

	@Override
	public void resetTask() {
		targetItem = null;
		theMaid.getNavigator().clearPath();
//		theMaid.getNavigator().setAvoidsWater(lastAvoidWater);
	}

	@Override
	public void updateTask() {
		theMaid.getLookHelper().setLookPositionWithEntity(targetItem, 30F, theMaid.getVerticalFaceSpeed());

		PathNavigate lnavigater = theMaid.getNavigator();
		if (lnavigater.noPath()) {
			if (targetItem.isInWater()) {
				//lnavigater.setAvoidsWater(false);
			}
			lnavigater.tryMoveToXYZ(targetItem.posX, targetItem.posY, targetItem.posZ, moveSpeed);
		}
	}

	public boolean canEntityItemBeSeen(Entity entity) {
		// アイテムの可視判定
//		return theMaid.worldObj.rayTraceBlocks(new Vec3(theMaid.posX, theMaid.posY + (double)theMaid.getEyeHeight(), theMaid.posZ), new Vec3(entity.posX, entity.posY + ((entity.getEntityBoundingBox().minY - entity.getEntityBoundingBox().minY) / 2), entity.posZ)) == null;
		return VectorUtil.canMoveThrough(theMaid, 0D, MathHelper.floor(entity.posX), MathHelper.floor(entity.posY), MathHelper.floor(entity.posZ), false, true, false);
	}

}
