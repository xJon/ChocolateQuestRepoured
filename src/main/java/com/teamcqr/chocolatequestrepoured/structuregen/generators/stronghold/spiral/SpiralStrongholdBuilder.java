package com.teamcqr.chocolatequestrepoured.structuregen.generators.stronghold.spiral;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonVolcano;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.AbstractDungeonPart;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.DungeonGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.AbstractDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.stronghold.EStrongholdRoomType;
import com.teamcqr.chocolatequestrepoured.util.ESkyDirection;

import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SpiralStrongholdBuilder {

	private AbstractDungeonGenerator<DungeonVolcano> generator;
	private DungeonGenerator dungeonGenerator;
	private ESkyDirection allowedDirection;
	private DungeonVolcano dungeon;
	private SpiralStrongholdFloor[] floors;
	private int floorCount = 0;
	private List<AbstractDungeonPart> strongholdParts = new ArrayList<>();
	private Random rdm;

	public SpiralStrongholdBuilder(AbstractDungeonGenerator<DungeonVolcano> generator, DungeonGenerator dungeonGenerator, ESkyDirection expansionDirection, DungeonVolcano dungeon, Random rdm) {
		this.generator = generator;
		this.dungeonGenerator = dungeonGenerator;
		this.rdm = rdm;
		this.allowedDirection = expansionDirection;
		this.dungeon = dungeon;

		this.floorCount = dungeon.getFloorCount(this.rdm);
		this.floors = new SpiralStrongholdFloor[this.floorCount];
	}

	public void calculateFloors(BlockPos strongholdEntrancePos) {
		Tuple<Integer, Integer> posTuple = new Tuple<>(strongholdEntrancePos.getX(), strongholdEntrancePos.getZ());
		int middle = (int) Math.floor(this.dungeon.getFloorSideLength() / 2);
		int entranceX = 0;
		int entranceZ = 0;
		int roomCount = this.dungeon.getStrongholdRoomCount(this.rdm);
		final int maxRoomsPerFloor = this.dungeon.getFloorSideLength() * 4 - 4;
		EStrongholdRoomType entranceType = EStrongholdRoomType.NONE;
		switch (this.allowedDirection) {
		case WEST:
			entranceType = EStrongholdRoomType.CURVE_EN;
			entranceX = this.dungeon.getFloorSideLength() - 1;
			entranceZ = middle;
			break;
		case NORTH:
			entranceType = EStrongholdRoomType.CURVE_SE;
			entranceX = middle;
			entranceZ = this.dungeon.getFloorSideLength() - 1;
			break;
		case SOUTH:
			entranceType = EStrongholdRoomType.CURVE_NW;
			entranceX = middle;
			entranceZ = 0;
			break;
		case EAST:
			entranceType = EStrongholdRoomType.CURVE_WS;
			entranceX = 0;
			entranceZ = middle;
			break;
		default:
			break;

		}
		int y = strongholdEntrancePos.getY();
		for (int i = 0; i < this.floors.length; i++) {
			if (posTuple == null || roomCount <= 0) {
				this.floorCount--;
				continue;
			}
			int floorRoomCount = maxRoomsPerFloor;
			if (roomCount >= maxRoomsPerFloor) {
				roomCount -= maxRoomsPerFloor;
				/* We add one cause the room above the stair does not count as room */
				roomCount++;
			} else {
				floorRoomCount = roomCount;
				roomCount = 0;
			}
			SpiralStrongholdFloor floor = new SpiralStrongholdFloor(this.generator, this.dungeonGenerator, posTuple, entranceX, entranceZ, roomCount <= 0 || i == (this.floors.length - 1), this.dungeon.getFloorSideLength(), floorRoomCount);
			floor.calculateRoomGrid(entranceType, (i + 1) % 2 == 0);
			floor.calculateCoordinates(y, this.dungeon.getRoomSizeX(), this.dungeon.getRoomSizeZ());
			posTuple = floor.getExitCoordinates();
			if (i != 0) {
				floor.overrideFirstRoomType(EStrongholdRoomType.NONE);
			}
			entranceX = floor.getExitIndex().getFirst();
			entranceZ = floor.getExitIndex().getSecond();
			if (i == (this.floors.length - 1)) {
				floor.overrideLastRoomType(EStrongholdRoomType.BOSS);
			} else {
				entranceType = floor.getExitRoomType();
			}
			y += this.dungeon.getRoomSizeY();
			this.floors[i] = floor;
		}
	}

	public void buildFloors(BlockPos strongholdEntrancePos, World world, int dungeonChunkX, int dungeonChunkZ, String mobType) {
		// BlockPos currentPos = strongholdEntrancePos;
		List<AbstractDungeonPart> floors = new ArrayList<>();
		for (int i = 0; i < this.floorCount; i++) {
			SpiralStrongholdFloor floor = this.floors[i];
			floors.addAll(floor.buildRooms(this.dungeon, strongholdEntrancePos.getX() / 16, strongholdEntrancePos.getZ() / 16, world, mobType));
		}
		this.strongholdParts.addAll(floors);
	}

	public List<AbstractDungeonPart> getStrongholdParts() {
		return this.strongholdParts;
	}

}
