package com.headfishindustries.milkspray.entities;

import java.util.List;

import com.headfishindustries.milkspray.MilkSpray;

import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackData;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntitySplashMilk extends EntityThrowable{
	
	private boolean isLingering = false;
	
	/**
	 *
	 * Shamelessly ripped and tweaked from EntityPotion
	 * 
	 **/
	
	public EntitySplashMilk(World w) {
		super(w);
	}

	public EntitySplashMilk(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}
	
	public EntitySplashMilk(World worldIn, double x, double y, double z, boolean lingering) {
		this(worldIn, x, y, z);
		this.isLingering = lingering;
		
	}
	
	public EntitySplashMilk(World worldIn, EntityPlayer playerIn) {
		super(worldIn, playerIn);
	}
	 
	public EntitySplashMilk(World worldIn, EntityPlayer playerIn, boolean lingering) {
			this(worldIn, playerIn);
			this.isLingering = lingering;
	}
	 
	 

	protected float getGravityVelocity()
	    {
	        return 0.05F;
	    }


	    protected void onImpact(RayTraceResult result)
	    {
	        if (!this.world.isRemote)
	        {
	        	boolean flag = true;

	            if (result.typeOfHit == RayTraceResult.Type.BLOCK && flag)
	            {
	                BlockPos blockpos = result.getBlockPos().offset(result.sideHit);
	                this.extinguishFires(blockpos, result.sideHit);

	                for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
	                {
	                    this.extinguishFires(blockpos.offset(enumfacing), enumfacing);
	                }
	            }

	             if (this.isLingering())
	             {
	                    this.makeAreaOfEffectCloud();
	             }
	             else
	             {
	                    this.applySplash(result);
	             }


	            int i = 2007;
	            this.world.playEvent(i, new BlockPos(this), 0xFFFFFF);
	            this.setDead();
	        }
	    }


	    private void applySplash(RayTraceResult p_190543_1_)
	    {
	        AxisAlignedBB axisalignedbb = this.getEntityBoundingBox().grow(4.0D, 2.0D, 4.0D);
	        List<EntityLivingBase> list = this.world.<EntityLivingBase>getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb);

	        if (!list.isEmpty())
	        {
	            for (EntityLivingBase entitylivingbase : list)
	            {
	                if (entitylivingbase.canBeHitWithPotion())
	                {
	                    double d0 = this.getDistanceSq(entitylivingbase);

	                    if (d0 < 16.0D)
	                    {
	                        double d1 = 1.0D - Math.sqrt(d0) / 4.0D;

	                        if (entitylivingbase == p_190543_1_.entityHit)
	                        {
	                            d1 = 1.0D;
	                        }

	                        entitylivingbase.clearActivePotions();
	                    }
	                }
	            }
	        }
	    }

	    private void makeAreaOfEffectCloud()
	    {
	        EntityAreaEffectCloud entityareaeffectcloud = new EntityMilkCloud(this.world, this.posX, this.posY, this.posZ);
	        entityareaeffectcloud.setOwner(this.getThrower());
	        entityareaeffectcloud.setRadius(3.0F);
	        entityareaeffectcloud.setRadiusOnUse(-0.5F);
	        entityareaeffectcloud.setWaitTime(10);
	        entityareaeffectcloud.setRadiusPerTick(-entityareaeffectcloud.getRadius() / (float)entityareaeffectcloud.getDuration());
	       
	        
	        entityareaeffectcloud.setColor(0xFFFFFF);
	        

	        this.world.spawnEntity(entityareaeffectcloud);
	    }

	    private boolean isLingering()
	    {
	        return this.isLingering;
	    }

	    private void extinguishFires(BlockPos pos, EnumFacing p_184542_2_)
	    {
	        if (this.world.getBlockState(pos).getBlock() == Blocks.FIRE)
	        {
	            this.world.extinguishFire((EntityPlayer)null, pos.offset(p_184542_2_), p_184542_2_.getOpposite());
	        }
	    }

	    public static void registerFixesPotion(DataFixer fixer)
	    {
	        EntityThrowable.registerFixesThrowable(fixer, "ThrownPotion");
	        fixer.registerWalker(FixTypes.ENTITY, new ItemStackData(EntityPotion.class, new String[] {"Potion"}));
	    }

	    /**
	     * (abstract) Protected helper method to read subclass entity data from NBT.
	     */
	    public void readEntityFromNBT(NBTTagCompound compound)
	    {
	        super.readEntityFromNBT(compound);
	        this.isLingering = compound.getBoolean("LINGERING");
	       
	    }

	    /**
	     * (abstract) Protected helper method to write subclass entity data to NBT.
	     */
	    public void writeEntityToNBT(NBTTagCompound compound)
	    {
	        super.writeEntityToNBT(compound);

	        compound.setBoolean("LINGERING", this.isLingering());
	    }


	    public static class EntityMilkCloud extends EntityAreaEffectCloud{

			public EntityMilkCloud(World worldIn, double x, double y, double z) {
				super(worldIn, x, y, z);
			}
			
			/** Splitting long functions is overrated. **/
			@Override
			public void onUpdate()
		    {
		        super.onUpdate();
		        boolean flag = this.shouldIgnoreRadius();
		        float f = this.getRadius();

		        if (this.world.isRemote)
		        {
		            EnumParticleTypes enumparticletypes = this.getParticle();
		            int[] aint = new int[enumparticletypes.getArgumentCount()];

		            if (aint.length > 0)
		            {
		                aint[0] = this.getParticleParam1();
		            }

		            if (aint.length > 1)
		            {
		                aint[1] = this.getParticleParam2();
		            }

		            if (flag)
		            {
		                if (this.rand.nextBoolean())
		                {
		                    for (int i = 0; i < 2; ++i)
		                    {
		                        float f1 = this.rand.nextFloat() * ((float)Math.PI * 2F);
		                        float f2 = MathHelper.sqrt(this.rand.nextFloat()) * 0.2F;
		                        float f3 = MathHelper.cos(f1) * f2;
		                        float f4 = MathHelper.sin(f1) * f2;

		                        if (enumparticletypes == EnumParticleTypes.SPELL_MOB)
		                        {
		                            int j = this.rand.nextBoolean() ? 16777215 : this.getColor();
		                            int k = j >> 16 & 255;
		                            int l = j >> 8 & 255;
		                            int i1 = j & 255;
		                            this.world.spawnAlwaysVisibleParticle(EnumParticleTypes.SPELL_MOB.getParticleID(), this.posX + (double)f3, this.posY, this.posZ + (double)f4, (double)((float)k / 255.0F), (double)((float)l / 255.0F), (double)((float)i1 / 255.0F));
		                        }
		                        else
		                        {
		                            this.world.spawnAlwaysVisibleParticle(enumparticletypes.getParticleID(), this.posX + (double)f3, this.posY, this.posZ + (double)f4, 0.0D, 0.0D, 0.0D, aint);
		                        }
		                    }
		                }
		            }
		            else
		            {
		                float f5 = (float)Math.PI * f * f;

		                for (int k1 = 0; (float)k1 < f5; ++k1)
		                {
		                    float f6 = this.rand.nextFloat() * ((float)Math.PI * 2F);
		                    float f7 = MathHelper.sqrt(this.rand.nextFloat()) * f;
		                    float f8 = MathHelper.cos(f6) * f7;
		                    float f9 = MathHelper.sin(f6) * f7;

		                    if (enumparticletypes == EnumParticleTypes.SPELL_MOB)
		                    {
		                        int l1 = this.getColor();
		                        int i2 = l1 >> 16 & 255;
		                        int j2 = l1 >> 8 & 255;
		                        int j1 = l1 & 255;
		                        this.world.spawnAlwaysVisibleParticle(EnumParticleTypes.SPELL_MOB.getParticleID(), this.posX + (double)f8, this.posY, this.posZ + (double)f9, (double)((float)i2 / 255.0F), (double)((float)j2 / 255.0F), (double)((float)j1 / 255.0F));
		                    }
		                    else
		                    {
		                        this.world.spawnAlwaysVisibleParticle(enumparticletypes.getParticleID(), this.posX + (double)f8, this.posY, this.posZ + (double)f9, (0.5D - this.rand.nextDouble()) * 0.15D, 0.009999999776482582D, (0.5D - this.rand.nextDouble()) * 0.15D, aint);
		                    }
		                }
		            }
		        }
		        else
		        {
		            if (this.ticksExisted >= this.getDuration())
		            {
		                this.setDead();
		                return;
		            }

		            if (this.ticksExisted % 5 == 0)
		            {

		                {
		                    List<EntityLivingBase> list = this.world.<EntityLivingBase>getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox());

		                    if (!list.isEmpty())
		                    {
		                        for (EntityLivingBase entitylivingbase : list)
		                        {
		                            if (entitylivingbase.canBeHitWithPotion())
		                            {
		                                double d0 = entitylivingbase.posX - this.posX;
		                                double d1 = entitylivingbase.posZ - this.posZ;
		                                double d2 = d0 * d0 + d1 * d1;

		                                if (d2 <= (double)(f * f))
		                                {
		                                	entitylivingbase.clearActivePotions();   
		                                }
		                            }
		                        }
		                    }
		                }
		            }
		        }
		    }
	    	
	    }
	

	    public static class RenderMilkSplash extends RenderSnowball<EntitySplashMilk>{

			public RenderMilkSplash(RenderManager renderManagerIn, RenderItem itemRendererIn) {
				super(renderManagerIn, MilkSpray.items.milk_bottle, itemRendererIn);
			}
	    	
	    }
}
