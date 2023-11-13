package com.gamermachine.spookymod.items;

import java.util.function.Predicate;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

public class Flintlock extends ProjectileWeaponItem {
	
	public static final Predicate<ItemStack> IRON_NUGGETS = (item) -> {
		      return item.is(Items.IRON_NUGGET);};
		      
    private boolean hasUsed;
    //hasUsed is to make sure the gun doesn't fire while the player's intention is to reload it.
    //like a crossbow, the player has to reload it, release and then click again to fire.
    //since this value wont matter if the player leaves the server, we dont have to make an nbtTag.
    
    private boolean justIncreased;

	public Flintlock(Properties p_43009_) {
		super(p_43009_);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Predicate<ItemStack> getAllSupportedProjectiles() {
		
		return (t) -> {
			
			//my proudest piece of code as of the 30th of october
			
		    CompoundTag cTag = t.getOrCreateTag();
		    
		      if (!cTag.contains("loadedness")) {
		    	  
		    	  cTag.putInt("loadedness", 0);
		    	  
		      }
		      
		    if (cTag.getInt("loadedness") == 0) {
		    	
				return t.is(Items.GUNPOWDER);
		    	
		    } else
			
			    return t.is(Items.IRON_NUGGET);
			
		};
		
	}
	
	private static int getLoadedness(ItemStack iStack) {
		
	      CompoundTag cTag = iStack.getOrCreateTag();
	      
	      if (!cTag.contains("loadedness")) {
	    	  
	    	  cTag.putInt("loadedness", 0);
	    	  
	      }
	      
	      return cTag.getInt("loadedness");
		
		
	}
	
	private static void setLoadedness(ItemStack iStack, int value) {
		
	      CompoundTag cTag = iStack.getOrCreateTag();
	      
	      if (!cTag.contains("loadedness")) {
	    	  
	    	  cTag.putInt("loadedness", 0);
	    	  
	      }
	      
	      cTag.putInt("loadedness", value);
	      
	      if (value == 3) {
	    	  
		  cTag.putFloat("CustomModelData", 1f);
	    	  
	      } else
	      cTag.putFloat("CustomModelData", 0f);
	}

	@Override
	public int getDefaultProjectileRange() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	   public int getUseDuration(ItemStack iStack) {
		   
		   //Change duration based on what loadedness it's got
		   
		      int loadedness = getLoadedness(iStack);
		      
		      if (loadedness == 0) {
		    	  
		    	  return 20;
		    	  
		      } else if (loadedness == 1) {
		    	  
		    	  return 30;
		    	  
		    	  
		      } else return 4;
		    	  
		   }
	   
	   public void onUseTick(Level lev, LivingEntity lEntity, ItemStack iStack, int useTime) {
		   
		   if (!lev.isClientSide) {
			   
		   CompoundTag cTag = iStack.getOrCreateTag();
			   
		   int useDuration = this.getUseDuration(iStack);
		   
		   int loadedness = getLoadedness(iStack);
		   
		   if (useDuration - (useTime - 1) == useDuration && !(loadedness >= 3)) {
			   
			   /*
			    * since the item has multiple uses to be reloaded, unlike one like a crossbow, it is likely the loading action will
			    * occur during onUseTick. Since onUseTick doesn't happen when usetime is 0, we make the loading action happen
			    * when it is at 1. It is also possible the player releases use when it is a 1 or 0, and we have to make it happen
			    * there too, making sure we don't add one extra when it already happened, hence justIncreased.
			    */
			   
			      
			      justIncreased = this.loadIt(lEntity, iStack);
			   
		   }
		   
		   System.out.println("loadedness " + loadedness + " use tune " + useTime);
			   
			   
		   }
	   }
	
	 public void releaseUsing(ItemStack iStack, Level p_40876_, LivingEntity lEntity, int useTime) {
		 
		      int useDuration = this.getUseDuration(iStack) - useTime;
		      
		      int loadedness = getLoadedness(iStack);
		      
			   if (useDuration - (useTime - 1) >= useDuration && !(loadedness >= 3) && !justIncreased) {
				   
				   justIncreased = this.loadIt(lEntity, iStack);
				   
				   
				   
			   }
		      
		      System.out.println(loadedness);
		      
		      hasUsed = false;
		      justIncreased = false;
		      
		      //Here we reset hasUsed and justIncreased because the weapon has been released from use.

		   }
	 
	   public InteractionResultHolder<ItemStack> use(Level p_40920_, Player playah, InteractionHand iHand) {
		   
		   //iStack here represents the itemStack holding the flintlock item
		   
		      ItemStack iStack = playah.getItemInHand(iHand);
		  
		      int loadedness = getLoadedness(iStack);
		      
		      if (loadedness < 3 || hasUsed) {
		    	  
		    	  //flintlock not ready to fire, check if can load it
		    		  
		    	  hasUsed = true;
		    	  
		          playah.startUsingItem(iHand);
		          
		          //startUsingItem is important to make sure useTick and releaseUse work
		      
		      } else {
		    	  
		    	  //flintlock ready to fire, fire it
		    	  
		    	  if (!hasUsed) {
		    	  
		    	 System.out.println("fire");
		    	 
		    	 setLoadedness(iStack, 0);
		    	 
		         return InteractionResultHolder.consume(iStack);
		    	 
		    	  }
		    	  
		      }
		   
	         return InteractionResultHolder.pass(iStack);
		   
		   
	   }
	   
	   private boolean loadIt(LivingEntity lEntity, ItemStack iStack) {
		   
		      boolean flag = lEntity instanceof Player && ((Player)lEntity).getAbilities().instabuild;
		      
		      ItemStack itemstack = lEntity.getProjectile(iStack);
		      
		      int loadedness = getLoadedness(iStack);
		      
		         if (itemstack.isEmpty()) {
		        	 
		        	 if(flag) {
		        		 //creative mode check, infinite ammo
		        		 if (loadedness == 0) {
		        			 
		             itemstack = new ItemStack(Items.GUNPOWDER);
		             
		        	 } else if (loadedness == 1) {
		        		 
		        	 itemstack = new ItemStack(Items.IRON_NUGGET);
		        	 }

		          } else return false;
		         
	          }
		         //actual load script start
		         
		      itemstack.shrink(1);
		      
		      setLoadedness(iStack, loadedness +1);

		      return true;

	   }
	  
}
