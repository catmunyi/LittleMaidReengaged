package net.blacklab.lmr.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

import java.util.Comparator;

public class EntityAILMNearestAttackableTargetSorter<T extends EntityLivingBase> implements Comparator<EntityLivingBase> {

	private Entity theEntity;

	public EntityAILMNearestAttackableTargetSorter(Entity par1Entity) {
		this.theEntity = par1Entity;
	}

	public int compareDistanceSq(EntityLivingBase par1Entity, EntityLivingBase par2Entity) {
		double var3 = this.theEntity.getDistanceSq(par1Entity);
		double var5 = this.theEntity.getDistanceSq(par2Entity);
        return Double.compare(var3, var5);
	}

	@Override
	public int compare(EntityLivingBase par1Obj, EntityLivingBase par2Obj) {
		return compareDistanceSq(par1Obj, par2Obj);
	}

	public void setEntity(Entity pEntity) {
		theEntity = pEntity;
	}

}
