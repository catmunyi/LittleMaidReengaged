package net.blacklab.lmr.entity.maidmodel;

import net.minecraft.client.model.TextureOffset;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.util.math.MathHelper;

import java.util.*;

public abstract class ModelBase extends AbstractModelBase {
	
	public static final float PI = (float)Math.PI;

	public Render render;

	// ModelBaseとある程度互換
	public int textureWidth = 64;
	public int textureHeight = 32;
    public float[] onGrounds = new float[]{0.0F, 0.0F};
	public int dominantArm = 0;
	public boolean isRiding = false;
	public boolean isChild = true;
    public List<ModelRenderer> boxList = new ArrayList<>();
    private Map<String, TextureOffset> modelTextureMap = new HashMap<>();



	// ModelBase互換関数群

	public void render(IModelCaps pEntityCaps, float par2, float par3,
			float ticksExisted, float pheadYaw, float pheadPitch, float par7, boolean pIsRender) {
	}

	public void setRotationAngles(float par1, float par2, float pTicksExisted,
			float pHeadYaw, float pHeadPitch, float par6, IModelCaps pEntityCaps) {
	}

	public void setLivingAnimations(IModelCaps pEntityCaps, float par2, float par3, float pRenderPartialTicks) {
	}

	public ModelRenderer getRandomModelBox(Random par1Random) {
		// 膝に矢を受けてしまってな・・・
		int li = par1Random.nextInt(this.boxList.size());
		ModelRenderer lmr = this.boxList.get(li);
		for (int lj = 0; lj < boxList.size(); lj++) {
			if (!lmr.cubeList.isEmpty()) {
				break;
			}
			// 箱がない
			if (++li >= boxList.size()) {
				li = 0;
			}
			lmr = this.boxList.get(li);
		}
		return lmr;
	}

	protected void setTextureOffset(String par1Str, int par2, int par3) {
		modelTextureMap.put(par1Str, new TextureOffset(par2, par3));
	}

	/**
	 * 推奨されません。
	 */
	public TextureOffset getTextureOffset(String par1Str) {
		// このままだと意味ないな。
		return modelTextureMap.get(par1Str);
	}


	// MathHelperトンネル関数群

	public static final float mh_sin(float f) {
		f = f % 6.283185307179586F;
		f = (f < 0F) ? 360 + f : f;
		return MathHelper.sin(f);
	}

	public static final float mh_cos(float f) {
		f = f % 6.283185307179586F;
		f = (f < 0F) ? 360 + f : f;
		return MathHelper.cos(f);
	}

	public static final float mh_sqrt_float(float f) {
		return MathHelper.sqrt(f);
	}

	public static final float mh_sqrt_double(double d) {
		return MathHelper.sqrt(d);
	}

	public static final int mh_floor_float(float f) {
		return MathHelper.floor(f);
	}

	public static final int mh_floor_double(double d) {
		return MathHelper.floor(d);
	}

	public static final long mh_floor_double_long(double value) {
        long i = (long)value;
        return value < (double)i ? i - 1L : i;
	}

	public static final float mh_abs(float f) {
		return MathHelper.abs(f);
	}

	public static final double mh_abs_max(double d, double d1) {
		return MathHelper.absMax(d, d1);
	}

	public static final int mh_bucketInt(int i, int j) {
		return i < 0 ? -((-i - 1) / j) - 1 : i / j;
	}

	public static final boolean mh_stringNullOrLengthZero(String s) {
        return s == null || "".equals(s);
	}

	public static final int mh_getRandomIntegerInRange(Random random, int minimum, int maximum) {
		return minimum >= maximum ? minimum : random.nextInt(maximum - minimum + 1) + minimum;
	}

}
