package com.teamcqr.chocolatequestrepoured.util;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

public class BlockPosUtil {

	public static interface BlockInfoConsumer {
		void accept(BlockPos.MutableBlockPos mutablePos, IBlockState state);
	}

	public static interface BlockInfoPredicate {
		boolean test(BlockPos.MutableBlockPos mutablePos, IBlockState state);
	}

	public static void forEach(World world, int x1, int y1, int z1, int horizontalRadius, int verticalRadius, boolean skipUnloadedChunks, boolean skipAirBlocks, BlockInfoConsumer action) {
		forEach(world, x1 - horizontalRadius, y1 - verticalRadius, z1 - horizontalRadius, x1 + horizontalRadius, y1 + verticalRadius, z1 + horizontalRadius, skipUnloadedChunks, skipAirBlocks, action);
	}

	public static void forEach(World world, int x1, int y1, int z1, int x2, int y2, int z2, boolean skipUnloadedChunks, boolean skipAirBlocks, BlockInfoConsumer action) {
		if (world.getWorldType() == WorldType.DEBUG_ALL_BLOCK_STATES) {
			return;
		}

		x1 = Math.max(x1, -30000000);
		y1 = Math.max(y1, 0);
		z1 = Math.max(z1, -30000000);
		x2 = Math.min(x2, 30000000);
		y2 = Math.min(y2, 255);
		z2 = Math.min(z2, 30000000);

		int chunkStartX = x1 >> 4;
		int chunkStartY = y1 >> 4;
		int chunkStartZ = z1 >> 4;
		int chunkEndX = x2 >> 4;
		int chunkEndY = y2 >> 4;
		int chunkEndZ = z2 >> 4;

		BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

		for (int chunkX = chunkStartX; chunkX <= chunkEndX; chunkX++) {
			for (int chunkZ = chunkStartZ; chunkZ <= chunkEndZ; chunkZ++) {
				if (skipUnloadedChunks && !((WorldServer) world).getChunkProvider().chunkExists(chunkX, chunkZ)) {
					continue;
				}

				Chunk chunk = world.getChunk(chunkX, chunkZ);
				ExtendedBlockStorage[] blockStorageArray = chunk.getBlockStorageArray();
				for (int chunkY = chunkStartY; chunkY <= chunkEndY; chunkY++) {
					ExtendedBlockStorage extendedBlockStorage = blockStorageArray[chunkY];

					if (skipAirBlocks && extendedBlockStorage == Chunk.NULL_BLOCK_STORAGE) {
						continue;
					}

					int blockStartX = Math.max(chunkX << 4, x1);
					int blockStartY = Math.max(chunkY << 4, y1);
					int blockStartZ = Math.max(chunkZ << 4, z1);
					int blockEndX = Math.min((chunkX << 4) | 15, x2);
					int blockEndY = Math.min((chunkY << 4) | 15, y2);
					int blockEndZ = Math.min((chunkZ << 4) | 15, z2);

					for (int x5 = blockStartX; x5 <= blockEndX; x5++) {
						for (int y5 = blockStartY; y5 <= blockEndY; y5++) {
							for (int z5 = blockStartZ; z5 <= blockEndZ; z5++) {
								IBlockState state = extendedBlockStorage.get(x5 & 15, y5 & 15, z5 & 15);

								if (skipAirBlocks && state.getBlock() == Blocks.AIR) {
									continue;
								}

								mutablePos.setPos(x5, y5, z5);
								action.accept(mutablePos, state);
							}
						}
					}
				}
			}
		}
	}

	public static List<BlockPos> getAll(World world, int x1, int y1, int z1, int horizontalRadius, int verticalRadius, boolean skipUnloadedChunks, boolean skipAirBlocks, @Nullable Block toCheck, @Nullable BlockInfoPredicate predicate) {
		return getAll(world, x1 - horizontalRadius, y1 - verticalRadius, z1 - horizontalRadius, x1 + horizontalRadius, y1 + verticalRadius, z1 + horizontalRadius, skipUnloadedChunks, skipAirBlocks, toCheck, predicate);
	}

	public static List<BlockPos> getAll(World world, int x1, int y1, int z1, int x2, int y2, int z2, boolean skipUnloadedChunks, boolean skipAirBlocks, @Nullable Block toCheck, @Nullable BlockInfoPredicate predicate) {
		List<BlockPos> list = new ArrayList<>();
		forEach(world, x1, y1, z1, x2, y2, z2, skipUnloadedChunks, skipAirBlocks, (mutablePos, state) -> {
			if ((toCheck == null || state.getBlock() == toCheck) && (predicate == null || predicate.test(mutablePos, state))) {
				list.add(mutablePos.toImmutable());
			}
		});
		return list;
	}

	public static BlockPos getNearest(World world, int x1, int y1, int z1, int horizontalRadius, int verticalRadius, boolean skipUnloadedChunks, boolean skipAirBlocks, @Nullable Block toCheck, @Nullable BlockInfoPredicate predicate) {
		return getNearest(world, x1 - horizontalRadius, y1 - verticalRadius, z1 - horizontalRadius, x1 + horizontalRadius, y1 + verticalRadius, z1 + horizontalRadius, skipUnloadedChunks, skipAirBlocks, new BlockPos(x1, y1, z1), toCheck, predicate);
	}

	public static BlockPos getNearest(World world, int x1, int y1, int z1, int x2, int y2, int z2, boolean skipUnloadedChunks, boolean skipAirBlocks, BlockPos pos, @Nullable Block toCheck, @Nullable BlockInfoPredicate predicate) {
		List<BlockPos> list = getAll(world, x1, y1, z1, x2, y2, z2, skipUnloadedChunks, skipAirBlocks, toCheck, predicate);
		BlockPos p1 = null;
		double min = Double.MAX_VALUE;
		for (BlockPos p2 : list) {
			double d = pos.distanceSq(p2);
			if (d < min) {
				p1 = p2;
				min = d;
			}
		}
		return p1;
	}

}
