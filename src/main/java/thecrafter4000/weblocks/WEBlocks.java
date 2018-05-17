package thecrafter4000.weblocks;

import static com.google.common.base.Preconditions.checkNotNull;

import java.lang.reflect.Field;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.sk89q.worldedit.world.registry.LegacyWorldData;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import thecrafter4000.weblocks.addon.StairStateFactory;
import thecrafter4000.weblocks.addon.carpenters.CarpentersSlabStateFactory;
import thecrafter4000.weblocks.addon.carpenters.CarpentersStairStateFactory;

/**
 * WEBlocks, a WorldEdit fix to support other mod's blocks for rotating operations.
 * @author TheCrafter4000
 */
@Mod(modid = WEBlocks.MODID, version = WEBlocks.VERSION, name = WEBlocks.NAME, acceptableRemoteVersions = "*")
public class WEBlocks {
	public static final String MODID = "weblocks";
	public static final String VERSION = "1.2.0";
	public static final String NAME = "WEBlocks";
		
	@Instance
	public static WEBlocks Instance = new WEBlocks();
	public static Logger Logger = LogManager.getLogger(NAME);
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		ModdedBlockRegistry.load(event.getModConfigurationDirectory());
		ModdedBlockRegistry.registerFactory(new StairStateFactory());
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		inject(LegacyWorldData.class.getName(), "INSTANCE", "blockRegistry");
		inject("com.sk89q.worldedit.forge.ForgeWorldData", "INSTANCE", "blockRegistry");
		
		if(Loader.isModLoaded("CarpentersBlocks")) {
			Logger.info("Enabled Carpenter's Blocks support!");
			ModdedBlockRegistry.registerFactory(new CarpentersStairStateFactory());
			ModdedBlockRegistry.registerFactory(new CarpentersSlabStateFactory());
			//TODO: Make more factories
			//TODO: Add layer rotation support.
		}
	}
	
	/**
	 * Injects a modified block registry into an {@link LegacyWorldData} instance field.
	 * @param clazz The class you want to inject in. Must be a subclass of {@link {@link LegacyWorldData}
	 * @param instanceField Name of the instance field. Mostly "INSTANCE".
	 * @param registryField Name of the block registry field. Mostly "blockRegistry".
	 */
	public static void inject(String clazz, String instanceField, String registryField) {
		Object instance = null;
		
		try {
			Class c = Class.forName(clazz); // Loads class
			Field f = c.getDeclaredField(instanceField);
			f.setAccessible(true);
			instance = f.get(null); // Creating instance
		} catch (NoSuchFieldException e) {
			Logger.fatal("Did not resolve instance " + clazz + "." + instanceField + "!", e);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			Logger.fatal("Failed to get " + clazz + " instance!", e);
		} catch (ClassNotFoundException e) {
			Logger.fatal("Failed to load class: " + clazz + "!", e);
		} 
		
		if(instance == null) return;
		
		try {
			Field f = LegacyWorldData.class.getDeclaredField(registryField);
			f.setAccessible(true);
			f.set(instance, ModdedBlockRegistry.INSTANCE);
			Logger.debug("Successfully replaced blockRegistry for " + clazz + "!");
		}catch(NoSuchFieldException e) {
			Logger.fatal("Did not resolve blockRegistry field " + registryField + " in " + clazz + "!", e);
		}catch (IllegalArgumentException | IllegalAccessException e) {
			Logger.fatal("Failed to overwrite blockRegistry for " + clazz + "!", e);
		}
	}
	
	/** Copies array data into an map. The {@code key} is the String representation of the index, using {@link String#valueOf(int)}. */
	public static <V> Map<String, V> toImmutableMap(V[] data){
		checkNotNull(data);
		Builder<String, V> builder = ImmutableMap.builder();
		for(int i = 0; i < data.length; i++) {
			builder.put(String.valueOf(i), data[i]);
		}
		return builder.build();
	}
}
