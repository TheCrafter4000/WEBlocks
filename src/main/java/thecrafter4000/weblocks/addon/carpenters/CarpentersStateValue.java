package thecrafter4000.weblocks.addon.carpenters;

import static thecrafter4000.weblocks.addon.carpenters.CarpentersUtils.getMeta;
import static thecrafter4000.weblocks.addon.carpenters.CarpentersUtils.toVector;

import java.util.List;

import com.carpentersblocks.tileentity.TEBase;
import com.sk89q.jnbt.CompoundTagBuilder;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.world.registry.StateValue;

import net.minecraftforge.common.util.ForgeDirection;

/** Custom implementation of {@link StateValue} for use with CarpentersBlocks */
public class CarpentersStateValue implements StateValue {

	protected Vector direction;
	protected byte id;

	public CarpentersStateValue(int meta, ForgeDirection... directions) {
		this.direction = toVector(directions);
		this.id = (byte) meta;
	}

	public CarpentersStateValue(int meta, List<ForgeDirection> directions) {
		this.direction = toVector(directions);
		this.id = (byte) meta;
	}

	@Override
	public boolean isSet(BaseBlock block) {
		return id == getMeta(block);
	}

	@Override
	public boolean set(BaseBlock block) {
		CompoundTagBuilder b = block.getNbtData().createBuilder();
		b.putInt(TEBase.TAG_METADATA, id);
		block.setNbtData(b.build());
		return true;
	}

	@Override
	public Vector getDirection() {
		return direction;
	}
}