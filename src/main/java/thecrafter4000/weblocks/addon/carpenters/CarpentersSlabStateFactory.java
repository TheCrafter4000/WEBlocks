package thecrafter4000.weblocks.addon.carpenters;

import static thecrafter4000.weblocks.addon.carpenters.CarpentersUtils.getMeta;

import com.carpentersblocks.block.BlockCarpentersBlock;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.world.registry.State;

import net.minecraft.block.Block;
import net.minecraftforge.common.util.ForgeDirection;
import thecrafter4000.weblocks.addon.IStateFactory;

public class CarpentersSlabStateFactory implements IStateFactory {
	
	private final CarpentersStateValue[] values = new CarpentersStateValue[] {
			new CarpentersStateValue(0, ForgeDirection.UNKNOWN),
			new CarpentersStateValue(1, ForgeDirection.WEST),
			new CarpentersStateValue(2, ForgeDirection.EAST),
			new CarpentersStateValue(3, ForgeDirection.DOWN),
			new CarpentersStateValue(4, ForgeDirection.UP),
			new CarpentersStateValue(5, ForgeDirection.NORTH),
			new CarpentersStateValue(6, ForgeDirection.SOUTH)};
	
	private final CarpentersState instance = new CarpentersState(values);
	
	@Override
	public boolean canCreateState(BaseBlock bukkitblock, Block forgeblock) {
		return forgeblock instanceof BlockCarpentersBlock;
	}

	@Override
	public boolean shouldAlwaysAdd() {
		return true;
	}

	@Override
	public State create(BaseBlock bukkitblock, Block forgeblock) {
		int meta = getMeta(bukkitblock);
		return instance;
	}
}