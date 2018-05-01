package thecrafter4000.weblocks;

import java.io.File;
import java.util.Map;

import com.carpentersblocks.block.BlockCarpentersStairs;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.util.gson.VectorAdapter;
import com.sk89q.worldedit.world.registry.LegacyBlockRegistry;
import com.sk89q.worldedit.world.registry.State;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;

/**
 * A block registry using unlocalized names instead of numeric id's.
 * @author TheCrafter4000
 */
public class ModdedBlockRegistry extends LegacyBlockRegistry {
	
	/** Internal use only */
	private static Map<String, Map<String, State>> registry = Maps.newHashMap();
	
	/** Registers a list of states. */
	public static void register(Block block, Map<String, State> values) {
		registry.put(block.getUnlocalizedName().substring(5), values);
	}
	
	//TODO: Make the config work
	protected static void load(File dir) {
		File file = new File(dir.getAbsoluteFile() + "/worldedit/ModdedBlocks.json");
		if(!file.exists()) {
			try {
				file.createNewFile();
			}catch(Exception e) {
				WEBlocks.Logger.catching(e);
			}
		}
		
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Vector.class, new VectorAdapter());
        Gson gson = gsonBuilder.create();

        try {
        	registry = gson.fromJson(file.getAbsolutePath(), new TypeToken<Map<String, Map<String, State>>>() {}.getType());
        }catch(Exception e) {
        	WEBlocks.Logger.fatal("Invalid configuration!");
 //       	registry = Maps.newHashMap();
        	e.printStackTrace();
        }
	}
	
	/** Registers the "facing" state */
	public static void registerFacing(Block block, State state) {
		Map<String, State> map = Maps.newHashMap();
		map.put("facing", state);
		register(block, map);
	}
	
	/** Get's all registered states */
	public static Map<String, State> getModdedStates(BaseBlock bukkitblock){
		Block block = Block.getBlockById(bukkitblock.getId());
		Map<String, State> states = registry.get(toUnlcStr(block));
		if(states == null) { // Default values
			states = Maps.newHashMap();
			if(block instanceof BlockStairs) { // Copied from OAK_STAIRS
				Map<String, ModdedStateValue> values = Maps.newHashMap();
				values.put("east", new ModdedStateValue((byte) 4, new Vector(1, 1, 0)));
				values.put("west", new ModdedStateValue((byte) 5, new Vector(-1, 1, 0)));
				values.put("north", new ModdedStateValue((byte) 7, new Vector(0, 1, -1)));
				values.put("south", new ModdedStateValue((byte) 6, new Vector(0, 1, 1)));
				values.put("east_upsidedown", new ModdedStateValue((byte) 0, new Vector(1, -1, 0)));
				values.put("west_upsidedown", new ModdedStateValue((byte) 1, new Vector(-1, -1, 0)));
				values.put("north_upsidedown", new ModdedStateValue((byte) 3, new Vector(0, -1, -1)));
				values.put("south_upsidedown", new ModdedStateValue((byte) 2, new Vector(0, -1, 1)));
				states.put("facing", new ModdedState((byte) 7, values));
				
				registry.put(toUnlcStr(block), states); // Saves created states for performance reasons.
//				System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(registry));
			}else if(block instanceof BlockCarpentersStairs) { //TODO: Make carpenters support a module
				states.put("facing", CarpentersState.getByBlock(bukkitblock));
			}
		}

		return states;
	}
	
	@Override
	public Map<String, ? extends State> getStates(BaseBlock block) {
		Map<String, ? extends State> states = super.getStates(block);
		if(states == null) {
			states = getModdedStates(block);
		}
//		System.out.println(toUnlcStr(Block.getBlockById(block.getId())));
//		states.entrySet().forEach(o -> o.getValue().valueMap().entrySet().forEach( e -> System.out.println("Key1: " + o.getKey() + ", Key2: " + e.getKey() + ", Value: " + e.getValue().getDirection())));
		return states;
	}
	
	/** Converts a block to an registry key string */
	private static String toUnlcStr(Block b) {
		return b.getUnlocalizedName().substring(5);
	}
}
