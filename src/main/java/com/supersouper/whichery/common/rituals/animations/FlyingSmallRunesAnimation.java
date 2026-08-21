package com.supersouper.whichery.common.rituals.animations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import org.lwjgl.opengl.GL11;

import com.gtnewhorizon.gtnhlib.eventbus.EventBusSubscriber;
import com.supersouper.whichery.api.rituals.RitualAnimation;
import com.supersouper.whichery.api.rituals.RitualRegistry;
import com.supersouper.whichery.api.rituals.RunningRitual;
import com.supersouper.whichery.client.render.ChalkRuneISBRH;
import com.supersouper.whichery.common.blocks.BlockChalkRuneSmall;
import com.supersouper.whichery.common.tileentities.ChalkRuneSmallTileEntity;
import com.supersouper.whichery.utils.WhicheryUtils;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class FlyingSmallRunesAnimation extends RitualAnimation {

    @SideOnly(Side.CLIENT)
    public static ArrayList<FlyingSmallRunesAnimation> animations;
    private ArrayList<Rune> runes = new ArrayList<>();

    private static class Rune {

        public double x, y, z, wantedX, wantedY, wantedZ;
        public int pos, rotation, rune;
        public String type;
        public final float[] rr = new float[3];

        public Rune(double x, double y, double z, int pos, int rotation, int rune, String type) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.wantedX = x;
            this.wantedY = y;
            this.wantedZ = z;
            this.pos = pos;
            this.rotation = rotation;
            this.rune = rune;
            this.type = type;
            this.rr[2] = rotation * 45;
        }
    }

    private int ticks = 0;

    public FlyingSmallRunesAnimation(TileEntity leader, RunningRitual currentRitual) {
        super(leader, currentRitual);
    }

    @Override
    public void onTick() {
        ticks++;
        rotate(0.05);
    }

    @Override
    public void transitionToStage(int stage) {
        if (leader.getWorldObj().isRemote && stage == 0) {
            animations.add(this);
            for (TileEntity te : currentRitual.getCapturedTileEntities()) {
                if (te.getClass() != ChalkRuneSmallTileEntity.class) continue;
                ChalkRuneSmallTileEntity cte = (ChalkRuneSmallTileEntity) te;
                Arrays.fill(cte.hides, true);
                cte.getWorldObj()
                    .markBlockForUpdate(cte.xCoord, cte.yCoord, cte.zCoord);
                for (int i = 0; i < cte.getTypes().length; i++) {
                    if (cte.getType(i) == null) continue;
                    runes.add(
                        new Rune(
                            cte.xCoord - leader.xCoord + BlockChalkRuneSmall.positions[i][0] - 0.25f,
                            cte.yCoord - leader.yCoord,
                            cte.zCoord - leader.zCoord + BlockChalkRuneSmall.positions[i][1] - 0.25f,
                            i,
                            cte.getRotation(i),
                            cte.getRune(i),
                            cte.getType(i)));
                }
            }
            this.radius = computeAverageRadius(runes) + 0.15 * runes.size();
            this.fixedY = computeAverageY(runes) + 1.5;
            this.baseAngle = new double[runes.size()];
            assignSlots();
        }
    }

    @Override
    public void complete(int stage) {

    }

    @Override
    public void end(int stage) {
        if (leader.getWorldObj().isRemote) {
            animations.remove(this);
            for (TileEntity te : currentRitual.getCapturedTileEntities()) {
                if (te.getClass() != ChalkRuneSmallTileEntity.class) continue;
                ChalkRuneSmallTileEntity cte = (ChalkRuneSmallTileEntity) te;
                Arrays.fill(cte.hides, false);
                cte.getWorldObj()
                    .markBlockForUpdate(cte.xCoord, cte.yCoord, cte.zCoord);
            }
        }
    }

    public void render(RenderWorldLastEvent event) {
        Tessellator t = Tessellator.instance;
        Minecraft mc = Minecraft.getMinecraft();
        EntityLivingBase viewEntity = mc.renderViewEntity != null ? mc.renderViewEntity : mc.thePlayer;
        double playerX = viewEntity.prevPosX + (viewEntity.posX - viewEntity.prevPosX) * event.partialTicks;
        double playerY = viewEntity.prevPosY + (viewEntity.posY - viewEntity.prevPosY) * event.partialTicks;
        double playerZ = viewEntity.prevPosZ + (viewEntity.posZ - viewEntity.prevPosZ) * event.partialTicks;
        GL11.glPushMatrix();
        GL11.glTranslated(-playerX, -playerY, -playerZ);
        GL11.glTranslatef(leader.xCoord + 0.5f, leader.yCoord, leader.zCoord + 0.5f);
        for (Rune rune : runes) {
            GL11.glPushMatrix();
            rune.x = WhicheryUtils.lerpD(rune.x, rune.wantedX, 0.02d);
            rune.y = WhicheryUtils.lerpD(rune.y, rune.wantedY, 0.02d);
            rune.z = WhicheryUtils.lerpD(rune.z, rune.wantedZ, 0.02d);
            GL11.glTranslated(rune.x, rune.y, rune.z);

            float yaw = (float) Math.toDegrees(Math.atan2(rune.z, rune.x)) + 90.0F;
            rune.rr[0] = ticks < 10 ? WhicheryUtils.lerpF(rune.rr[0], yaw, 0.1f) : yaw;
            GL11.glRotatef(-rune.rr[0], 0.0F, 1.0F, 0.0F);
            rune.rr[1] = WhicheryUtils.lerpF(rune.rr[1], 90, 0.02f);
            GL11.glRotatef(rune.rr[1], 1.0F, 0.0F, 0.0F);
            t.startDrawingQuads();
            int color = RitualRegistry.CHALK_TYPES.get(rune.type).drawColor;
            int r = (color >> 16) & 255;
            int g = (color >> 8) & 255;
            int b = color & 255;
            t.setTranslation(-0.5, 0, -0.5);
            rune.rr[2] = WhicheryUtils.lerpF(rune.rr[2], 180, 0.02f);
            ChalkRuneISBRH.renderIconIn2D(
                t,
                RitualRegistry.RUNE_ICONS_SMALL.get(rune.type)[rune.rune],
                8,
                1f / 16f,
                0.5f,
                rune.rr[2],
                r,
                g,
                b,
                true,
                false);
            t.setTranslation(0, 0, 0);
            t.draw();

            GL11.glPopMatrix();
        }
        GL11.glPopMatrix();
    }

    private double radius;
    private double fixedY;
    private double[] baseAngle;
    private double currentRotation = 0.0;

    private static double computeAverageRadius(List<Rune> runes) {
        if (runes.isEmpty()) return 1.0;
        double sum = 0;
        for (Rune r : runes) sum += Math.hypot(r.wantedX, r.wantedZ);
        return sum / runes.size();
    }

    private static double computeAverageY(List<Rune> runes) {
        if (runes.isEmpty()) return 0.0;
        double sum = 0;
        for (Rune r : runes) sum += r.wantedY;
        return sum / runes.size();
    }

    private void assignSlots() {
        int n = runes.size();
        if (n == 0) return;

        // Equally spaced slots, relative to an as-yet-undetermined starting angle.
        double[] slotAngles = new double[n];
        for (int i = 0; i < n; i++) slotAngles[i] = 2 * Math.PI * i / n;

        // Current angle of each rune, normalized to [0, 2π).
        double[] runeAngle = new double[n];
        Integer[] order = new Integer[n];
        for (int i = 0; i < n; i++) {
            Rune r = runes.get(i);
            double a = Math.atan2(r.wantedZ, r.wantedX);
            if (a < 0) a += 2 * Math.PI;
            runeAngle[i] = a;
            order[i] = i;
        }
        Arrays.sort(order, Comparator.comparingDouble(i -> runeAngle[i]));

        // For every possible cyclic match between sorted runes and sorted slots,
        // find the best rigid rotation of the whole slot ring for that match
        // (via circular mean), then score the match+rotation pair. Keep the best.
        double bestCost = Double.MAX_VALUE;
        int bestOffset = 0;
        double bestTheta = 0.0;
        for (int offset = 0; offset < n; offset++) {
            double sumSin = 0, sumCos = 0;
            for (int i = 0; i < n; i++) {
                double diff = runeAngle[order[i]] - slotAngles[(i + offset) % n];
                sumSin += Math.sin(diff);
                sumCos += Math.cos(diff);
            }
            // Circular mean of the residuals = the rotation that best aligns
            // this match's slots with the runes' actual angles.
            double theta = Math.atan2(sumSin, sumCos);

            double cost = 0;
            for (int i = 0; i < n; i++) {
                double slot = slotAngles[(i + offset) % n] + theta;
                cost += circularDistance(runeAngle[order[i]], slot);
            }

            if (cost < bestCost) {
                bestCost = cost;
                bestOffset = offset;
                bestTheta = theta;
            }
        }

        for (int i = 0; i < n; i++) {
            int runeIdx = order[i];
            baseAngle[runeIdx] = normalizeAngle(slotAngles[(i + bestOffset) % n] + bestTheta);
        }

        // Snap runes to their assigned slot immediately.
        for (int i = 0; i < n; i++) {
            applyPosition(i, baseAngle[i]);
        }
    }

    private static double circularDistance(double a, double b) {
        double diff = Math.abs(a - b) % (2 * Math.PI);
        return Math.min(diff, 2 * Math.PI - diff);
    }

    private static double normalizeAngle(double a) {
        double twoPi = 2 * Math.PI;
        double r = a % twoPi;
        return r < 0 ? r + twoPi : r;
    }

    private void applyPosition(int index, double angle) {
        Rune r = runes.get(index);
        r.wantedX = radius * Math.cos(angle);
        r.wantedZ = radius * Math.sin(angle);
        r.wantedY = fixedY;
    }

    public void rotate(double deltaAngleRadians) {
        currentRotation = (currentRotation + deltaAngleRadians) % (2 * Math.PI);
        for (int i = 0; i < runes.size(); i++) {
            applyPosition(i, baseAngle[i] + currentRotation);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

    }

    @SideOnly(Side.CLIENT)
    @EventBusSubscriber(side = Side.CLIENT)
    public static class Events {

        static {
            animations = new ArrayList<>();
        }

        @SubscribeEvent
        public static void onRenderWorldLast(RenderWorldLastEvent event) {
            for (FlyingSmallRunesAnimation animation : animations) {
                animation.render(event);
            }
        }
    }
}
