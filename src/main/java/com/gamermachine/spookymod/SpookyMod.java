package com.gamermachine.spookymod;

import com.gamermachine.spookymod.client.model.*;
import com.gamermachine.spookymod.client.renders.*;
import com.gamermachine.spookymod.entities.BulletEntity;
import com.gamermachine.spookymod.items.Flintlock;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.EntityRenderersEvent.*;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(SpookyMod.MODID)
public class SpookyMod
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "cedspookymod";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "examplemod" namespace
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    // Create a Deferred Register to hold Items which will all be registered under the "examplemod" namespace
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MODID);
    // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "examplemod" namespace
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    // Creates a new Block with the id "examplemod:example_block", combining the namespace and path
    public static final RegistryObject<Block> ORANGE_GRASS = BLOCKS.register("orange_grass", () -> new Block(BlockBehaviour.Properties.of().strength(0.6F).sound(SoundType.GRASS).mapColor(MapColor.COLOR_ORANGE)));
    // Creates a new BlockItem with the id "examplemod:example_block", combining the namespace and path
    public static final RegistryObject<Item> ORANGE_GRASS_ITEM = ITEMS.register("orange_grass", () -> new BlockItem(ORANGE_GRASS.get(), new Item.Properties()));

    // Creates a new food item with the id "examplemod:example_id", nutrition 1 and saturation 2
    public static final RegistryObject<Item> EXAMPLE_ITEM = ITEMS.register("example_item", () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
            .alwaysEat().nutrition(1).saturationMod(2f).build())));
    
    //public static final Item FLINTLOCK_ITEM = new Flintlock(new Item.Properties().stacksTo(1).durability(357));
    
    public static final RegistryObject<Item> FLINTLOCK = ITEMS.register("flintlock", () -> new Flintlock(new Item.Properties().stacksTo(1).durability(357)));

    // Creates a creative tab with the id "examplemod:example_tab" for the example item, that is placed after the combat tab
    public static final RegistryObject<CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("spookymod_tab", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> EXAMPLE_ITEM.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(EXAMPLE_ITEM.get()); // Add the example item to the tab. For your own tabs, this method is preferred over the event
            }).build());
    
 /*   static Supplier<EntityType<BulletEntity>> blankMind = () -> {EntityType.Builder.<BulletEntity>of(BulletEntity::new, MobCategory.MISC)
		.sized(0.4F, 0.7F)
		.clientTrackingRange(10)
		.build("bullet");}; */
		
	/*    static Supplier<EntityType<Arrow>> blankMind2 = () -> {EntityType.Builder.<Arrow>of(Arrow::new, MobCategory.MISC)
			.sized(0.4F, 0.7F)
			.clientTrackingRange(10)
			.build("randomp");}; */

	public static final RegistryObject<EntityType<BulletEntity>> BULLET = 
			ENTITIES.register("bullet", () -> EntityType.Builder.<BulletEntity>of(BulletEntity::new, MobCategory.MISC)
				.sized(0.4F, 0.7F)
				.clientTrackingRange(10)
				.build("bullet"));
	
	/* public static final RegistryObject<EntityType<Arrow>> RANDOMP = ENTITIES.register("randomp", () -> EntityType.Builder.<Arrow>of(Arrow::new, MobCategory.MISC)
		.sized(0.4F, 0.7F)
		.clientTrackingRange(10)
		.build("randomp")); */
    

    
    public SpookyMod()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register the Deferred Register to the mod event bus so blocks get registered
        BLOCKS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        ITEMS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so tabs get registered
        CREATIVE_MODE_TABS.register(modEventBus);
        
        ENTITIES.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);
        
        modEventBus.addListener(this::thisIsComplicated);
        
        modEventBus.addListener(this::regEntities);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.logDirtBlock)
            LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));

        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);

        Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS)
            event.accept(ORANGE_GRASS_ITEM);
    }
    
	private void thisIsComplicated(FMLClientSetupEvent event) {
    	
    	event.enqueueWork(() -> {
    		
    		//I'll always remember you, XFactHD, thanks for your help
    		
    		System.out.println("the item property seems to have registered");
    		
            ItemProperties.register(FLINTLOCK.get(), new ResourceLocation(MODID, "loadedness"), (iStack, wc1, wc2, wc3) -> {
            	CompoundTag cTag = iStack.getOrCreateTag();
            	
      	        return cTag.getInt("loadedness");
            	
            });
    		
    	});
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }
    
    public void regEntities(EntityRenderersEvent.RegisterRenderers event) {
    	event.registerEntityRenderer(BULLET.get(), BulletRender::new);
    	
    }
    
    /* public static void registerModels(ModelEvent.RegisterAdditional event) {
        event.register(BulletModel);
    } */

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}
