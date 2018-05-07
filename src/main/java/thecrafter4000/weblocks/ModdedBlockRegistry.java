package thecrafter4000.weblocks;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.util.gson.VectorAdapter;
import com.sk89q.worldedit.world.registry.LegacyBlockRegistry;
import com.sk89q.worldedit.world.registry.State;

import net.minecraft.block.Block;
import thecrafter4000.weblocks.addon.IStateFactory;

/**
 * A block registry using unlocalized names instead of numeric id's.
 * @author TheCrafter4000
 */
public class ModdedBlockRegistry extends LegacyBlockRegistry {
	
	public static final ModdedBlockRegistry INSTANCE = new ModdedBlockRegistry();
	
	private ModdedBlockRegistry() {}
	
	/** Holds all states. Key is the unlocalized name of the block */
	private static final Map<String, List<State>> registry = Maps.newHashMap();
	/** Holds all {@link IStateFactory} instances */
	private static final List<IStateFactory> factories = Lists.newArrayList();
	
	/** Registers a list of states. */
	public static void register(Block block, List<State> values) {
		String key = toUnlcStr(block);
		if(registry.get(key) != null) {
			registry.get(key).addAll(values);
		}else registry.put(key, new ArrayList<State>(values));
	}
	
	/** Registers a state */
	public static void register(Block block, State state) {
		register(block, ImmutableList.of(state));
	}
	
	/** Registers an state factory. Use this to handle more complicated stuff. */
	public static void registerFactory(IStateFactory instance) {
		factories.add(instance);
	}
	
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
 //       	registry = gson.fromJson(file.getAbsolutePath(), new TypeToken<Map<String, Map<String, State>>>() {}.getType());
        }catch(Exception e) {
        	WEBlocks.Logger.fatal("Invalid configuration!");
 //       	registry = Maps.newHashMap();
        	e.printStackTrace();
        }
	}
	
	/** Get's all registered states */
	public static List<State> getModdedStates(BaseBlock bukkitblock){
		Block block = Block.getBlockById(bukkitblock.getId());
		List<State> states = registry.get(toUnlcStr(block));
		boolean isEmpty = false;
		if(states == null || states.isEmpty()) { 
			states = new ArrayList<State>();
			isEmpty = true;
		}
		
		for(IStateFactory factory : factories) {
			if(factory.canCreateState(bukkitblock, block) && (isEmpty || factory.shouldAlwaysAdd())) {
				states.add(factory.create(bukkitblock, block));
			}
		}

		return states;
	}
	
	@Override
	public Map<String, ? extends State> getStates(BaseBlock block) {
		Map<String, State> states = (Map<String, State>) super.getStates(block);
		if(states == null || states.isEmpty()) {
			states = Maps.newHashMap();
			int i = 0;
			for(State t : getModdedStates(block)) {
				states.put("key" + ++i, t);
			}
		}
//		System.err.println(states);
//		System.out.println(toUnlcStr(Block.getBlockById(block.getId())));
//		states.entrySet().forEach(o -> o.getValue().valueMap().entrySet().forEach( e -> System.out.println("Key1: " + o.getKey() + ", Key2: " + e.getKey() + ", Value: " + e.getValue().getDirection())));
		return states;
	}
	
	/** Converts a block to an registry key string */
	private static String toUnlcStr(Block b) {
		return b.getUnlocalizedName().substring(5);
	}
}
