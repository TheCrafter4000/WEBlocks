package thecrafter4000.weblocks;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.world.registry.StateValue;

/**
 * Clone of {@link com.sk89q.worldedit.world.registry.SimpleStateValue}
 */
public class ModdedStateValue implements StateValue {

    private ModdedState state;
    private Byte data;
    private Vector direction;
    
    public ModdedStateValue(Byte data, Vector direction) {
		this.data = data;
		this.direction = direction;
	}

	void setState(ModdedState state) {
        this.state = state;
    }

    @Override
    public boolean isSet(BaseBlock block) {
        return data != null && (block.getData() & state.getDataMask()) == data;
    }

    @Override
    public boolean set(BaseBlock block) {
        if (data != null) {
            block.setData((block.getData() & ~state.getDataMask()) | data);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Vector getDirection() {
        return direction;
    }
}
