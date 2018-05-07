package thecrafter4000.weblocks.addon.carpenters;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.Map;

import com.carpentersblocks.tileentity.TEBase;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;

import net.minecraftforge.common.util.ForgeDirection;

public class CarpentersUtils {

	/** Get's the internal carpenter meta from an BaseBlock */
	public static byte getMeta(BaseBlock block) {
		return (byte) block.getNbtData().getInt(TEBase.TAG_METADATA);
	}
	
	/** 
	 * Converts the given direction (or multiple) into an WE Vector
	 * @param directions Do not put {@code null} here. Use {@link ForgeDirection#UNKNOWN} instead.
	 */
	public static Vector toVector(ForgeDirection... directions) {
		checkNotNull(directions);
		return toVector(ImmutableList.copyOf(directions));
	}
	
	/** Converts the given directions into an WE Vector */
	public static Vector toVector(List<ForgeDirection> directions) {
		checkNotNull(directions);
		return new Vector(
				directions.contains(ForgeDirection.EAST) ? 1 : (directions.contains(ForgeDirection.WEST) ? -1 : 0), 
				directions.contains(ForgeDirection.UP) ? 1 : (directions.contains(ForgeDirection.DOWN) ? -1 : 0), 
				directions.contains(ForgeDirection.SOUTH) ? 1 : (directions.contains(ForgeDirection.NORTH) ? -1 : 0));
	}
	
	/** Copies array data into an array. The {@code key} is the String representation of the index, using {@link String#valueOf(int)}. */
	public static <V> Map<String, V> toImmutableMap(V[] data){
		checkNotNull(data);
		Builder<String, V> builder = ImmutableMap.builder();
		for(int i = 0; i < data.length; i++) {
			builder.put(String.valueOf(i), data[i]);
		}
		return builder.build();
	}
}
