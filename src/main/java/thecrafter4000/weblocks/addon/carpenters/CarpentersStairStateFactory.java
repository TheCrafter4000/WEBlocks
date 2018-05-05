package thecrafter4000.weblocks.addon.carpenters;

import java.util.List;
import java.util.Map;

import com.carpentersblocks.block.BlockCarpentersStairs;
import com.carpentersblocks.data.Stairs;
import com.carpentersblocks.tileentity.TEBase;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sk89q.jnbt.CompoundTagBuilder;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.world.registry.State;
import com.sk89q.worldedit.world.registry.StateValue;

import net.minecraft.block.Block;
import net.minecraftforge.common.util.ForgeDirection;
import thecrafter4000.weblocks.WEBlocks;
import thecrafter4000.weblocks.addon.IStateFactory;

/**
 * Carpenters Stair integration
 * @author TheCrafter4000
 */
public class CarpentersStairStateFactory implements IStateFactory {

	/** Stores all states */
	private List<CarpentersState> states = Lists.newArrayList(
			new CarpentersState(Stairs.Type.NORMAL_SIDE),
			new CarpentersState(Stairs.Type.NORMAL),
			new CarpentersState(Stairs.Type.NORMAL_INT),
			new CarpentersState(Stairs.Type.NORMAL_EXT));
	
	public CarpentersStairStateFactory() {}
	
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
		
		for(CarpentersState s : states) {
			if(s.type == t) {
				return s;
			}
		};
		
		WEBlocks.Logger.fatal("Did not resolve CarpenterState for meta " + meta + "!");
		return null;
	}
	
	/** Get's the internal carpenter meta from an BaseBlock */
	private static byte getMeta(BaseBlock block) {
		return (byte) block.getNbtData().getInt(TEBase.TAG_METADATA);
	}
	
	/*=====================================================================================*/
	
	private static class CarpentersState implements State {
		private final Stairs.Type type;
		private Map<String, CarpentersStateValue> map = Maps.newHashMap();
		
		public CarpentersState(Stairs.Type type) {
			if(!CarpentersStateValue.initalized) CarpentersStateValue.initalize();
			this.type = type;
			for(int i = 0; i < Stairs.stairsList.length; i++) {
				if(Stairs.stairsList[i].stairsType == type) {
					map.put("ID: " + i, CarpentersStateValue.values[i]);
				}
			}
		}

		@Override
		public Map<String, ? extends StateValue> valueMap() {
			return map;
		}

		@Override
		public StateValue getValue(BaseBlock block) {
			return CarpentersStateValue.values[getMeta(block)];
		}

		@Override
		public boolean hasDirection() { 
			return true; 
		}
	}
	
	/*=====================================================================================*/
	
	private static class CarpentersStateValue implements StateValue {
		private static CarpentersStateValue[] values = new CarpentersStateValue[28];
		private static boolean initalized = false;
		
		private static void initalize() {
			for(int i = 0; i < Stairs.stairsList.length; i++) {
				List<ForgeDirection> directions = Stairs.stairsList[i].facings;
				new CarpentersStateValue(i, new Vector( //Using stored facings for the vector is highly skilled...
						directions.contains(ForgeDirection.EAST) ? 1 : (directions.contains(ForgeDirection.WEST) ? -1 : 0), 
						directions.contains(ForgeDirection.UP) ? 1 : (directions.contains(ForgeDirection.DOWN) ? -1 : 0), 
						directions.contains(ForgeDirection.SOUTH) ? 1 : (directions.contains(ForgeDirection.NORTH) ? -1 : 0)));
			}
			initalized = true;
		}

		/*=====================================================================================*/
		
		private Vector direction;
		private byte id;
		
		private CarpentersStateValue(int meta, Vector direction) {
			this.direction = direction;
			this.id = (byte) meta;
			values[meta] = this;
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
}
