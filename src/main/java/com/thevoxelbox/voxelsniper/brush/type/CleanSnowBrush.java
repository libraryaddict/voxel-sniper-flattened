package com.thevoxelbox.voxelsniper.brush.type;

import com.thevoxelbox.voxelsniper.sniper.Sniper;
import com.thevoxelbox.voxelsniper.sniper.Undo;
import com.thevoxelbox.voxelsniper.sniper.snipe.message.SnipeMessenger;
import com.thevoxelbox.voxelsniper.sniper.snipe.Snipe;
import com.thevoxelbox.voxelsniper.sniper.toolkit.ToolkitProperties;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;

/**
 * http://www.voxelwiki.com/minecraft/Voxelsniper#Clean_Snow_Brush
 *
 * @author psanker
 */
public class CleanSnowBrush extends AbstractBrush {

	private double trueCircle;

	@Override
	public final void handleCommand(String[] parameters, Snipe snipe) {
		SnipeMessenger messenger = snipe.createMessenger();
		for (int index = 1; index < parameters.length; index++) {
			String parameter = parameters[index];
			if (parameter.equalsIgnoreCase("info")) {
				messenger.sendMessage(ChatColor.GOLD + "Clean Snow Brush Parameters:");
				messenger.sendMessage(ChatColor.AQUA + "/b cls true -- will use a true sphere algorithm instead of the skinnier version with classic sniper nubs. /b cls false will switch back. (false is default)");
				return;
			} else if (parameter.startsWith("true")) {
				this.trueCircle = 0.5;
				messenger.sendMessage(ChatColor.AQUA + "True circle mode ON.");
			} else if (parameter.startsWith("false")) {
				this.trueCircle = 0;
				messenger.sendMessage(ChatColor.AQUA + "True circle mode OFF.");
			} else {
				messenger.sendMessage(ChatColor.RED + "Invalid brush parameters! use the info parameter to display parameter info.");
			}
		}
	}

	@Override
	public void handleArrowAction(Snipe snipe) {
		cleanSnow(snipe);
	}

	@Override
	public void handleGunpowderAction(Snipe snipe) {
		cleanSnow(snipe);
	}

	private void cleanSnow(Snipe snipe) {
		ToolkitProperties toolkitProperties = snipe.getToolkitProperties();
		int brushSize = toolkitProperties.getBrushSize();
		double brushSizeSquared = Math.pow(brushSize + this.trueCircle, 2);
		Undo undo = new Undo();
		for (int y = (brushSize + 1) * 2; y >= 0; y--) {
			double ySquared = Math.pow(y - brushSize, 2);
			for (int x = (brushSize + 1) * 2; x >= 0; x--) {
				double xSquared = Math.pow(x - brushSize, 2);
				for (int z = (brushSize + 1) * 2; z >= 0; z--) {
					if ((xSquared + Math.pow(z - brushSize, 2) + ySquared) <= brushSizeSquared) {
						Block targetBlock = getTargetBlock();
						int targetBlockX = targetBlock.getX();
						int targetBlockY = targetBlock.getY();
						int targetBlockZ = targetBlock.getZ();
						if ((clampY(targetBlockX + x - brushSize, targetBlockY + z - brushSize, targetBlockZ + y - brushSize).getType() == Material.SNOW) && ((clampY(targetBlockX + x - brushSize, targetBlockY + z - brushSize - 1, targetBlockZ + y - brushSize).getType() == Material.SNOW) || (clampY(targetBlockX + x - brushSize, targetBlockY + z - brushSize - 1, targetBlockZ + y - brushSize).getType() == Material.AIR))) {
							undo.put(clampY(targetBlockX + x, targetBlockY + z, targetBlockZ + y));
							setBlockData(targetBlockZ + y - brushSize, targetBlockX + x - brushSize, targetBlockY + z - brushSize, Material.AIR.createBlockData());
						}
					}
				}
			}
		}
		Sniper sniper = snipe.getSniper();
		sniper.storeUndo(undo);
	}

	@Override
	public void sendInfo(Snipe snipe) {
		SnipeMessenger messenger = snipe.createMessenger();
		messenger.sendBrushNameMessage();
		messenger.sendBrushSizeMessage();
	}
}
