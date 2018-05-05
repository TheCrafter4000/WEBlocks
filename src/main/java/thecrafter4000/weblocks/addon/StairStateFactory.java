package thecrafter4000.weblocks.addon;

import java.util.Map;

import com.google.common.collect.Maps;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.world.registry.State;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import thecrafter4000.weblocks.ModdedState;
import thecrafter4000.weblocks.ModdedStateValue;

/** Example implementation of {@link IStateFactory} for stair rotation */
public class StairStateFactory implements IStateFactory {

	@Override
	public boolean canCreateState(BaseBlock bukkitblock, Block forgeblock) {
		return forgeblock instanceof BlockStairs;
	}
	
	@Override
	public boolean shouldAlwaysAdd() {
		return false; // We don't want to add stuff twice here, since the config already contains these information for vanillia blocks
	}

	@Override
	public State create(BaseBlock bukkitblock, Block forgeblock) {
		Map<String, ModdedStateValue> values = Maps.newHashMap(); // Literal copy of the WE config. Took from OAK_STAIRS
		values.put("east", new ModdedStateValue((byte) 4, new Vector(1, 1, 0)));
		values.put("west", new ModdedStateValue((byte) 5, new Vector(-1, 1, 0)));
		values.put("north", new ModdedStateValue((byte) 7, new Vector(0, 1, -1)));
		values.put("south", new ModdedStateValue((byte) 6, new Vector(0, 1, 1)));
		values.put("east_upsidedown", new ModdedStateValue((byte) 0, new Vector(1, -1, 0)));
		values.put("west_upsidedown", new ModdedStateValue((byte) 1, new Vector(-1, -1, 0)));
		values.put("north_upsidedown", new ModdedStateValue((byte) 3, new Vector(0, -1, -1)));
		values.put("south_upsidedown", new ModdedStateValue((byte) 2, new Vector(0, -1, 1)));
		return new ModdedState((byte) 7, values);
	}
}