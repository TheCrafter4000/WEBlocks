package thecrafter4000.weblocks.addon.carpenters;

import static thecrafter4000.weblocks.WEBlocks.toImmutableMap;
import static thecrafter4000.weblocks.addon.carpenters.CarpentersUtils.getMeta;

import java.util.Map;

import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.world.registry.State;
import com.sk89q.worldedit.world.registry.StateValue;

/** Custom implementation of {@link State} for use with CarpentersBlocks */
public class CarpentersState implements State {
	protected Map<String, CarpentersStateValue> values; // Key is an integer
	
	public CarpentersState(CarpentersStateValue[] values) {
		this.values = toImmutableMap(values);
	}
	
	/** Internal use. Key is an integer represented by an string. */
	protected CarpentersState(Map<String, CarpentersStateValue> values) {
		this.values = values;
	}
	
	@Override
	public Map<String, ? extends StateValue> valueMap() {
		return values;
	}

	@Override
	public StateValue getValue(BaseBlock block) {
		return values.get(String.valueOf(getMeta(block)));
	}

	@Override
	public boolean hasDirection() { 
		return true; 
	}
}