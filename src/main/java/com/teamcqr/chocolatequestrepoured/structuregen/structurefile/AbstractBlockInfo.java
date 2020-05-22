package com.teamcqr.chocolatequestrepoured.structuregen.structurefile;

import javax.annotation.Nullable;

import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;

public abstract class AbstractBlockInfo implements IGeneratable {

	protected BlockPos pos;

	public AbstractBlockInfo(BlockPos pos) {
		this.pos = pos;
	}

	public AbstractBlockInfo(BlockPos pos, NBTTagIntArray nbtTagIntArray, BlockStatePalette blockStatePalette, NBTTagList compoundTagList) {
		this.pos = pos;
		this.readFromNBT(nbtTagIntArray, blockStatePalette, compoundTagList);
	}

	public abstract int getId();

	public NBTTagIntArray writeToNBT(BlockStatePalette blockStatePalette, NBTTagList compoundTagList) {
		return new NBTTagIntArray(new int[] { this.getId() });
	}

	public void readFromNBT(NBTTagIntArray nbtTagIntArray, BlockStatePalette blockStatePalette, NBTTagList compoundTagList) {

	}

	@Nullable
	public static AbstractBlockInfo create(BlockPos pos, NBTTagIntArray nbtTagIntArray, BlockStatePalette blockStatePalette, NBTTagList compoundTagList) {
		if (nbtTagIntArray.getIntArray().length == 0) {
			return null;
		}
		switch (nbtTagIntArray.getIntArray()[0]) {
		case PosInfoBlock.ID:
			return new PosInfoBlock(pos, nbtTagIntArray, blockStatePalette, compoundTagList);
		case PosInfoBanner.ID:
			return new PosInfoBanner(pos, nbtTagIntArray, blockStatePalette, compoundTagList);
		case PosInfoBoss.ID:
			return new PosInfoBoss(pos, nbtTagIntArray);
		case PosInfoForceFieldNexus.ID:
			return new PosInfoForceFieldNexus(pos, nbtTagIntArray);
		case PosInfoLootChest.ID:
			return new PosInfoLootChest(pos, nbtTagIntArray);
		case PosInfoSpawner.ID:
			return new PosInfoSpawner(pos, nbtTagIntArray, blockStatePalette, compoundTagList);
		default:
			return null;
		}
	}

	public BlockPos getPos() {
		return this.pos;
	}

}
