package net.blacklab.lmr;

import net.blacklab.lib.vevent.VEventBus;
import net.blacklab.lmr.client.resource.OldZipTexturesWrapper;
import net.blacklab.lmr.client.resource.SoundResourcePack;
import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.blacklab.lmr.event.EventHookLMRE;
import net.blacklab.lmr.item.ItemMaidPorter;
import net.blacklab.lmr.item.ItemMaidSpawnEgg;
import net.blacklab.lmr.item.ItemTriggerRegisterKey;
import net.blacklab.lmr.network.GuiHandler;
import net.blacklab.lmr.network.LMRNetwork;
import net.blacklab.lmr.network.ProxyCommon;
import net.blacklab.lmr.util.DevMode;
import net.blacklab.lmr.util.FileList;
import net.blacklab.lmr.util.IFF;
import net.blacklab.lmr.util.helper.CommonHelper;
import net.blacklab.lmr.util.manager.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Random;

@Mod(
		modid = LittleMaidReengaged.DOMAIN,
		name = "LittleMaidReengaged",
		version = LittleMaidReengaged.VERSION,
		acceptedMinecraftVersions=LittleMaidReengaged.ACCEPTED_MCVERSION,
		dependencies = LittleMaidReengaged.DEPENDENCIES/*,
		updateJSON = "http://mc.el-blacklab.net/lmr-version.json"*/)
public class LittleMaidReengaged {

	public static final String DOMAIN = "lmreengaged";
	public static final String VERSION = "8.1.6.145";
	public static final String ACCEPTED_MCVERSION = "[1.12.2]";
	public static final String DEPENDENCIES = "required-after:forge@[14.23.5.2768,);"
			+ "required-after:net.blacklab.lib@[6.1.7.1,)";

	/*
	 * public static String[] cfg_comment = {
	 * "spawnWeight = Relative spawn weight. The lower the less common. 10=pigs. 0=off"
	 * , "spawnLimit = Maximum spawn count in the World.",
	 * "minGroupSize = Minimum spawn group count.",
	 * "maxGroupSize = Maximum spawn group count.",
	 * "canDespawn = It will despawn, if it lets things go. ",
	 * "checkOwnerName = At local, make sure the name of the owner. ",
	 * "antiDoppelganger = Not to survive the doppelganger. ",
	 * "enableSpawnEgg = Enable LMM SpawnEgg Recipe. ",
	 * "VoiceDistortion = LittleMaid Voice distortion.",
	 * "defaultTexture = Default selected Texture Packege. Null is Random",
	 * "DebugMessage = Print Debug Massages.",
	 * "DeathMessage = Print Death Massages.", "Dominant = Spawn Anywhere.",
	 * "Aggressive = true: Will be hostile, false: Is a pacifist",
	 * "IgnoreItemList = aaa, bbb, ccc: Items little maid to ignore",
	 * "AchievementID = used Achievement index.(0 = Disable)",
	 * "UniqueEntityId = UniqueEntityId(0 is AutoAssigned. max 255)" };
	 */

	// @MLProp(info="Relative spawn weight. The lower the less common. 10=pigs. 0=off")
	public static int cfg_spawnWeight = 5;
	// @MLProp(info="Maximum spawn count in the World.")
	public static int cfg_spawnLimit = 20;
	// @MLProp(info="Minimum spawn group count.")
	public static int cfg_minGroupSize = 1;
	// @MLProp(info="Maximum spawn group count.")
	public static int cfg_maxGroupSize = 3;
	// @MLProp(info="It will despawn, if it lets things go. ")
	public static boolean cfg_canDespawn = false;

	// @MLProp(info="Print Debug Massages.")
	public static boolean cfg_PrintDebugMessage = false;
	// @MLProp(info="Print Death Massages.")
	public static boolean cfg_DeathMessage = true;
	// @MLProp(info="Spawn Anywhere.")
	public static boolean cfg_Dominant = false;
	// アルファブレンド
	public static boolean cfg_isModelAlphaBlend = false;
	// 野生テクスチャ
	public static boolean cfg_isFixedWildMaid = false;

	public static final float cfg_voiceRate = 0.2f;

	@SidedProxy(clientSide = "net.blacklab.lmr.network.ProxyClient", serverSide = "net.blacklab.lmr.network.ProxyCommon")
	public static ProxyCommon proxy;

	@Instance(DOMAIN)
	public static LittleMaidReengaged instance;

	//Logger
	private static Logger logger;
	// Item
	public static ItemMaidSpawnEgg spawnEgg;
	public static ItemTriggerRegisterKey registerKey;
	public static ItemMaidPorter maidPorter;

	public static void Debug(String pText, Object... pVals) {
		// デバッグメッセージ
		if (cfg_PrintDebugMessage || DevMode.DEVELOPMENT_DEBUG_MODE) {
			System.out.println(String.format("littleMaidMob-" + pText, pVals));
		}
	}

	public static void Debug(boolean isRemote, String format, Object... pVals) {
		Debug("Side=%s; ".concat(format), isRemote, pVals);
	}

	public String getName() {
		return "LittleMaidReengaged";
	}

	public static void log(Level level, String format, Object... data) {
		if (logger != null) {
			logger.log(level, String.format(format, data));
		}
		else {
			throw new IllegalStateException("YOU MUST USE Logger AFTER FMLPreInitialization!");
		}
	}

	public static Random randomSoundChance;

	@EventHandler
	public void preInit(FMLPreInitializationEvent evt) {
		// MMMLibからの引継ぎ
		// ClassLoaderを初期化

		// Find classpath dir
		logger = evt.getModLog();
		String classpath = System.getProperty("java.class.path");
		String separator = System.getProperty("path.separator");

		for (String path :
				classpath.split(separator)) {
			File pathFile = new File(path);
			if (pathFile.isDirectory()) {
				FileList.dirClasspath.add(pathFile);
			}
		}

		StabilizerManager.init();

		// テクスチャパックの構築
		ModelManager.instance.init();
		ModelManager.instance.loadTextures();
		// ロード
		if (CommonHelper.isClient) {
			// テクスチャパックの構築
//			MMM_TextureManager.loadTextures();
//			MMM_StabilizerManager.loadStabilizer();
			// テクスチャインデックスの構築
			Debug("Localmode: InitTextureList.");
			ModelManager.instance.initTextureList(true);
		} else {
			ModelManager.instance.loadTextureServer();
		}

		// FileManager.setSrcPath(evt.getSourceFile());
		// MMM_cfg_init();

		// MMMLibのRevisionチェック
		// MMM_Helper.checkRevision("6");
		// MMM_cfg_checkConfig(this.getClass());

		randomSoundChance = new Random();

		// Config
		Configuration cfg = new Configuration(evt.getSuggestedConfigurationFile());
		cfg.load();

		cfg_canDespawn = cfg.getBoolean("canDespawn", "General", false,
				"Set whether non-contracted maids can despawn.");
		cfg_DeathMessage = cfg.getBoolean("deathMessage", "General", true,
				"Set whether prints death message of maids.");
		cfg_Dominant = cfg.getBoolean("Dominant", "Advanced", false,
				"Recommended to keep 'false'. If true, non-vanilla check is used for maid spawning.");
		cfg_maxGroupSize = cfg.getInt("maxGroupSize", "Advanced", 3, 1, 20,
				"Settings for maid spawning. Recommended to keep default.");
		cfg_minGroupSize = cfg.getInt("minGroupSize", "Advanced", 1, 1, 20,
				"Settings for maid spawning. Recommended to keep default.");
		cfg_spawnLimit = cfg.getInt("spawnLimit", "Advanced", 20, 1, 30,
				"Settings for maid spawning. Recommended to keep default.");
		cfg_spawnWeight = cfg.getInt("spawnWeight", "Advanced", 5, 1, 9,
				"Settings for maid spawning. Recommended to keep default.");
		cfg_PrintDebugMessage = cfg.getBoolean("PrintDebugMessage", "Advanced", false,
				"Print debug logs. Recommended to keep default.");
		cfg_isModelAlphaBlend = cfg.getBoolean("isModelAlphaBlend", "Advanced", true,
				"If your graphics SHOULD be too powerless to draw alpha-blend textures, turn this 'false'.");
		cfg_isFixedWildMaid = cfg.getBoolean("isFixedWildMaid", "General", false,
				"If 'true', only default-texture maid spawns. You can still change their textures after employing.");

		cfg.save();

//		latestVersion = Version.getLatestVersion("http://mc.el-blacklab.net/lmmnxversion.txt", 10000);

		EntityRegistry.registerModEntity(new ResourceLocation(DOMAIN, "LittleMaid"), EntityLittleMaid.class,
				"LittleMaid", 0, instance, 80, 1, true);

		spawnEgg = new ItemMaidSpawnEgg();
		ForgeRegistries.ITEMS.register(spawnEgg);
		GameRegistry.addShapedRecipe(
				new ResourceLocation(DOMAIN, "spawn_littlemaid_egg"),
				null,
				new ItemStack(spawnEgg, 1),
				"scs", "sbs", " e ", 's',
				Items.SUGAR, 'c',
				new ItemStack(Items.DYE, 1, 3),
				'b', Items.SLIME_BALL,
				'e', Items.EGG);

		registerKey = new ItemTriggerRegisterKey();
		ForgeRegistries.ITEMS.register(registerKey);

		GameRegistry.addShapelessRecipe(
			new ResourceLocation(DOMAIN, "registerkey"),
			null,
			new ItemStack(registerKey),
			Ingredient.fromItem(Items.EGG),
			Ingredient.fromItem(Items.SUGAR),
			Ingredient.fromItem(Items.NETHER_WART)
		);

		maidPorter = new ItemMaidPorter();
		ForgeRegistries.ITEMS.register(maidPorter);
		
		// AIリストの追加
		EntityModeManager.init();

		// アイテムスロット更新用のパケット
		LMRNetwork.init(DOMAIN);

		// Register model and renderer
		proxy.rendererRegister();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.loadSounds();

		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());

		if (CommonHelper.isClient) {
			initClient();
		}

		MinecraftForge.EVENT_BUS.register(new EventHookLMRE());
		VEventBus.instance.registerListener(new EventHookLMRE());
	}

	@SideOnly(Side.CLIENT)
	private void initClient() {
		SimpleReloadableResourceManager resourceManager = ((SimpleReloadableResourceManager) Minecraft.getMinecraft().getResourceManager());
		resourceManager.reloadResourcePack(new SoundResourcePack());
		resourceManager.reloadResourcePack(new OldZipTexturesWrapper());
		Minecraft.getMinecraft().getSoundHandler().onResourceManagerReload(resourceManager);
	}

	// public static ProxyClient.CountThread countThread;

	@EventHandler
	public void postInit(FMLPostInitializationEvent evt) {
		// カンマ区切りのアイテム名のリストを配列にして設定
		// "aaa, bbb,ccc  " -> "aaa" "bbb" "ccc"

		// デフォルトモデルの設定
		// MMM_TextureManager.instance.setDefaultTexture(LMM_EntityLittleMaid.class,
		// MMM_TextureManager.instance.getTextureBox("default_Orign"));

		if (cfg_spawnWeight > 0) {
			for (Biome biome : Biome.REGISTRY) {
				if (biome != null &&
						(
								(BiomeDictionary.hasType(biome, BiomeDictionary.Type.HOT) ||
//										BiomeDictionary.hasType(biome, BiomeDictionary.Type.COLD) ||
										BiomeDictionary.hasType(biome, BiomeDictionary.Type.WET) ||
										BiomeDictionary.hasType(biome, BiomeDictionary.Type.DRY) ||
										BiomeDictionary.hasType(biome, BiomeDictionary.Type.SAVANNA) ||
										BiomeDictionary.hasType(biome, BiomeDictionary.Type.CONIFEROUS) ||
//										BiomeDictionary.hasType(biome, BiomeDictionary.Type.LUSH) ||
										BiomeDictionary.hasType(biome, BiomeDictionary.Type.MUSHROOM) ||
										BiomeDictionary.hasType(biome, BiomeDictionary.Type.FOREST) ||
										BiomeDictionary.hasType(biome, BiomeDictionary.Type.PLAINS) ||
										BiomeDictionary.hasType(biome, BiomeDictionary.Type.SANDY) ||
//										BiomeDictionary.hasType(biome, BiomeDictionary.Type.SNOWY) ||
										BiomeDictionary.hasType(biome, BiomeDictionary.Type.BEACH))
						)
				) {
					EntityRegistry.addSpawn(EntityLittleMaid.class, cfg_spawnWeight, cfg_minGroupSize, cfg_maxGroupSize, EnumCreatureType.CREATURE, biome);
					//System.out.println("Registering spawn in " + biome.toString());
					//Debug("Registering maids to spawn in " + biome.toString());
				}
			}
		}

		// モードリストを構築
//		EntityModeManager.loadEntityMode();
//		EntityModeManager.showLoadedModes();
		LoaderSearcher.INSTANCE.register(EntityModeHandler.class);
		LoaderSearcher.INSTANCE.startSearch();

		// サウンドのロード
		// TODO ★ proxy.loadSounds();

		// IFFのロード
		IFF.loadIFFs();

	}

}
