package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonCastle;
import com.teamcqr.chocolatequestrepoured.util.BlockStateGenArray;

import net.minecraft.util.math.BlockPos;

public abstract class CastleRoomGenericBase extends CastleRoomDecoratedBase {
	public CastleRoomGenericBase(int sideLength, int height, int floor) {
		super(sideLength, height, floor);
	}

	@Override
	public void generateRoom(BlockPos castleOrigin, BlockStateGenArray genArray, DungeonCastle dungeon) {
		; // No special generation - decorations only
	}

	@Override
	boolean shouldBuildEdgeDecoration() {
		return true;
	}

	@Override
	boolean shouldBuildWallDecoration() {
		return true;
	}

	@Override
	boolean shouldBuildMidDecoration() {
		return true;
	}

	@Override
	boolean shouldAddSpawners() {
		return true;
	}

	@Override
	boolean shouldAddChests() {
		return true;
	}
}
