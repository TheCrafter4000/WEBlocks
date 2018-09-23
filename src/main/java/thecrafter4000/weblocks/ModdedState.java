package thecrafter4000.weblocks;

import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.world.registry.State;
import com.sk89q.worldedit.world.registry.StateValue;

import javax.annotation.Nullable;
import java.util.Map;

import static thecrafter4000.weblocks.WEBlocks.toImmutableMap;

/**
 * Custom {@link State} implementation.
 * @author TheCrafter4000
 */
public class ModdedState implements State {

	@Nullable
	private Byte dataMask;
	private ModdedStateValue[] values;
	
	public ModdedState(@Nullable Byte dataMask, ModdedStateValue[] values) {
		this.values = values;
		this.dataMask = dataMask;
		validate();
	}

	public void validate() {
		for (ModdedStateValue value : values) {
			value.validate(this);
		}
	}

	@Override
	public Map<String, ModdedStateValue> valueMap() { // Key (string) not used, possible old code ported into 1.7.10
		return toImmutableMap(values);
	}

	@Override
	public StateValue getValue(BaseBlock block) {
		for (ModdedStateValue value : values) {
			if (value.isSet(block)) {
				return value;
			}
		}

		return null;
	}

	/** Returns the dataMask, or 0xF if not defined. */
	public byte getDataMask() {
		return dataMask != null ? dataMask : 0xF;
	}

	@Override
	public boolean hasDirection() {
		for (ModdedStateValue value : values) {
			if (value.getDirection() != null) {
				return true;
			}
		}

		return false;
	}
}
