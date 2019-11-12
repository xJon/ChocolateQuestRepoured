package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration;

import net.minecraft.block.BlockSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

public class RoomDecorTableMedium extends RoomDecorBlocks
{

    public RoomDecorTableMedium()
    {
        super();
    }

    @Override
    protected void makeSchematic()
    {
        IBlockState blockType = Blocks.OAK_FENCE.getDefaultState();
        schematic.add(new DecoBlockOffset(0, 0, 0, blockType));
        schematic.add(new DecoBlockOffset(1, 0, 0, blockType));
        schematic.add(new DecoBlockOffset(0, 0, 1, blockType));
        schematic.add(new DecoBlockOffset(1, 0, 1, blockType));

        blockType = Blocks.WOODEN_SLAB.getDefaultState();
        blockType = blockType.withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.BOTTOM);
        schematic.add(new DecoBlockOffset(0, 1, 0, blockType));
        schematic.add(new DecoBlockOffset(1, 1, 0, blockType));
        schematic.add(new DecoBlockOffset(0, 1, 1, blockType));
        schematic.add(new DecoBlockOffset(1, 1, 1, blockType));
    }

}
