package com.thevoxelbox.voxelsniper.brush.perform;

import com.thevoxelbox.voxelsniper.Message;
import com.thevoxelbox.voxelsniper.SnipeData;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

/**
 * @author Voxel
 */
public class InkInkNoPhysPerformer extends AbstractPerformer {

	private BlockData blockData;
	private BlockData replaceBlockData;

	public InkInkNoPhysPerformer() {
		super("Ink-Ink, No Physics");
	}

	@Override
	public void init(SnipeData snipeData) {
		this.world = snipeData.getWorld();
		this.blockData = snipeData.getBlockData();
		this.replaceBlockData = snipeData.getReplaceBlockData();
	}

	@Override
	public void info(Message message) {
		message.performerName(this.getName());
		message.blockData();
		message.replaceBlockData();
	}

	@Override
	public void perform(Block block) {
		BlockData blockData = block.getBlockData();
		if (blockData.equals(this.replaceBlockData)) {
			this.undo.put(block);
			block.setBlockData(this.blockData, false);
		}
	}

	@Override
	public boolean isUsingReplaceMaterial() {
		return true;
	}
}