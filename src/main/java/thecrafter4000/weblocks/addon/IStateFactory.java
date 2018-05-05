package thecrafter4000.weblocks.addon;

import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.world.registry.State;

import net.minecraft.block.Block;

/**
 * Main interface for use with other mods. 
 * @author TheCrafter4000
 */
public interface IStateFactory {

	/** Returns true if this factory is usable on the given block */
	public boolean canCreateState(BaseBlock bukkitblock, Block forgeblock);

	/** Returns whether blocks that already got states (from the config for example) should be edited by this factory */
	public boolean shouldAlwaysAdd();
	
	/**
	 * Returns the matching state
	 * @param bukkitblock The WorldEdit block. Holds TE data.
	 * @param forgeblock The block.
	 * @return a working state, or null if an error occurred.
	 */
	public State create(BaseBlock bukkitblock, Block forgeblock);
}
