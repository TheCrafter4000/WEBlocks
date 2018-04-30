package thecrafter4000.weblocks;

import java.util.Map;

import com.google.common.collect.Maps;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.blocks.BlockMaterial;
import com.sk89q.worldedit.world.registry.LegacyBlockRegistry;
import com.sk89q.worldedit.world.registry.State;
import com.sk89q.worldedit.world.registry.StateValue;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;

/**
 * A block registry using unlocalized names instead of numeric id's.
 * @author TheCrafter4000
 */
public class ModdedBlockRegistry extends LegacyBlockRegistry {
	
	private static final Map<String, Map<String, ModdedState>> registry = Maps.newHashMap();
	//TODO: Use a pattern for key, not just getUnlocalizedName()
	
	public static void register(String unlocalizedname, Map<String, ModdedState> values) {
		registry.put(unlocalizedname, values);
	}
	
	public static Map<String, ModdedState> getStates(Block block){
		Map<String, ModdedState> map = registry.get(block.getUnlocalizedName());
		if(map == null) {
			if(block instanceof BlockStairs) {
				Map<String, ModdedStateValue> values = Maps.newHashMap();
				values.put("east", new ModdedStateValue((byte) 4, new Vector(1, 1, 0)));
				values.put("west", new ModdedStateValue((byte) 5, new Vector(-1, 1, 0)));
				values.put("north", new ModdedStateValue((byte) 7, new Vector(0, 1, -1)));
				values.put("south", new ModdedStateValue((byte) 6, new Vector(0, 1, 1)));
				values.put("east_upsidedown", new ModdedStateValue((byte) 0, new Vector(1, -1, 0)));
				values.put("west_upsidedown", new ModdedStateValue((byte) 1, new Vector(-1, -1, 0)));
				values.put("north_upsidedown", new ModdedStateValue((byte) 3, new Vector(0, -1, -1)));
				values.put("south_upsidedown", new ModdedStateValue((byte) 2, new Vector(0, -1, 1)));
				map = Maps.newHashMap();
				map.put("facing", new ModdedState((byte) 7, values));
			}
			//TODO: Add more blocks, maybe ladders and stuff.
			if(map != null) {
				//TODO: Save to config maybe...
				registry.put(block.getUnlocalizedName(), map); // Saves created states for performance reasons.
			}
		}
		return map;
	}
	
	@Override
	public Map<String, ? extends State> getStates(BaseBlock block) {
		Map<String, ? extends State> states = super.getStates(block);
		if(states == null) {
			states = getStates(Block.getBlockById(block.getId()));
		}
//		states.entrySet().forEach(o -> o.getValue().valueMap().entrySet().forEach( e -> System.out.println("Key1: " + o.getKey() + ", Key2: " + e.getKey() + ", Value: " + e.getValue().getDirection())));
//		System.out.println();
		return states;
	}
}
