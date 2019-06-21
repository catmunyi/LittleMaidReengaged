package net.blacklab.lmr.client.resource;

import com.google.common.collect.ImmutableSet;
import net.blacklab.lmr.LittleMaidReengaged;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.util.ResourceLocation;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Set;

public class OldZipTexturesWrapper implements IResourcePack {

    public static ArrayList<String> keys = new ArrayList<>();

	@Override
    public InputStream getInputStream(ResourceLocation arg0) {
		if(resourceExists(arg0)){
            String key = arg0.getPath();
			if(key.startsWith("/")) key = key.substring(1);
			return LittleMaidReengaged.class.getClassLoader().getResourceAsStream(key);
		}
		return null;
	}

	@Override
    public BufferedImage getPackImage() {
		return null;
	}

	@Override
	public IMetadataSection getPackMetadata(MetadataSerializer arg0,
                                            String arg1) {
		return null;
	}

	@Override
	public String getPackName() {
		return "OldTexturesLoader";
	}

	@Override
	public Set<String> getResourceDomains() {
		return ImmutableSet.of("minecraft");
	}

	@Override
	public boolean resourceExists(ResourceLocation arg0) {
        String key = arg0.getPath();
		if(key.startsWith("/")) key = key.substring(1);
		return keys.contains(key);
	}

}
