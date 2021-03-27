package com.idtech;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * This class is a helpful method to help throw TNT with an arc a specific distance.
 *
 * In minecraft, imparting TNT with a motion value of 1 in the x and 1 in the y moves it exactly 30 blocks,
 * swapping the motion value in the x for 2 moves it 60 blocks, using this data, we can infer that air resistance is ignored.
 * Taking this into account, we can use kinematics to throw a block and have it land in a specific spot.
 *
 * @author Holden Clarke
 * @license GPLv3
 *
 *
 * Copyright (C) 2021 Holden Clarke, Licensed under the GNU General Public License v3
 * This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */
public class LibTNTLauncher {
    /* KINEMATICS EQUATIONS:
     *
     * v = v_0+at
     * x = x_0 + v_0*t + 1/2(a * t^2)
     * v^2 = (v_0)^2 + 2a(x - x_0)
     * x=vt
     */
    private static final double gravAccel = 19.8347;
    private static final double latConstant = 13.63; //VNot of TNT with a motion of 1
    private static final double yConstant = 21.81817; //VNot of TNT with a motion of 1
    private static double arcFactor= 1.25; //How much the arc should overcompensate

    public static void launch(World worldIn, EntityLivingBase igniter, BlockPos source, BlockPos destination){
        EntityTNTPrimed r = new EntityTNTPrimed(worldIn, source.getX(), source.getY(), source.getZ(), igniter);
        int xInc = 0;
        int zInc = 0;
        
        //Vertical Component
        double vertXNot = source.getY();
        double vertX = destination.getY();
        double vertVNot = Math.sqrt(2.0*(-19.8345)*((Math.max(vertXNot, vertX)*arcFactor)-Math.min(vertXNot, vertX)));
        double t = ((vertVNot*-1.0)-vertVNot)/gravAccel;
        
        //Horizontal Component
        double xLeg = Math.max(Math.abs(destination.getX()), Math.abs(source.getX())) - Math.min(Math.abs(destination.getX()), Math.abs(source.getX()));
        double zLeg = Math.max(Math.abs(destination.getZ()), Math.abs(source.getZ())) - Math.min(Math.abs(destination.getZ()), Math.abs(source.getZ()));
        double dist = Math.hypot(xLeg,zLeg);
        double horizV = dist*latConstant;
        //Determines whether X is increasing or decreasing
        if(destination.getX() > source.getX()){
            xInc = 1;
        }else if(destination.getX() < source.getX()){
            xInc = -1;
        }

        //Determines whether Z is increasing or decreasing
        if(destination.getZ() > source.getZ()){
            zInc = 1;
        }else if(destination.getZ() < source.getZ()){
            zInc = -1;
        }
        r.motionY = vertVNot/yConstant;
        r.motionX = ((dist - Math.pow(xLeg,2)/t)/latConstant)*xInc;
        r.motionX = ((dist - Math.pow(zLeg,2)/t)/latConstant)*zInc;
        worldIn.spawnEntity(r);
    }

    public static void launch(World worldIn, EntityLivingBase igniter, double sourceX, double sourceY, double sourceZ, BlockPos destination) throws NullPointerException{
        EntityTNTPrimed r = new EntityTNTPrimed(worldIn, sourceX, sourceY, sourceZ, igniter);
        int xInc = 0;
        int zInc = 0;

        //Vertical Component
        double vertXNot = sourceY;
        double vertX = destination.getY();
        double vertVNot = Math.sqrt(2.0*(19.8345)*((Math.max(vertXNot, vertX)*arcFactor)-Math.min(vertXNot, vertX)));
        double t = ((vertVNot*-1.0)-vertVNot)/gravAccel;

        //Horizontal Component
        double xLeg = Math.max(Math.abs(destination.getX()), Math.abs(sourceX)) - Math.min(Math.abs(destination.getX()), Math.abs(sourceX));
        double zLeg = Math.max(Math.abs(destination.getZ()), Math.abs(sourceZ)) - Math.min(Math.abs(destination.getZ()), Math.abs(sourceZ));
        double dist = Math.hypot(xLeg,zLeg);
        double horizV = dist/latConstant;
        //Determines whether X is increasing or decreasing
        if(destination.getX() > sourceX){
            xInc = 1;
        }else if(destination.getX() < sourceX){
            xInc = -1;
        }

        //Determines whether Z is increasing or decreasing
        if(destination.getZ() > sourceZ){
            zInc = 1;
        }else if(destination.getZ() < sourceZ){
            zInc = -1;
        }

        r.motionY = vertVNot/yConstant;
        r.motionX = ((horizV - Math.pow(xLeg,1)/t)/latConstant)*xInc;
        r.motionZ = ((horizV - Math.pow(zLeg,1)/t)/latConstant)*zInc;
        System.out.println("Motion Y, X, Z"+"\n"+
                r.motionY +"\n"+
                r.motionX +
                r.motionZ);
        worldIn.spawnEntity(r);
    }

}
