package net.blacklab.lmr.client.resource;

import com.google.common.collect.ImmutableSet;
import net.blacklab.lmr.LittleMaidReengaged;
import net.blacklab.lmr.client.sound.SoundRegistry;
import net.blacklab.lmr.util.FileList;
import net.minecraft.client.resources.DefaultResourcePack;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * サウンドパック用
 */
public class SoundResourcePack implements IResourcePack, Closeable {

	public SoundResourcePack() {
	}

	@Override
    public InputStream getInputStream(ResourceLocation par1ResourceLocation) {
        LittleMaidReengaged.Debug("GET STREAM %s", par1ResourceLocation.getPath());
		InputStream inputstream = getResourceStream(par1ResourceLocation);
//		if (inputstream != null) {
		return inputstream;
//		} else {
//			throw new FileNotFoundException(par1ResourceLocation.getResourcePath());
//		}
	}

	private InputStream getResourceStream(ResourceLocation resource) {
		InputStream lis = null;
        if (resource.getPath().endsWith("sounds.json")) {
			//return LittleMaidReengaged.class.getClassLoader().getResourceAsStream("LittleMaidReengaged/sounds.json");
			File jsonDir = new File(FileList.dirMods, "LittleMaidReengaged");
			if (!jsonDir.exists()) {
				return null;
			}
			File jsonFile = new File(jsonDir, "sounds.json");
			if (!jsonFile.exists() || !jsonFile.canRead()) {
				return null;
			}
			try {
				lis = new FileInputStream(jsonFile);
            }
            catch (FileNotFoundException ignored) {
            }
        }
        if (resource.getPath().endsWith(".ogg")) {
			//lis = LittleMaidReengaged.class.getClassLoader().getResourceAsStream(decodePathGetPath(resource));
			String f = decodePathGetName(resource);
			if (!SoundRegistry.isSoundNameRegistered(f)) {
				return null;
			}
			return SoundRegistry.getSoundStream(f);
		}
		return lis;
	}

	@Override
	public boolean resourceExists(ResourceLocation resource) {
        LittleMaidReengaged.Debug("RESOURCE CHECK %s", resource.getPath());
        if (resource.getPath().endsWith("sounds.json")) {
			File jsonDir = new File(FileList.dirMods, "LittleMaidReengaged");
			if (!jsonDir.exists()) {
				return false;
			}
			File jsonFile = new File(jsonDir, "sounds.json");
            return jsonFile.exists();
        }
        if (resource.getPath().endsWith(".ogg")) {
			String f = decodePathGetName(resource);
            return SoundRegistry.isSoundNameRegistered(f) && SoundRegistry.getPathListFromRegisteredName(f) != null;
		}
		return false;
	}

	private String decodePathSplicePathStr(ResourceLocation rl) {
        String path = rl.getPath();
		Pattern pattern = Pattern.compile("^/*?sounds/(.+)\\.ogg");
		Matcher matcher = pattern.matcher(path);
		if (matcher.find()) {
			return matcher.group(1);
		}
		return null;
	}

	private String decodePathGetName(ResourceLocation rl) {
		String f = decodePathSplicePathStr(rl);
		String[] gs = f.split("//");
		return gs[0];
	}

	private String decodePathGetPath(ResourceLocation rl) {
		String f = decodePathSplicePathStr(rl);
		String[] gs = f.split("//");
		return gs.length>1 ? gs[1] : null;
	}

	@SuppressWarnings("rawtypes")
	public static final Set lmmxResourceDomains = ImmutableSet.of(LittleMaidReengaged.DOMAIN);

	@Override
	@SuppressWarnings("rawtypes")
	public Set getResourceDomains() {
		return lmmxResourceDomains;
	}

	@Override
	public IMetadataSection getPackMetadata(MetadataSerializer par1MetadataSerializer, String par2Str)
	{ //throws IOException {
		return null;
	}

	// 未使用
	@Override
	public BufferedImage getPackImage() {// throws IOException {
		try {
			return ImageIO.read(DefaultResourcePack.class.getResourceAsStream("/"
                    + (new ResourceLocation("pack.png")).getPath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getPackName() {
		return "SoundPackLMR";
	}

	@Override
    public void close() {
		SoundRegistry.close();
	}
}
