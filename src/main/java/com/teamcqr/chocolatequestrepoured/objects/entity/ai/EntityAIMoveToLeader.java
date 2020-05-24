package com.teamcqr.chocolatequestrepoured.objects.entity.ai;

import java.util.EnumSet;

import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;

import net.minecraft.entity.LivingEntity;
import net.minecraft.pathfinding.PathPoint;

public class EntityAIMoveToLeader extends AbstractCQREntityAI<AbstractEntityCQR> {

	public EntityAIMoveToLeader(AbstractEntityCQR entity) {
		super(entity);
		//this.setMutexBits(3);
		setMutexFlags(EnumSet.of(Flag.MOVE));
	}

	@Override
	public boolean shouldExecute() {
		if (this.entity.hasLeader()) {
			LivingEntity leader = this.entity.getLeader();
			return this.entity.getDistanceSq(leader) > 64.0D;
		}
		return false;
	}

	@Override
	public boolean shouldContinueExecuting() {
		if (this.entity.hasLeader()) {
			LivingEntity leader = this.entity.getLeader();

			if (this.entity.getDistanceSq(leader) > 16.0D) {
				return this.entity.hasPath();
			}
		}
		return false;
	}

	@Override
	public void startExecuting() {
		LivingEntity leader = this.entity.getLeader();
		this.entity.getNavigator().tryMoveToEntityLiving(leader, 1.0D);
	}

	@Override
	public void tick() {
		if (this.entity.hasPath()) {
			LivingEntity leader = this.entity.getLeader();
			PathPoint target = this.entity.getNavigator().getPath().getFinalPathPoint();

			if (leader.getDistanceSq(target.x + 0.5D, target.y, target.z + 0.5D) > 16.0D) {
				this.entity.getNavigator().tryMoveToEntityLiving(leader, 1.0D);
			}
		}
	}

	@Override
	public void resetTask() {
		this.entity.getNavigator().clearPath();
	}

}
