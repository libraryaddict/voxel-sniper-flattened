/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.thevoxelbox.voxelsniper.brush.performer.type;

import com.thevoxelbox.voxelsniper.sniper.toolkit.Messages;
import com.thevoxelbox.voxelsniper.sniper.toolkit.ToolkitProperties;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

/**
 * @author Voxel
 */
public class ComboMaterialPerformer extends AbstractPerformer {

	private BlockData blockData;
	private BlockData replaceBlockData;

	public ComboMaterialPerformer() {
		super("Combo-Material");
	}

	@Override
	public void init(ToolkitProperties toolkitProperties) {
		this.world = toolkitProperties.getWorld();
		this.blockData = toolkitProperties.getBlockData();
		this.replaceBlockData = toolkitProperties.getReplaceBlockData();
	}

	@Override
	public void info(Messages messages) {
		messages.performerName(this.getName());
		messages.blockDataType();
		messages.replaceBlockDataType();
		messages.blockData();
	}

	@Override
	public void perform(Block block) {
		if (block.getType() == this.replaceBlockData.getMaterial()) {
			this.undo.put(block);
			block.setBlockData(this.blockData);
		}
	}

	@Override
	public boolean isUsingReplaceMaterial() {
		return true;
	}
}
