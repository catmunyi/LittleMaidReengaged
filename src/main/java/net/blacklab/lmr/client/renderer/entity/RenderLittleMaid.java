package net.blacklab.lmr.client.renderer.entity;

import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.blacklab.lmr.entity.maidmodel.IModelCaps;
import net.blacklab.lmr.entity.maidmodel.ModelBaseDuo;
import net.blacklab.lmr.util.helper.RendererHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.Iterator;

public class RenderLittleMaid extends RenderModelMulti {

	// Method
	public RenderLittleMaid(RenderManager manager,float f) {
		super(manager,f);

		addLayer(new MMMLayerHeldItem(this));
		addLayer(new MMMLayerArmor(this));
	}

	/**
	 * 防具描画レイヤー
	 */
	public class MMMLayerArmor extends LayerArmorBase{

		//レイヤーと化した防具描画

		public RenderLivingBase p1;
		public RenderLivingBase field_177190_a;
		public float field_177184_f;
		public float field_177185_g;
		public float field_177192_h;
		public float field_177187_e;
		public boolean field_177193_i;
		public EntityLittleMaid lmm;

		public static final float renderScale = 0.0625F;

		public MMMLayerArmor(RenderLivingBase p_i46125_1_) {
			super(p_i46125_1_);
			p1 = p_i46125_1_;
//			this.modelLeggings = mmodel;
//			this.modelArmor = mmodel;
		}

		@Override
		protected void initArmor() {
		}

		@Override
		protected void setModelSlotVisible(ModelBase paramModelBase, EntityEquipmentSlot paramInt) {
			ModelBaseDuo model = (ModelBaseDuo) paramModelBase;
			model.showArmorParts(paramInt.getIndex());
		}

		@Override
		public void doRenderLayer(EntityLivingBase par1EntityLiving, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
			lmm = (EntityLittleMaid) par1EntityLiving;

			for (int i=0; i<4; i++) {
				if (!lmm.maidInventory.armorItemInSlot(i).isEmpty()) {
					render(par1EntityLiving, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, i);
				}
			}
		}

		public void render(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, int renderParts) {
//			boolean lri = (renderCount & 0x0f) == 0;
			//総合
			modelFATT.showArmorParts(renderParts);

			//Inner
			INNER:{
				if(modelFATT.textureInner!=null){
					ResourceLocation texInner = modelFATT.textureInner[renderParts];
					if(texInner!=null&&lmm.isArmorVisible(0)) try{
						Minecraft.getMinecraft().getTextureManager().bindTexture(texInner);
						GL11.glEnable(GL11.GL_BLEND);
						GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
						modelFATT.modelInner.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, renderScale, fcaps);
						modelFATT.modelInner.setLivingAnimations(fcaps, limbSwing, limbSwingAmount, partialTicks);
						modelFATT.modelInner.render(fcaps, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, renderScale, true);
                    }
                    catch (Exception ignored) {
                    }
				} else {
//					modelFATT.modelInner.render(lmm.maidCaps, par2, par3, lmm.ticksExisted, par5, par6, renderScale, true);
				}
			}

			// 発光Inner
			INNERLIGHT: if (modelFATT.modelInner!=null) {
				ResourceLocation texInnerLight = modelFATT.textureInnerLight[renderParts];
				if (texInnerLight != null&&lmm.isArmorVisible(1)) {
					try{
						Minecraft.getMinecraft().getTextureManager().bindTexture(texInnerLight);
						GL11.glEnable(GL11.GL_BLEND);
						GL11.glDisable(GL11.GL_ALPHA_TEST);
						GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
						GL11.glDepthFunc(GL11.GL_LEQUAL);

						RendererHelper.setLightmapTextureCoords(0x00f000f0);//61680
						if (modelFATT.textureLightColor == null) {
							GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
						} else {
							//発光色を調整
							GL11.glColor4f(
									modelFATT.textureLightColor[0],
									modelFATT.textureLightColor[1],
									modelFATT.textureLightColor[2],
									modelFATT.textureLightColor[3]);
						}
						modelFATT.modelInner.render(fcaps, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, renderScale, true);
						RendererHelper.setLightmapTextureCoords(modelFATT.lighting);
						GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
						GL11.glDisable(GL11.GL_BLEND);
						GL11.glEnable(GL11.GL_ALPHA_TEST);
					}catch(Exception e){ break INNERLIGHT; }
				}
			}

//			Minecraft.getMinecraft().getTextureManager().deleteTexture(lmm.getTextures(0)[renderParts]);
			//Outer
//			if(LittleMaidReengaged.cfg_isModelAlphaBlend) GL11.glEnable(GL11.GL_BLEND);
			OUTER:{
				if(modelFATT.textureOuter!=null){
					ResourceLocation texOuter = modelFATT.textureOuter[renderParts];
					if(texOuter!=null&&lmm.isArmorVisible(2)) try{
						Minecraft.getMinecraft().getTextureManager().bindTexture(texOuter);
						GL11.glEnable(GL11.GL_BLEND);
						GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
						modelFATT.modelOuter.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, renderScale, fcaps);
						modelFATT.modelOuter.setLivingAnimations(fcaps, limbSwing, limbSwingAmount, partialTicks);
						modelFATT.modelOuter.render(fcaps, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, renderScale, true);
                    }
                    catch (Exception ignored) {
                    }
				}else{
//					modelFATT.modelOuter.render(lmm.maidCaps, limbSwing, par3, lmm.ticksExisted, par5, par6, renderScale, true);
				}
			}

			// 発光Outer
			OUTERLIGHT: if (modelFATT.modelOuter!=null) {
				ResourceLocation texOuterLight = modelFATT.textureOuterLight[renderParts];
				if (texOuterLight != null&&lmm.isArmorVisible(3)) {
					try{
						Minecraft.getMinecraft().getTextureManager().bindTexture(texOuterLight);
						GL11.glEnable(GL11.GL_BLEND);
						GL11.glEnable(GL11.GL_ALPHA_TEST);
						GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
						GL11.glDepthFunc(GL11.GL_LEQUAL);

						RendererHelper.setLightmapTextureCoords(0x00f000f0);//61680
						if (modelFATT.textureLightColor == null) {
							GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
						} else {
							//発光色を調整
							GL11.glColor4f(
									modelFATT.textureLightColor[0],
									modelFATT.textureLightColor[1],
									modelFATT.textureLightColor[2],
									modelFATT.textureLightColor[3]);
						}
						if(lmm.isArmorVisible(1)) modelFATT.modelOuter.render(fcaps, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, renderScale, true);
						RendererHelper.setLightmapTextureCoords(modelFATT.lighting);
						GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
						GL11.glDisable(GL11.GL_BLEND);
						GL11.glEnable(GL11.GL_ALPHA_TEST);
					}catch(Exception e){ break OUTERLIGHT; }
					GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				}
			}
		}
	}

	/**
	 * 手持ちアイテムレイヤー
	 */
	public class MMMLayerHeldItem extends LayerHeldItem{

		//レイヤーと化したアイテム描画

		protected RenderLivingBase renderer;
		public MMMLayerHeldItem(RenderLivingBase p_i46115_1_) {
			super(p_i46115_1_);
			renderer = p_i46115_1_;
		}

		@Override
		public void doRenderLayer(EntityLivingBase p_177141_1_,
				float p_177141_2_, float p_177141_3_, float p_177141_4_,
				float p_177141_5_, float p_177141_6_, float p_177141_7_,
				float p_177141_8_) {
			EntityLittleMaid lmm = (EntityLittleMaid) p_177141_1_;

			if(!lmm.isMaidWait()){

				Iterator<ItemStack> heldItemIterator = lmm.getHeldEquipment().iterator();
                int i = 0;
                int[] handindexes = {lmm.getDominantArm(), lmm.getDominantArm() == 1 ? 0 : 1};

                while (heldItemIterator.hasNext()) {
                    ItemStack itemstack = heldItemIterator.next();

					if (!itemstack.isEmpty())
					{
						GlStateManager.pushMatrix();

						// Use dominant arm as mainhand.
						modelMain.model.Arms[handindexes[i]].postRender(0.0625F);

						if (lmm.isSneaking()) {
							GlStateManager.translate(0.0F, 0.2F, 0.0F);
						}
						boolean flag = handindexes[i] == 1;

						GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
						GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
						/* 初期モデル構成で
						 * x: 手の甲に垂直な方向(-で向かって右に移動)
						 * y: 体の面に垂直な方向(-で向かって背面方向に移動)
						 * z: 腕に平行な方向(-で向かって手の先方向に移動)
						 */
						GlStateManager.translate(flag ? -0.0125F : 0.0125F, 0.05f, -0.15f);
						Minecraft.getMinecraft().getItemRenderer().renderItemSide(lmm, itemstack,
								flag ?
										ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND :
										ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND,
								flag);
						GlStateManager.popMatrix();
					}

					i++;
				}

			}

		}


	}

	@Override
	public void setModelValues(EntityLivingBase par1EntityLiving, double par2,
			double par4, double par6, float par8, float par9, IModelCaps pEntityCaps) {
		EntityLittleMaid lmaid = (EntityLittleMaid)par1EntityLiving;
		super.setModelValues(par1EntityLiving, par2, par4, par6, par8, par9, pEntityCaps);

//		modelMain.setRender(this);
//		modelMain.setEntityCaps(pEntityCaps);
//		modelMain.showAllParts();
//		modelMain.isAlphablend = true;
//		modelFATT.isAlphablend = true;

		modelMain.setCapsValue(IModelCaps.caps_heldItemLeft, (Integer)0);
		modelMain.setCapsValue(IModelCaps.caps_heldItemRight, (Integer)0);
//		modelMain.setCapsValue(IModelCaps.caps_onGround, renderSwingProgress(lmaid, par9));
		modelMain.setCapsValue(IModelCaps.caps_onGround,
				lmaid.mstatSwingStatus[0].getSwingProgress(par9),
				lmaid.mstatSwingStatus[1].getSwingProgress(par9));
		modelMain.setCapsValue(IModelCaps.caps_isRiding, lmaid.isRiding());
		modelMain.setCapsValue(IModelCaps.caps_isSneak, lmaid.isSneaking());
		modelMain.setCapsValue(IModelCaps.caps_aimedBow, lmaid.isAimebow());
		modelMain.setCapsValue(IModelCaps.caps_isWait, lmaid.isMaidWait());
		modelMain.setCapsValue(IModelCaps.caps_isChild, lmaid.isChild());
		modelMain.setCapsValue(IModelCaps.caps_entityIdFactor, lmaid.entityIdFactor);
		modelMain.setCapsValue(IModelCaps.caps_ticksExisted, lmaid.ticksExisted);
		modelMain.setCapsValue(IModelCaps.caps_dominantArm, lmaid.getDominantArm());
		modelFATT.setModelAttributes(mainModel);
		// だが無意味だ
//		plittleMaid.textureModel0.isChild = plittleMaid.textureModel1.isChild = plittleMaid.textureModel2.isChild = plittleMaid.isChild();
	}

/*
	@SuppressWarnings("unused")
	protected void renderString(LMM_EntityLittleMaid plittleMaid, double px, double py, double pz, float f, float f1) {
		// ひも
		// TODO 傍目にみた表示がおかしい
		if(plittleMaid.mstatgotcha != null && plittleMaid.mstatgotcha instanceof EntityLivingBase) {
			EntityLivingBase lel = (EntityLivingBase)plittleMaid.mstatgotcha;
			py -= 0.5D;
			Tessellator tessellator = Tessellator.getInstance();
			float f9 = ((lel.prevRotationYaw + (lel.rotationYaw - lel.prevRotationYaw) * f1 * 0.5F) * 3.141593F) / 180F;
			float f3 = ((lel.prevRotationPitch + (lel.rotationPitch - lel.prevRotationPitch) * f1 * 0.5F) * 3.141593F) / 180F;
			double d3 = MathHelper.sin(f9);
			double d5 = MathHelper.cos(f9);
			float f11 = lel.getSwingProgress(f1);
			float f12 = MathHelper.sin(MathHelper.sqrt(f11) * 3.141593F);
			Vec3 vec3d = new Vec3d(-0.5D, 0.029999999999999999D, 0.55D);

			vec3d.rotatePitch((-(lel.prevRotationPitch + (lel.rotationPitch - lel.prevRotationPitch) * f1) * 3.141593F) / 180F);
			vec3d.rotateYaw((-(lel.prevRotationYaw + (lel.rotationYaw - lel.prevRotationYaw) * f1) * 3.141593F) / 180F);
			//vec3d.rotateAroundY(f12 * 0.5F);
			//vec3d.rotateAroundX(-f12 * 0.7F);

			double d7 = lel.prevPosX + (lel.posX - lel.prevPosX) * f1 + vec3d.xCoord;
			double d8 = lel.prevPosY + (lel.posY - lel.prevPosY) * f1 + vec3d.yCoord;
			double d9 = lel.prevPosZ + (lel.posZ - lel.prevPosZ) * f1 + vec3d.zCoord;
			if(renderManager.options.thirdPersonView > 0) {
				float f4 = ((lel.prevRenderYawOffset + (lel.renderYawOffset - lel.prevRenderYawOffset) * f1) * 3.141593F) / 180F;
				double d11 = MathHelper.sin(f4);
				double d13 = MathHelper.cos(f4);
				d7 = (lel.prevPosX + (lel.posX - lel.prevPosX) * f1) - d13 * 0.34999999999999998D - d11 * 0.54999999999999998D;
				d8 = (lel.prevPosY + (lel.posY - lel.prevPosY) * f1) - 0.45000000000000001D;
				d9 = ((lel.prevPosZ + (lel.posZ - lel.prevPosZ) * f1) - d11 * 0.34999999999999998D) + d13 * 0.54999999999999998D;
			}
			double d10 = plittleMaid.prevPosX + (plittleMaid.posX - plittleMaid.prevPosX) * f1;
			double d12 = plittleMaid.prevPosY + (plittleMaid.posY - plittleMaid.prevPosY) * f1 + 0.25D - 0.5D;//+ 0.75D;
			double d14 = plittleMaid.prevPosZ + (plittleMaid.posZ - plittleMaid.prevPosZ) * f1;
			double d15 = (float)(d7 - d10);
			double d16 = (float)(d8 - d12);
			double d17 = (float)(d9 - d14);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_LIGHTING);
			//tessellator.startDrawing(3);
			try {
				tessellator.getWorldRenderer().func_181668_a(3, new VertexFormat()
					.func_181721_a(new VertexFormatElement(0, EnumType.FLOAT, EnumUsage.NORMAL, 16)));
			} catch (Exception e) {
				e.printStackTrace();
			}
			//tessellator.setColorOpaque_I(0);
			GlStateManager.color(1f, 1f, 1f);
			tessellator.getWorldRenderer().putPositionData(new int[]{1, 1, 1, 12, 24});
			int i = 16;
			for(int j = 0; j <= i; j++)
			{
				float f5 = (float)j / (float)i;
				try {
					tessellator.getWorldRenderer().putPosition(px + d15 * f5, py + d16 * (f5 * f5 + f5) * 0.5D + (((float)i - (float)j) / (i * 0.75F) + 0.125F), pz + d17 * f5);
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}

			tessellator.draw();
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
		}
	}
*/
/*
	public void doRenderLitlleMaid(LMM_EntityLittleMaid plittleMaid, double px, double py, double pz, float f, float f1) {
		// いくつか重複してるのであとで確認
		// 姿勢による高さ調整

		// ここは本来的には要らない。
		if (plittleMaid.world instanceof WorldServer) {
			// RSHUD-ACV用
			MMM_TextureBox ltbox0 = ((MMM_TextureBoxServer)plittleMaid.textureData.textureBox[0]).localBox;
			MMM_TextureBox ltbox1 = ((MMM_TextureBoxServer)plittleMaid.textureData.textureBox[1]).localBox;
			modelMain.model = ltbox0.models[0];
			modelFATT.modelInner = ltbox1.models[1];
			modelFATT.modelOuter = ltbox1.models[2];
			plittleMaid.textureData.setTextureNamesServer();
			modelMain.textures = plittleMaid.textureData.getTextures(0);
			modelFATT.textureInner = plittleMaid.textureData.getTextures(1);
			modelFATT.textureOuter = plittleMaid.textureData.getTextures(2);
			modelFATT.textureInnerLight = plittleMaid.textureData.getTextures(3);
			modelFATT.textureOuterLight = plittleMaid.textureData.getTextures(4);
		} else {
			modelMain.model = ((MMM_TextureBox)plittleMaid.textureData.textureBox[0]).models[0];
			modelFATT.modelInner = ((MMM_TextureBox)plittleMaid.textureData.textureBox[1]).models[1];
			modelFATT.modelOuter = ((MMM_TextureBox)plittleMaid.textureData.textureBox[1]).models[2];
			modelMain.textures = plittleMaid.textureData.getTextures(0);
			modelFATT.textureInner = plittleMaid.textureData.getTextures(1);
			modelFATT.textureOuter = plittleMaid.textureData.getTextures(2);
			modelFATT.textureInnerLight = plittleMaid.textureData.getTextures(3);
			modelFATT.textureOuterLight = plittleMaid.textureData.getTextures(4);
		}

//		doRenderLiving(plittleMaid, px, py, pz, f, f1);
		renderModelMulti(plittleMaid, px, py, pz, f, f1, plittleMaid.maidCaps);
		renderString(plittleMaid, px, py, pz, f, f1);
	}
*/
	@Override
	public void doRender(EntityLiving par1EntityLiving,
			double par2, double par4, double par6, float par8, float par9) {
		if (!(par1EntityLiving instanceof EntityLittleMaid)) {
			return;
		}

		EntityLittleMaid lmm = (EntityLittleMaid)par1EntityLiving;

		fcaps = lmm.maidCaps;
//		doRenderLitlleMaid(lmm, par2, par4, par6, par8, par9);
		renderModelMulti(lmm, par2, par4, par6, par8, par9, fcaps);
//		renderString(lmm, par2, par4, par6, par8, par9);
		// ロープ
//		func_110827_b(lmm, par2, par4 - modelMain.model.getLeashOffset(lmm.maidCaps), par6, par8, par9);
	}

	@Override
	public void renderLivingAt(EntityLivingBase par1EntityLiving, double par2, double par4, double par6) {
		super.renderLivingAt(par1EntityLiving, par2, par4, par6);

		EntityLittleMaid llmm = (EntityLittleMaid)par1EntityLiving;
		// 追加分
		for (int li = 0; li < llmm.maidEntityModeList.size(); li++) {
			llmm.maidEntityModeList.get(li).showSpecial(this, par2, par4, par6);
		}
	}

	@Override
	protected int getColorMultiplier(EntityLivingBase par1EntityLiving, float par2, float par3) {
		return ((EntityLittleMaid)par1EntityLiving).colorMultiplier(par2, par3);
	}

    public void renderLivingLabel(Entity p_147906_1_, String p_147906_2_, double p_147906_3_, double p_147906_5_, double p_147906_7_, int p_147906_9_)
    {
    	super.renderLivingLabel(p_147906_1_, p_147906_2_, p_147906_3_, p_147906_5_, p_147906_7_, p_147906_9_);
    }

}
