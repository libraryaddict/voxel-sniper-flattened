package com.thevoxelbox.voxelsniper.brush.type.performer;

import com.thevoxelbox.voxelsniper.sniper.Sniper;
import com.thevoxelbox.voxelsniper.sniper.toolkit.Messages;
import com.thevoxelbox.voxelsniper.sniper.toolkit.ToolkitProperties;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jetbrains.annotations.Nullable;

/**
 * http://www.voxelwiki.com/minecraft/Voxelsniper#Set_Brush
 *
 * @author Voxel
 */
public class SetBrush extends AbstractPerformerBrush {

	private static final int SELECTION_SIZE_MAX = 5000000;
	@Nullable
	private Block block;

	public SetBrush() {
		super("Set");
	}

	private boolean set(Block block, ToolkitProperties toolkitProperties) {
		if (this.block == null) {
			this.block = block;
			return true;
		} else {
			World thisBlockWorld = this.block.getWorld();
			String name = thisBlockWorld.getName();
			World parameterBlockWorld = block.getWorld();
			String parameterBlockWorldName = parameterBlockWorld.getName();
			if (!name.equals(parameterBlockWorldName)) {
				toolkitProperties.sendMessage(ChatColor.RED + "You selected points in different worlds!");
				this.block = null;
				return true;
			}
			int x1 = this.block.getX();
			int x2 = block.getX();
			int y1 = this.block.getY();
			int y2 = block.getY();
			int z1 = this.block.getZ();
			int z2 = block.getZ();
			int lowX = (x1 <= x2) ? x1 : x2;
			int lowY = (y1 <= y2) ? y1 : y2;
			int lowZ = (z1 <= z2) ? z1 : z2;
			int highX = (x1 >= x2) ? x1 : x2;
			int highY = (y1 >= y2) ? y1 : y2;
			int highZ = (z1 >= z2) ? z1 : z2;
			if (Math.abs(highX - lowX) * Math.abs(highZ - lowZ) * Math.abs(highY - lowY) > SELECTION_SIZE_MAX) {
				toolkitProperties.sendMessage(ChatColor.RED + "Selection size above hardcoded limit, please use a smaller selection.");
			} else {
				for (int y = lowY; y <= highY; y++) {
					for (int x = lowX; x <= highX; x++) {
						for (int z = lowZ; z <= highZ; z++) {
							this.performer.perform(clampY(x, y, z));
						}
					}
				}
			}
			this.block = null;
			return false;
		}
	}

	@Override
	public final void arrow(ToolkitProperties toolkitProperties) {
		Block targetBlock = getTargetBlock();
		if (set(targetBlock, toolkitProperties)) {
			toolkitProperties.sendMessage(ChatColor.GRAY + "Point one");
		} else {
			Sniper owner = toolkitProperties.getOwner();
			owner.storeUndo(this.performer.getUndo());
		}
	}

	@Override
	public final void powder(ToolkitProperties toolkitProperties) {
		Block lastBlock = getLastBlock();
		if (lastBlock == null) {
			return;
		}
		if (set(lastBlock, toolkitProperties)) {
			toolkitProperties.sendMessage(ChatColor.GRAY + "Point one");
		} else {
			Sniper owner = toolkitProperties.getOwner();
			owner.storeUndo(this.performer.getUndo());
		}
	}

	@Override
	public final void info(Messages messages) {
		this.block = null;
		messages.brushName(this.getName());
	}

	@Override
	public final void parameters(String[] parameters, ToolkitProperties toolkitProperties) {
		super.parameters(parameters, toolkitProperties);
	}

	@Override
	public String getPermissionNode() {
		return "voxelsniper.brush.set";
	}
}