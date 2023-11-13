package com.gamermachine.spookymod.client.renders;

import com.gamermachine.spookymod.entities.BulletEntity;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;

public class BulletRender extends EntityRenderer<BulletEntity>{
	private static final ResourceLocation TEXTURE = new ResourceLocation("cedspookymod:textures/entity/bullet_entity.png");

	public BulletRender(Context p_174008_) {
		super(p_174008_);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ResourceLocation getTextureLocation(BulletEntity p_114482_) {
		// TODO Auto-generated method stub
		return TEXTURE;
	}

}
