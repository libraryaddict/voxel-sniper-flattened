/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.thevoxelbox.voxelsniper.brush.performer.type;

import java.util.List;
import com.thevoxelbox.voxelsniper.sniper.toolkit.Messages;
import com.thevoxelbox.voxelsniper.sniper.toolkit.ToolkitProperties;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

/**
 * @author Voxel
 */
public class IncludeComboPerformer extends AbstractPerformer {

	private List<BlockData> includeList;
	private BlockData blockData;

	public IncludeComboPerformer() {
		super("Include Combo");
	}

	@Override
	public void info(Messages messages) {
		messages.performerName(this.getName());
		messages.voxelList();
		messages.blockDataType();
		messages.blockData();
	}

	@Override
	public void init(ToolkitProperties toolkitProperties) {
		this.world = toolkitProperties.getWorld();
		this.blockData = toolkitProperties.getBlockData();
		this.includeList = toolkitProperties.getVoxelList();
	}

	@Override
	public void perform(Block block) {
		BlockData blockData = block.getBlockData();
		if (this.includeList.contains(blockData)) {
			this.undo.put(block);
			block.setBlockData(this.blockData);
		}
	}
}
