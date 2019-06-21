package net.blacklab.lmr.entity.maidmodel;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.EntityLivingBase;

public interface IModelBaseMMM extends IModelCaps {

    void renderItems(EntityLivingBase pEntity, Render pRender);

    void showArmorParts(int pParts);

    void setEntityCaps(IModelCaps pModelCaps);

    void setRender(Render pRender);

    void setArmorRendering(boolean pFlag);

}
