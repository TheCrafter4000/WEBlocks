package thecrafter4000.weblocks.addon.carpenters;

import static thecrafter4000.weblocks.addon.carpenters.CarpentersUtils.getMeta;

import com.carpentersblocks.block.BlockCarpentersStairs;
import com.carpentersblocks.data.Stairs;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.world.registry.State;

import net.minecraft.block.Block;
import thecrafter4000.weblocks.addon.IStateFactory;

/**
 * Carpenters Stair integration
 * @author TheCrafter4000
 */
public class CarpentersStairStateFactory implements IStateFactory {

	private CarpentersState[] data = new CarpentersState[4]; // Each Stair type got separate states containing the matching state values
	private CarpentersStateValue[] values = new CarpentersStateValue[28]; // All state values
	
	public CarpentersStairStateFactory() {
		for(int i = 0; i < Stairs.stairsList.length; i++) {
			values[i] = new CarpentersStateValue(i, Stairs.stairsList[i].facings);
		}
		for(int s = 0; s < 4; s++) { // Iterates over all 4 stair types
			Builder<String, CarpentersStateValue> builder = ImmutableMap.builder();
			for(int i = 0; i < Stairs.stairsList.length; i++) {
				if(Stairs.stairsList[i].stairsType.ordinal() == s) { // Only matching stair types are added
					builder.put(String.valueOf(i), values[i]);
				}
			}
			data[s] = new CarpentersState(builder.build());
		}
	}
	
	@Override
	public boolean canCreateState(BaseBlock bukkitblock, Block forgeblock) {
		return forgeblock instanceof BlockCarpentersStairs;
	}

	@Override
	public boolean shouldAlwaysAdd() {
		return true;
	}

	@Override
	public State create(BaseBlock bukkitblock, Block forgeblock) {
		int meta = getMeta(bukkitblock);
		Stairs.Type t = Stairs.stairsList[meta].stairsType;
		return data[t.ordinal()];
	}
}
