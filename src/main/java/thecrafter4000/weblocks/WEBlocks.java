package thecrafter4000.weblocks;

import java.lang.reflect.Field;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.forge.ForgeWorld;
import com.sk89q.worldedit.world.registry.LegacyBlockRegistry;
import com.sk89q.worldedit.world.registry.LegacyWorldData;
import com.sk89q.worldedit.world.registry.State;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;


/**
 * WEBlocks, a mod by TheCrafter4000.
 * @author TheCrafter4000
 */
@Mod(modid = WEBlocks.MODID, version = WEBlocks.VERSION, name = WEBlocks.NAME, acceptableRemoteVersions = "*")
public class WEBlocks {
	public static final String MODID = "weblocks";
	public static final String VERSION = "1.0";
	public static final String NAME = "WEBlocks";
		
	@Instance
	public static WEBlocks Instance = new WEBlocks();
	public static Logger Logger = LogManager.getLogger(NAME);
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		//TODO: Read config
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		inject(LegacyWorldData.class.getName(), "INSTANCE", "blockRegistry");
		inject("com.sk89q.worldedit.forge.ForgeWorldData", "INSTANCE", "blockRegistry");
		//TODO: Add a way to do this via config.
	}
	
	/**
	 * Injects a modified block registry into an {@link LegacyWorldData} instance.
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
			f.set(instance, new ModdedBlockRegistry());
			Logger.info("Successfully replaced blockRegistry for " + clazz + "!");
		}catch(NoSuchFieldException e) {
			Logger.fatal("Did not resolve blockRegistry field " + registryField + " in " + clazz + "!", e);
		}catch (IllegalArgumentException | IllegalAccessException e) {
			Logger.fatal("Failed to overwrite blockRegistry for " + clazz + "!", e);
		}
	}
}
