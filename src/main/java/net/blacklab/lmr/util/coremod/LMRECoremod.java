package net.blacklab.lmr.util.coremod;

import net.blacklab.lmr.util.helper.CommonHelper;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import java.util.Map;

public class LMRECoremod implements IFMLLoadingPlugin{

	@Override
	public String[] getASMTransformerClass() {
		return new String[] {"net.blacklab.lmr.util.coremod.Transformer"};
	}

	@Override
	public String getModContainerClass() {
		return "net.blacklab.lmr.util.coremod.LMRECoreModContainer";
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {
		
	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}
	
}
