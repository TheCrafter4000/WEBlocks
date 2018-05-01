package thecrafter4000.weblocks;

import java.util.List;
import java.util.Map;

import com.carpentersblocks.data.Stairs;
import com.carpentersblocks.tileentity.TEBase;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sk89q.jnbt.CompoundTagBuilder;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.world.registry.State;
import com.sk89q.worldedit.world.registry.StateValue;

import net.minecraftforge.common.util.ForgeDirection;

/**
 * Carpenters Stair integration
 * @author TheCrafter4000
 */
public class CarpentersState implements State {
	
	/** Internal use */
	private static List<CarpentersState> states = Lists.newArrayList();
	
	/** Get's the internal carpenter meta from an BaseBlock */
	public static byte getMeta(BaseBlock block) {
		return (byte) block.getNbtData().getInt(TEBase.TAG_METADATA);
	}
	
	/** Get's the fitting state for the internal meta because Carpenters stairs are rotated differently depending on it's type. */
	public static CarpentersState getByMeta(int meta) {
		Stairs.Type t = Stairs.stairsList[meta].stairsType;
		for(CarpentersState s : states) {
			if(s.type == t) {
				return s;
			}
		};
		WEBlocks.Logger.fatal("Did not resolve CarpenterState for meta " + meta + "!");
		return null;
	}
	
	/** Helper function. Calls {@link #getByMeta(int)} */
	public static CarpentersState getByBlock(BaseBlock block) {
		return getByMeta(getMeta(block));
	}
	
	public static final CarpentersState NORMAL_SIDE = new CarpentersState(Stairs.Type.NORMAL_SIDE);
	public static final CarpentersState NORMAL		= new CarpentersState(Stairs.Type.NORMAL);
	public static final CarpentersState NORMAL_INT  = new CarpentersState(Stairs.Type.NORMAL_INT);
	public static final CarpentersState NORMAL_EXT  = new CarpentersState(Stairs.Type.NORMAL_EXT);
	
	/*=====================================================================================*/

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

		states.add(this);
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
	
	/*=====================================================================================*/
	
	private static class CarpentersStateValue implements StateValue {
		private static CarpentersStateValue[] values = new CarpentersStateValue[28];
		private static boolean initalized = false;
		
		private static void initalize() {
			for(int i = 0; i < Stairs.stairsList.length; i++) {
				List<ForgeDirection> directions = Stairs.stairsList[i].facings;
				new CarpentersStateValue(i, new Vector( //Uses the facings for the vector is highly skilled.
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
			return id == CarpentersState.getMeta(block);
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
