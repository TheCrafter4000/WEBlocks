package thecrafter4000.weblocks;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.util.gson.VectorAdapter;
import com.sk89q.worldedit.world.registry.LegacyBlockRegistry;
import com.sk89q.worldedit.world.registry.State;
import net.minecraft.block.Block;
import thecrafter4000.weblocks.addon.IStateFactory;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static thecrafter4000.weblocks.WEBlocks.toImmutableMap;

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
		}else registry.put(key, new ArrayList<>(values));
	}
	
	/** Registers a state */
	public static void register(Block block, State state) {
		register(block, Lists.newArrayList(state));
	}
	
	/** Registers an state factory. Use this to handle more complicated stuff. */
	public static void registerFactory(IStateFactory instance) {
		factories.add(instance);
	}
		
	protected static void load(File dir) {
		File file = new File(dir.getAbsoluteFile() + "/worldedit/ModdedBlocks.json"); // Config file
		Type type = new TypeToken<Map<String[], List<ModdedState>>>(){}.getType(); // Config type
		Map<String[], List<State>> loaded = new HashMap<>();

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Vector.class, new VectorAdapter());
		builder.enableComplexMapKeySerialization();
        builder.setPrettyPrinting();
        Gson gson = builder.create();

		if(!file.exists()) {
			try {
				file.createNewFile();
				FileWriter writer = new FileWriter(file);
				gson.toJson(loaded, type, writer); // The map is empty at this point.
				writer.close();
				WEBlocks.Logger.info("Did not resolve configuration file. Created a new one at " + file.getAbsolutePath() + "!");
			}catch(Exception e) {
				WEBlocks.Logger.catching(e);
			}
		}
        
        try {
        	FileReader reader = new FileReader(file);

        	loaded = gson.fromJson(reader, type);
        	loaded.forEach((unlocalizedNames, moddedStates) -> {
				for (State state : moddedStates) {
					((ModdedState)state).validate();
				}

        		for(String name : unlocalizedNames) {
					registry.put(name, moddedStates);
				}
			});

        	reader.close();
        	WEBlocks.Logger.info("Successfully loaded config.");
        }catch(Exception e) {
        	WEBlocks.Logger.fatal("Invalid configuration! Skipping loading. File path: " + file.getAbsolutePath(), e);
        }
	}
	
	@Override
	public Map<String, ? extends State> getStates(BaseBlock block) {
		Map<String, State> states = (Map<String, State>) super.getStates(block); // Get's WE entries.
		if(states == null) { // Creates new map if no results we're found by super call.
			states = Maps.newHashMap();
		}
		
		states.putAll(toImmutableMap(getModdedStates(block, states.isEmpty()).toArray(new State[0])));
		
		return states;
	}
	
	/** Get's all injected registered states. */
	private static List<State> getModdedStates(BaseBlock bukkitblock, boolean isEmpty){
		Block block = Block.getBlockById(bukkitblock.getId()); // Getting forge block
		List<State> states = registry.get(toUnlcStr(block)); // Getting stored entries.
		
		if(states == null) { // Null-safety
			states = new ArrayList<>();
		}else if(!states.isEmpty() && isEmpty) { // If we found nothing with WE, but in our own registry;
			isEmpty = false;
		}
		
		for(IStateFactory factory : factories) {
			if(factory.canCreateState(bukkitblock, block) && (isEmpty || factory.shouldAlwaysAdd())) {
				states.add(factory.create(bukkitblock, block));
			}
		}

		return states;
	}
	
	/** Converts a block to an registry key string */
	private static String toUnlcStr(Block b) {
		return b.getUnlocalizedName().substring(5);
	}
}
