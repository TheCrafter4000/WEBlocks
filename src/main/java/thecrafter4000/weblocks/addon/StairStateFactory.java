package thecrafter4000.weblocks.addon;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.world.registry.State;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import thecrafter4000.weblocks.ModdedState;
import thecrafter4000.weblocks.ModdedStateValue;

/** Example implementation of {@link IStateFactory}. Add's stair rotation data for modded blocks. */
public class StairStateFactory implements IStateFactory {

	@Override
	public boolean canCreateState(BaseBlock bukkitblock, Block forgeblock) {
		return forgeblock instanceof BlockStairs;
	}
	
	@Override
	public boolean shouldAlwaysAdd() {
		return false; // We don't want to add stuff twice here, since the configuration already contains these information for vanilla blocks.
	}

	@Override
	public State create(BaseBlock bukkitblock, Block forgeblock) {
		return new ModdedState((byte) 7, new ModdedStateValue[] { // Literal copy of the WE configuration file.
				new ModdedStateValue(0, new Vector(1, -1, 0)), // east_upsidedown
				new ModdedStateValue(1, new Vector(-1, -1, 0)),// west_upsidedown
				new ModdedStateValue(2, new Vector(0, -1, 1)), // south_upsidedown
				new ModdedStateValue(3, new Vector(0, -1, -1)),// north_upsidedown
				new ModdedStateValue(4, new Vector(1, 1, 0)),  // east
				new ModdedStateValue(5, new Vector(-1, 1, 0)), // west
				new ModdedStateValue(6, new Vector(0, 1, 1)),  // south
				new ModdedStateValue(7, new Vector(0, 1, -1))  // north
			});
	}
}