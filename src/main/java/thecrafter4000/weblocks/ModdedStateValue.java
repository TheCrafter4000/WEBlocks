package thecrafter4000.weblocks;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.world.registry.StateValue;

/**
 * Custom {@link StateValue} implementation.
 * @author TheCrafter4000
 */
public class ModdedStateValue implements StateValue {

	private transient byte dataMask;
	private byte meta;
	private Vector direction;

	public ModdedStateValue(byte data, Vector direction) {
		this.meta = data;
		this.direction = direction;
	}

	public ModdedStateValue(int data, Vector direction) {
		this((byte) data, direction);
	}

	/** Get's the dataMask from parent */
	void validate(ModdedState moddedState) {
		dataMask = moddedState.getDataMask();
	}

	@Override
	public boolean isSet(BaseBlock block) {
		return (block.getData() & dataMask) == meta;
	}

	@Override
	public boolean set(BaseBlock block) {
		block.setData((block.getData() & ~dataMask) | meta);
		return true;
	}

	@Override
	public Vector getDirection() {
		return direction;
	}
}