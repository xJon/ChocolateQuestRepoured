package com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.piratecaptain;

import com.teamcqr.chocolatequestrepoured.objects.entity.ai.AbstractCQREntityAI;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRPirateCaptain;

public class BossAIPirateTurnInvisible extends AbstractCQREntityAI<EntityCQRPirateCaptain> {

	private int cooldown = 0;
	private int invisibleTime = 0;
	
	public BossAIPirateTurnInvisible(EntityCQRPirateCaptain entity) {
		super(entity);
	}

	@Override
	public boolean shouldExecute() {
		if(entity != null && entity.getHealth() / entity.getMaxHealth() <= 0.5 && entity.hasAttackTarget() && !entity.isDead) {
			cooldown --;
			return cooldown <= 0;
		}
		return false;
	}
	
	@Override
	public void startExecuting() {
		invisibleTime = 200;
		entity.setInvisibleTicks(1);
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		return invisibleTime > 0;
	}
	
	@Override
	public void updateTask() {
		boolean disInt = false;
		boolean reInt = false;
		boolean invi = true;
		if(invisibleTime <= EntityCQRPirateCaptain.TURN_INVISIBLE_ANIMATION_TIME) {
			reInt = true;
			invi = false;
			entity.setInvisibleTicks(entity.getInvisibleTicks() -1);
		}
		else if(invisibleTime >= 200 - EntityCQRPirateCaptain.TURN_INVISIBLE_ANIMATION_TIME) {
			disInt = true;
			invi = false;
			entity.setInvisibleTicks(entity.getInvisibleTicks() +1);
		}
		
		invisibleTime--;
		if(invisibleTime == 0) {
			disInt = false;
			reInt = false;
			invi = false;
			this.cooldown = 150;
		}
		entity.setInvisible(invi);
		entity.setIsReintegrating(reInt);
		entity.setIsDisintegrating(disInt);
	}

}