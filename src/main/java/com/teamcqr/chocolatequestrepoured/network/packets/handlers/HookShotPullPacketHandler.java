package com.teamcqr.chocolatequestrepoured.network.packets.handlers;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.network.packets.toClient.HookShotPullPacket;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileHookShotHook;
import com.teamcqr.chocolatequestrepoured.objects.items.ItemHookshotBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class HookShotPullPacketHandler implements IMessageHandler<HookShotPullPacket, IMessage> {
    @Override
    public IMessage onMessage(final HookShotPullPacket message, MessageContext ctx) {
        if (ctx.side != Side.CLIENT) {
            return null;
        }

        Minecraft minecraft = Minecraft.getMinecraft();
        final WorldClient worldClient = minecraft.world;

        minecraft.addScheduledTask(new Runnable() {
            @Override
            public void run() {
                HookShotPullPacketHandler.this.processMessage(message, ctx);
            }
        });

        return null;
    }

    private void processMessage(final HookShotPullPacket message, MessageContext ctx) {
        EntityPlayer player = CQRMain.proxy.getPlayer(ctx);

        if (message.isPulling())
        {
            System.out.println("Received a pull message. Impacted @ " + message.getImpactLocation());

            Vec3d playerPos = player.getPositionVector();

            double distanceToHook = playerPos.distanceTo(message.getImpactLocation());

            //Check to see if we are already at the hook
            if (distanceToHook < ProjectileHookShotHook.STOP_PULL_DISTANCE) {
                player.setVelocity(0 , 0 ,0);
                return;
            }

            Vec3d hookDirection = message.getImpactLocation().subtract(playerPos);
            Vec3d pullV = hookDirection.normalize().scale(message.getVelocity());

            player.setVelocity(pullV.x, pullV.y, pullV.z);
            player.velocityChanged = true;
        }
        else
        {
            player.setVelocity(0, 0, 0);
        }
    }
}