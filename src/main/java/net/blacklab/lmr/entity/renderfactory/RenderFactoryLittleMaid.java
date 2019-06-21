package net.blacklab.lmr.entity.renderfactory;

import net.blacklab.lmr.client.renderer.entity.RenderLittleMaid;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderFactoryLittleMaid implements IRenderFactory<Entity> {

	@Override
    public Render<? super Entity> createRenderFor(RenderManager manager) {
		return new RenderLittleMaid(manager, 0.3F);
	}

}
