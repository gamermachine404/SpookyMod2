package com.gamermachine.spookymod.entities;

import java.util.Collection;
import java.util.Map;

import com.google.common.collect.Multimap;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class BulletEntity extends Projectile{
	
	private int damage;
	private int armorPiercing;
	
	private boolean isSuperPiercing;
	private boolean isArcanePiercing;

	public BulletEntity(EntityType<? extends BulletEntity> p_37248_, Level p_37249_) {
		super(p_37248_, p_37249_);
		System.out.println("hi my name is mason and I exist");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void defineSynchedData() {
		// TODO Auto-generated method stub
		
	}
	
	protected void onHitEntity(EntityHitResult ehr) {
		System.out.println("a HIT!");
	      super.onHitEntity(ehr);
	      Entity entity = ehr.getEntity();
	      
	      double armor;
	      double armorToughness;
	      int effectiveEPL;
	      
	      if (entity.getType() == EntityType.ENDERMAN) {  
	    	  return;
	      }
	      
	      armor = getEntityDefensePoints(entity);
			System.out.println(armor);
	      
	      armorToughness = (this.isSuperPiercing == true) ? getEntityToughnessPoints(entity) : 0d;
	      System.out.println(armorToughness);
	      
	      
		
	}
	
	private static double getEntityDefensePoints(Entity entity) {
		
	      Iterable<ItemStack> armorStacks = entity.getArmorSlots();
	      
  	  double totalDefensePoints = 0d;
	      
	      for (ItemStack ais : armorStacks) {
	    	  
	    	  double totalArmorDefensePoints = 0d;
	    	 
	    	 EquipmentSlot eSlot = ais.getEquipmentSlot();
	    	 
           Multimap<Attribute, AttributeModifier> aModifs = ais.getAttributeModifiers(eSlot);
           
           Collection<AttributeModifier> aArmor = aModifs.get(Attributes.ARMOR);
           
           
           for (AttributeModifier v1: aArmor) {
          	 totalArmorDefensePoints = totalArmorDefensePoints + v1.getAmount();
           }
           
           totalDefensePoints = totalDefensePoints + totalArmorDefensePoints;
	    	 
	    	  
	    	  
	      }
	      
	      return totalDefensePoints;
		
		
	}
	
	private static double getEntityToughnessPoints(Entity entity) {
		
	      Iterable<ItemStack> armorStacks = entity.getArmorSlots();
	      
  	  double totalATP = 0d;
	      
	      for (ItemStack ais : armorStacks) {
	    	  
	    	  double totalArmorTP = 0d;
	    	 
	    	 EquipmentSlot eSlot = ais.getEquipmentSlot();
	    	 
           Multimap<Attribute, AttributeModifier> aModifs = ais.getAttributeModifiers(eSlot);
           
           Collection<AttributeModifier> aArmorToughness = aModifs.get(Attributes.ARMOR_TOUGHNESS);
           
           for (AttributeModifier v1: aArmorToughness) {
          	 totalArmorTP = totalArmorTP + v1.getAmount();
           }
	    	 
           totalATP = totalATP + totalArmorTP;
	    	  
	      }
	      return totalATP;
	}
	
	private static int getEntityEPL(Entity entity) {
		
	     Iterable<ItemStack> armorStacks = entity.getArmorSlots();
	     
	   int totalEPL = 0;
	   
	   for (ItemStack ais : armorStacks) {
		   
		   Map<Enchantment, Integer> enchMap = ais.getAllEnchantments();
		   
		   Integer prot1 = enchMap.get(Enchantments.ALL_DAMAGE_PROTECTION);
		   Integer prot2 = enchMap.get(Enchantments.PROJECTILE_PROTECTION);
		   
		   totalEPL = totalEPL + prot1.intValue();
		   totalEPL = totalEPL + prot2.intValue();
		   
	   }
	   
	   return totalEPL;
	}

}
