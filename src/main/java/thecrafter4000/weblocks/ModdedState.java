package thecrafter4000.weblocks;

import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.world.registry.State;
import com.sk89q.worldedit.world.registry.StateValue;

import thecrafter4000.weblocks.addon.carpenters.CarpentersStateValue;
import thecrafter4000.weblocks.addon.carpenters.CarpentersUtils;

/**
 * Clone of {@link com.sk89q.worldedit.world.registry.SimpleState}
 */
public class ModdedState implements State {

	private Byte dataMask;
	private Map<String, ModdedStateValue> values; // String not necessary, possible old code copied into 1.7.10

	public ModdedState(Byte dataMask, ModdedStateValue[] values) {
		this(dataMask, CarpentersUtils.toImmutableMap(values));
	}

	public ModdedState(Byte dataMask, Map<String, ModdedStateValue> values) {
		this.values = ImmutableMap.copyOf(values);
		this.dataMask = dataMask;

		for (ModdedStateValue v : values.values()) {
			v.setState(this);
		}
	}

	@Override
	public Map<String, ModdedStateValue> valueMap() {
		return values;
	}

	@Nullable
	@Override
	public StateValue getValue(BaseBlock block) {
		for (StateValue value : values.values()) {
			if (value.isSet(block)) {
				return value;
			}
		}

		return null;
	}

	public byte getDataMask() {
		return dataMask != null ? dataMask : 0xF;
	}

	@Override
	public boolean hasDirection() {
		for (ModdedStateValue value : values.values()) {
			if (value.getDirection() != null) {
				return true;
			}
		}

		return false;
	}
}
