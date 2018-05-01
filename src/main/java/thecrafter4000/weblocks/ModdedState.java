package thecrafter4000.weblocks;

import java.util.Collections;
import java.util.Map;

import javax.annotation.Nullable;

import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.world.registry.State;
import com.sk89q.worldedit.world.registry.StateValue;


/**
 * Clone of {@link com.sk89q.worldedit.world.registry.SimpleState}
 */
public class ModdedState implements State {
	
    private Byte dataMask;
    private Map<String, ModdedStateValue> values; //TODO: Remove these unnecessary strings, make it a list.
    
    public ModdedState(Byte dataMask, Map<String, ModdedStateValue> values) {
		this.dataMask = dataMask;
		this.values = values;
		postDeserialization();
	}

	@Override
    public Map<String, ModdedStateValue> valueMap() {
        return Collections.unmodifiableMap(values);
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

    void postDeserialization() {
        for (ModdedStateValue v : values.values()) {
            v.setState(this);
        }
    }

}
