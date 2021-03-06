package com.teamcqr.chocolatequestrepoured.structuregen.structurefile;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.init.ModItems;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.structuregen.inhabitants.DungeonInhabitant;
import com.teamcqr.chocolatequestrepoured.structuregen.inhabitants.DungeonInhabitantManager;
import com.teamcqr.chocolatequestrepoured.structureprot.ProtectedRegion;
import com.teamcqr.chocolatequestrepoured.util.BlockPlacingHelper;
import com.teamcqr.chocolatequestrepoured.util.CQRConfig;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;

public class BlockInfoBoss extends AbstractBlockInfo {

	public BlockInfoBoss(BlockPos pos) {
		super(pos);
	}

	public BlockInfoBoss(int x, int y, int z) {
		super(x, y, z);
	}

	public BlockInfoBoss(BlockPos pos, NBTTagIntArray nbtTagIntArray) {
		super(pos);
		this.readFromNBT(nbtTagIntArray, null, null);
	}

	public BlockInfoBoss(int x, int y, int z, NBTTagIntArray nbtTagIntArray) {
		super(x, y, z);
		this.readFromNBT(nbtTagIntArray, null, null);
	}

	@Override
	public void generate(World world, BlockPos dungeonPos, BlockPos dungeonPartPos, PlacementSettings settings, String dungeonMob, ProtectedRegion protectedRegion) {
		BlockPos transformedPos = dungeonPartPos.add(Template.transformedBlockPos(settings, this.getPos()));

		if (!world.isOutsideBuildHeight(transformedPos)) {
			BlockPlacingHelper.setBlockState(world, transformedPos, Blocks.AIR.getDefaultState(), 18, CQRConfig.advanced.instantLightUpdates);

			DungeonInhabitant inha = DungeonInhabitantManager.getInhabitantByName(dungeonMob);

			if (inha != null) {
				if (inha.getBossID() != null) {
					Entity entity = EntityList.createEntityByIDFromName(inha.getBossID(), world);

					entity.setPosition(transformedPos.getX() + 0.5D, transformedPos.getY(), transformedPos.getZ() + 0.5D);

					if (entity instanceof EntityLiving) {
						((EntityLiving) entity).onInitialSpawn(world.getDifficultyForLocation(transformedPos), null);
						((EntityLiving) entity).enablePersistence();

						if (entity instanceof AbstractEntityCQR) {
							((AbstractEntityCQR) entity).onSpawnFromCQRSpawnerInDungeon(settings, inha);
							if (inha.getFactionOverride() != null && !inha.getFactionOverride().isEmpty()) {
								((AbstractEntityCQR) entity).setFaction(inha.getFactionOverride());
							}
						}
					}

					world.spawnEntity(entity);

					if (protectedRegion != null) {
						protectedRegion.addEntityDependency(entity.getPersistentID());
					}
				} else if (inha.getEntityID() != null) {
					/*
					 * EntityArmorStand indicator = new EntityArmorStand(world);
					 * indicator.setCustomNameTag("Oops! We haven't added this boss yet! Treat yourself to some free loot!"); indicator.setPosition(transformedPos.getX() + 0.5D,
					 * transformedPos.getY(), transformedPos.getZ() + 0.5D); indicator.setEntityInvulnerable(true); indicator.setInvisible(true);
					 * indicator.setAlwaysRenderNameTag(true); indicator.setSilent(true); indicator.setNoGravity(true);
					 */
					Entity entity = EntityList.createEntityByIDFromName(inha.getEntityID(), world);

					entity.setPosition(transformedPos.getX() + 0.5D, transformedPos.getY(), transformedPos.getZ() + 0.5D);
					entity.setCustomNameTag("Temporary Boss");

					if (entity instanceof EntityLiving) {
						((EntityLiving) entity).onInitialSpawn(world.getDifficultyForLocation(transformedPos), null);
						((EntityLiving) entity).enablePersistence();

						if (entity instanceof AbstractEntityCQR) {
							((AbstractEntityCQR) entity).onSpawnFromCQRSpawnerInDungeon(settings, inha);
							((AbstractEntityCQR) entity).setSizeVariation(1.1F);
							if (inha.getFactionOverride() != null && !inha.getFactionOverride().isEmpty()) {
								((AbstractEntityCQR) entity).setFaction(inha.getFactionOverride());
							}
						}

						((EntityLiving) entity).getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(100);
						((EntityLiving) entity).setHealth(((EntityLiving) entity).getMaxHealth());

						// Some gear
						((EntityLiving) entity).setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ModItems.GREAT_SWORD_DIAMOND));
						((EntityLiving) entity).setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(inha.getShieldReplacement()));

						((EntityLiving) entity).setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(ModItems.HELMET_HEAVY_DIAMOND));
						((EntityLiving) entity).setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(ModItems.CHESTPLATE_HEAVY_DIAMOND));
						((EntityLiving) entity).setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(ModItems.LEGGINGS_HEAVY_DIAMOND));
						((EntityLiving) entity).setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(ModItems.BOOTS_HEAVY_DIAMOND));
					}

					world.spawnEntity(entity);

					if (protectedRegion != null) {
						protectedRegion.addEntityDependency(entity.getPersistentID());
						CQRMain.logger.info("{} {}", protectedRegion.getUuid(), protectedRegion.getEntityDependencies(), protectedRegion.getBlockDependencies());
					}
				}
			}
		} else {
			CQRMain.logger.warn("Failed to place boss at {}", transformedPos);
		}
	}

	@Override
	public int getId() {
		return BOSS_INFO_ID;
	}

}
