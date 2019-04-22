package com.thevoxelbox.voxelsniper.brush.type.performer;

import com.thevoxelbox.voxelsniper.sniper.toolkit.Messages;
import com.thevoxelbox.voxelsniper.sniper.toolkit.ToolkitProperties;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;

/**
 * http://www.voxelwiki.com/minecraft/Voxelsniper#Ring_Brush
 *
 * @author Voxel
 */
public class RingBrush extends AbstractPerformerBrush {

	private double trueCircle;
	private double innerSize;

	public RingBrush() {
		super("Ring");
	}

	private void ring(ToolkitProperties v, Block targetBlock) {
		int brushSize = v.getBrushSize();
		double outerSquared = Math.pow(brushSize + this.trueCircle, 2);
		double innerSquared = Math.pow(this.innerSize, 2);
		for (int x = brushSize; x >= 0; x--) {
			double xSquared = Math.pow(x, 2);
			for (int z = brushSize; z >= 0; z--) {
				double ySquared = Math.pow(z, 2);
				if ((xSquared + ySquared) <= outerSquared && (xSquared + ySquared) >= innerSquared) {
					this.performer.perform(targetBlock.getRelative(x, 0, z));
					this.performer.perform(targetBlock.getRelative(x, 0, -z));
					this.performer.perform(targetBlock.getRelative(-x, 0, z));
					this.performer.perform(targetBlock.getRelative(-x, 0, -z));
				}
			}
		}
		v.getOwner()
			.storeUndo(this.performer.getUndo());
	}

	@Override
	public final void arrow(ToolkitProperties toolkitProperties) {
		this.ring(toolkitProperties, this.getTargetBlock());
	}

	@Override
	public final void powder(ToolkitProperties toolkitProperties) {
		this.ring(toolkitProperties, this.getLastBlock());
	}

	@Override
	public final void info(Messages messages) {
		messages.brushName(this.getName());
		messages.size();
		messages.custom(ChatColor.AQUA + "The inner radius is " + ChatColor.RED + this.innerSize);
	}

	@Override
	public final void parameters(String[] parameters, ToolkitProperties toolkitProperties) {
		for (int i = 1; i < parameters.length; i++) {
			if (parameters[i].equalsIgnoreCase("info")) {
				toolkitProperties.sendMessage(ChatColor.GOLD + "Ring Brush Parameters:");
				toolkitProperties.sendMessage(ChatColor.AQUA + "/b ri true -- will use a true circle algorithm instead of the skinnier version with classic sniper nubs. /b ri false will switch back. (false is default)");
				toolkitProperties.sendMessage(ChatColor.AQUA + "/b ri ir2.5 -- will set the inner radius to 2.5 units");
				return;
			} else if (parameters[i].startsWith("true")) {
				this.trueCircle = 0.5;
				toolkitProperties.sendMessage(ChatColor.AQUA + "True circle mode ON.");
			} else if (parameters[i].startsWith("false")) {
				this.trueCircle = 0;
				toolkitProperties.sendMessage(ChatColor.AQUA + "True circle mode OFF.");
			} else if (parameters[i].startsWith("ir")) {
				try {
					this.innerSize = Double.parseDouble(parameters[i].replace("ir", ""));
					toolkitProperties.sendMessage(ChatColor.AQUA + "The inner radius has been set to " + ChatColor.RED + this.innerSize);
				} catch (NumberFormatException exception) {
					toolkitProperties.sendMessage(ChatColor.RED + "The parameters included are invalid.");
				}
			} else {
				toolkitProperties.sendMessage(ChatColor.RED + "Invalid brush parameters! use the info parameter to display parameter info.");
			}
		}
	}

	@Override
	public String getPermissionNode() {
		return "voxelsniper.brush.ring";
	}
}
