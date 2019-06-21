package net.blacklab.lmr.entity.littlemaid.mode;

import net.blacklab.lmr.LittleMaidReengaged;
import net.blacklab.lmr.achievements.AchievementsLMRE;
import net.blacklab.lmr.entity.ai.EntityAILMHurtByTarget;
import net.blacklab.lmr.entity.ai.EntityAILMNearestAttackableTarget;
import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.blacklab.lmr.entity.littlemaid.trigger.ModeTrigger;
import net.blacklab.lmr.inventory.InventoryLittleMaid;
import net.blacklab.lmr.util.Counter;
import net.blacklab.lmr.util.helper.CommonHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

import java.util.HashMap;
import java.util.UUID;

/**
 * 独自基準としてモード定数は0x0080は平常、0x00c0は血まみれモードと区別。
 */
public class EntityMode_Fencer extends EntityModeBase {

	public static final String mmode_Fencer			= "Fencer";
	public static final String mmode_Bloodsucker	= "Bloodsucker";

	public static final String mtrigger_Sword 	= "Fencer:Sword";
	public static final String mtrigger_Axe = "Bloodsucker:Axe";
	
	// Charging timer
	protected Counter ticksCharge;
	protected static final UUID CHARGING_BOOST_UUID = UUID.nameUUIDFromBytes(LittleMaidReengaged.DOMAIN.concat(":fencer_charge_boost").getBytes());
	protected static final AttributeModifier CHARGING_BOOST_MODIFIER = new AttributeModifier(CHARGING_BOOST_UUID, LittleMaidReengaged.DOMAIN.concat(":fencer_charge_boost"), 0.2d, 0);

	protected static final int CHARGE_COUNTER_MAX_VALUE = 60;
	public EntityMode_Fencer(EntityLittleMaid pEntity) {
		super(pEntity);
		isAnytimeUpdate = true;
		ticksCharge = new Counter(-20*30, CHARGE_COUNTER_MAX_VALUE, -20*30);
	}

	@Override
	public int priority() {
		return 3000;
	}

	@Override
	public void init() {
		// 登録モードの名称追加
		/* langファイルに移動
		ModLoader.addLocalization("littleMaidMob.mode.Fencer", "Fencer");
		ModLoader.addLocalization("littleMaidMob.mode.Fencer", "ja_JP", "護衛剣士");
		ModLoader.addLocalization("littleMaidMob.mode.F-Fencer", "F-Fencer");
		ModLoader.addLocalization("littleMaidMob.mode.F-Fencer", "ja_JP", "自由剣士");
		ModLoader.addLocalization("littleMaidMob.mode.T-Fencer", "T-Fencer");
		ModLoader.addLocalization("littleMaidMob.mode.D-Fencer", "D-Fencer");
		ModLoader.addLocalization("littleMaidMob.mode.Bloodsucker", "Bloodsucker");
		ModLoader.addLocalization("littleMaidMob.mode.Bloodsucker", "ja_JP", "血に飢えた冥土");
		ModLoader.addLocalization("littleMaidMob.mode.F-Bloodsucker", "F-Bloodsucker");
		ModLoader.addLocalization("littleMaidMob.mode.F-Bloodsucker", "ja_JP", "通魔冥土");
		ModLoader.addLocalization("littleMaidMob.mode.T-Bloodsucker", "T-Bloodsucker");
		ModLoader.addLocalization("littleMaidMob.mode.D-Bloodsucker", "D-Bloodsucker");
		*/
		ModeTrigger.registerTrigger(mtrigger_Sword, new HashMap<>());
		ModeTrigger.registerTrigger(mtrigger_Axe, new HashMap<>());
	}

	@Override
	public void addEntityMode(EntityAITasks pDefaultMove, EntityAITasks pDefaultTargeting) {
		// Fencer:0x0080
		EntityAITasks[] ltasks = new EntityAITasks[2];
		ltasks[0] = pDefaultMove;
		ltasks[1] = new EntityAITasks(owner.aiProfiler);

//		ltasks[1].addTask(1, new EntityAIOwnerHurtByTarget(owner));
//		ltasks[1].addTask(2, new EntityAIOwnerHurtTarget(owner));
		ltasks[1].addTask(3, new EntityAILMHurtByTarget(owner, true));
		ltasks[1].addTask(4, new EntityAILMNearestAttackableTarget(owner, EntityLivingBase.class, 0, true));

		owner.addMaidMode(mmode_Fencer, ltasks);


		// Bloodsucker:0x00c0
		EntityAITasks[] ltasks2 = new EntityAITasks[2];
		ltasks2[0] = pDefaultMove;
		ltasks2[1] = new EntityAITasks(owner.aiProfiler);

		ltasks2[1].addTask(1, new EntityAILMHurtByTarget(owner, true));
		ltasks2[1].addTask(2, new EntityAILMNearestAttackableTarget(owner, EntityLivingBase.class, 0, true));

		owner.addMaidMode(mmode_Bloodsucker, ltasks2);
	}

	@Override
	public boolean changeMode(EntityPlayer pentityplayer) {
		ItemStack litemstack = owner.getHandSlotForModeChange();
		if (!litemstack.isEmpty()) {
			if (isTriggerItem(mmode_Fencer, litemstack)) {
				owner.setMaidMode(mmode_Fencer);
				if (pentityplayer != null) {
					AchievementsLMRE.grantAdvancement(pentityplayer, "fencer");
				}
				if (litemstack.getItem() instanceof ItemSpade && pentityplayer != null) {
					AchievementsLMRE.grantAdvancement(pentityplayer, "zombuster");
				}
				return true;
			} else  if (isTriggerItem(mmode_Bloodsucker, litemstack)) {
				owner.setMaidMode(mmode_Bloodsucker);
				if (pentityplayer != null) {
					AchievementsLMRE.grantAdvancement(pentityplayer, "bloodsucker");
				}
				if (litemstack.getItem() instanceof ItemSpade && pentityplayer != null) {
					AchievementsLMRE.grantAdvancement(pentityplayer, "zombuster");
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean setMode(String pMode) {
		switch (pMode) {
		case mmode_Fencer :
//			pentitylittlemaid.maidInventory.currentItem = getNextEquipItem(pentitylittlemaid, pMode);
			owner.setBloodsuck(false);
			owner.aiAttack.isGuard = true;
			return true;
		case mmode_Bloodsucker :
//			pentitylittlemaid.maidInventory.currentItem = getNextEquipItem(pentitylittlemaid, pMode);
			owner.setBloodsuck(true);
			return true;
		}

		return false;
	}

	@Override
	public int getNextEquipItem(String pMode) {
		if (isTriggerItem(pMode, owner.getHandSlotForModeChange())) {
			return InventoryLittleMaid.handInventoryOffset;
		}

		int li;
		int ll = -1;
		double ld = 0;
		double lld;
		ItemStack litemstack;

		// モードに応じた識別判定、速度優先
		switch (pMode) {
		case mmode_Fencer :
			for (li = 0; li < owner.maidInventory.getSizeInventory() - 1; li++) {
				litemstack = owner.maidInventory.getStackInSlot(li);
				if (litemstack.isEmpty()) continue;

				// 剣
				if (isTriggerItem(pMode, litemstack)) {
					return li;
				}

				// 攻撃力な高いものを記憶する
				lld = 1;
				try {
					lld = CommonHelper.getAttackVSEntity(litemstack);
				}
                catch (Exception ignored) {
				}
				if (lld > ld) {
					ll = li;
					ld = lld;
				}
			}
			break;
		case mmode_Bloodsucker :
			for (li = 0; li < owner.maidInventory.getSizeInventory(); li++) {
				litemstack = owner.maidInventory.getStackInSlot(li);
				if (litemstack.isEmpty()) continue;

				// 斧
				if (isTriggerItem(pMode, litemstack)) {
					return li;
				}

				// 攻撃力な高いものを記憶する
				lld = 1;
				try {
					lld = CommonHelper.getAttackVSEntity(litemstack);
				}
                catch (Exception ignored) {
				}
				if (lld > ld) {
					ll = li;
					ld = lld;
				}
			}
			break;
		}

		return -1;
	}

	@Override
	protected boolean isTriggerItem(String pMode, ItemStack par1ItemStack) {
		if (par1ItemStack.isEmpty()) {
			return false;
		}

		switch (pMode) {
		case mmode_Fencer:
			return owner.getModeTrigger().isTriggerable(mtrigger_Sword, par1ItemStack, ItemSword.class);
		case mmode_Bloodsucker:
			return owner.getModeTrigger().isTriggerable(mtrigger_Axe, par1ItemStack, ItemAxe.class);
		}
		return super.isTriggerItem(pMode, par1ItemStack);
	}

	@Override
	public boolean checkItemStack(ItemStack pItemStack) {
		// 装備アイテムを回収
		return pItemStack.getItem() instanceof ItemSword || pItemStack.getItem() instanceof ItemAxe;
	}
	
	@Override
	public boolean isSearchEntity() {
		return owner.getMaidModeString().equals(mmode_Fencer);
	}
	
	@Override
	public boolean checkEntity(String pMode, Entity pEntity) {
		// Distance from master
		if (!owner.isFreedom() && owner.getMaidMasterEntity() != null &&
				owner.getMaidMasterEntity().getDistanceSq(pEntity) >= getLimitRangeSqOnFollow()) {
			return false;
		}

		if (pEntity instanceof EntityCreeper) {
            if (owner.getMaidMasterEntity() == null || !owner.getMaidMasterEntity().equals(((EntityCreeper) pEntity).getAttackTarget())) {
				return false;
			}
		}
		return !owner.getIFF(pEntity);
	}
	
	@Override
	public void updateAITick(String pMode) {
		super.updateAITick(pMode);
		if (pMode.equals(mmode_Fencer)|| pMode.equals(mmode_Bloodsucker)) {
			// Charge(boost moving speed)
			ticksCharge.onUpdate();
			EntityLivingBase targetEntity = owner.getAttackTarget();
			if (targetEntity != null && !targetEntity.isDead) {
				if (!ticksCharge.isDelay()) {
					// Reset counter
					ticksCharge.setValue(CHARGE_COUNTER_MAX_VALUE);
				}
				if (ticksCharge.isEnable()) {
					// Keep boosting speed
					IAttributeInstance maidAttribute;
					if (!(maidAttribute = owner.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED)).hasModifier(CHARGING_BOOST_MODIFIER)) {
						maidAttribute.applyModifier(CHARGING_BOOST_MODIFIER);
					}
				} else {
					// Reset speed
					resetSpeed();
				}
			} else {
				// no target or target died
				resetSpeed();
				if (ticksCharge.isEnable()) {
					ticksCharge.setValue(0);
				}
			}
		}
	}
	/**
	 * Reset AI speed once.
	 */
	protected void resetSpeed() {
		IAttributeInstance maidAttribute;
		if ((maidAttribute = owner.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED)).hasModifier(CHARGING_BOOST_MODIFIER)) {
			maidAttribute.removeModifier(CHARGING_BOOST_UUID);
		}
	}

	@Override
	public double getDistanceToSearchTargets() {
		if (owner.isFreedom()) {
			return 18d;
		}
		return super.getDistanceToSearchTargets();
	}

	@Override
	public double getLimitRangeSqOnFollow() {
		return 18 * 18;
	}

	@Override
	public double getFreedomTrackingRangeSq() {
		return 25 * 25;
	}
}
