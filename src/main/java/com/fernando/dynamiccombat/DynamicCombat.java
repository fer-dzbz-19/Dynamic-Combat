package com.fernando.dynamiccombat;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraft.client.Minecraft;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(DynamicCombat.MODID)
public class DynamicCombat {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "dynamiccombat";
    // Create a new instance of the mod class and store it in a static field for easy access
    private final Map<UUID, ComboManager> comboManagers = new HashMap<>();
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "dynamiccombat" namespace
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    // Create a Deferred Register to hold Items which will all be registered under the "dynamiccombat" namespace
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "dynamiccombat" namespace
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    // Creates a new Block with the id "dynamiccombat:example_block", combining the namespace and path
    public static final DeferredBlock<Block> EXAMPLE_BLOCK = BLOCKS.registerSimpleBlock("example_block", BlockBehaviour.Properties.of().mapColor(MapColor.STONE));
    // Creates a new BlockItem with the id "dynamiccombat:example_block", combining the namespace and path
    public static final DeferredItem<BlockItem> EXAMPLE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("example_block", EXAMPLE_BLOCK);

    // Creates a new food item with the id "dynamiccombat:example_id", nutrition 1 and saturation 2
    public static final DeferredItem<Item> EXAMPLE_ITEM = ITEMS.registerSimpleItem("example_item", new Item.Properties().food(new FoodProperties.Builder()
            .alwaysEdible().nutrition(1).saturationModifier(2f).build()));

    // Creates a creative tab with the id "dynamiccombat:example_tab" for the example item, that is placed after the combat tab
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("example_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.dynamiccombat")) //The language key for the title of your CreativeModeTab
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> EXAMPLE_ITEM.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(EXAMPLE_ITEM.get());// Add the example item to the tab. For your own tabs, this method is preferred over the event
            }).build());

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public DynamicCombat(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register the Deferred Register to the mod event bus so blocks get registered
        BLOCKS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        ITEMS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so tabs get registered
        CREATIVE_MODE_TABS.register(modEventBus);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (DynamicCombat) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

        LOGGER.info("================================");
        LOGGER.info("       DYNAMIC COMBAT");
        LOGGER.info("        MOD CARREGADO!");
        LOGGER.info("================================");
        
        if (Config.LOG_DIRT_BLOCK.getAsBoolean()) {
            LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));
        }

        LOGGER.info("{}{}", Config.MAGIC_NUMBER_INTRODUCTION.get(), Config.MAGIC_NUMBER.getAsInt());

        Config.ITEM_STRINGS.get().forEach((item) -> LOGGER.info("ITEM >> {}", item));
    }

     private ComboManager getComboManager(Player player) {
        return comboManagers.computeIfAbsent(
            player.getUUID(),
            uuid -> new ComboManager()
        );
    }


   private boolean checkSwordCombo(ComboManager comboManager, AttackStrength strength) {

        if (comboManager.isCombo(1) && strength == AttackStrength.FULL) {
            LOGGER.info("SWORD COMBO 1 CORRETO!");
            return true;
        }
        else if (comboManager.isCombo(2) && strength == AttackStrength.MEDIUM) {
            LOGGER.info("SWORD COMBO 2 CORRETO!");
            return true;
        }
        else if (comboManager.isCombo(3) && strength == AttackStrength.MEDIUM) {
            LOGGER.info("SWORD COMBO 3 CORRETO!");
            return true;
        }
        else if (comboManager.isCombo(4) && strength == AttackStrength.MEDIUM) {
            LOGGER.info("SWORD COMBO 4 CORRETO!");
            return true;
        }
        else {
            LOGGER.info("SWORD COMBO INCORRETO!");
            comboManager.resetCombo();
            return false;
        }
    
    }
    
    // Identify the weapon used in the attack
    private WeaponType identifyWeapon(ItemStack weapon) {

        if (weapon.is(Items.WOODEN_SWORD)) {
            return WeaponType.SWORD;
        }

        if (weapon.is(Items.STONE_SWORD)) {
            return WeaponType.SWORD;
        }

        if (weapon.is(Items.IRON_SWORD)) {
            return WeaponType.SWORD;
        }

        if (weapon.is(Items.DIAMOND_SWORD)) {
            return WeaponType.SWORD;
        }

        if (weapon.is(Items.NETHERITE_SWORD)) {
            return WeaponType.SWORD;
        }

        if (weapon.is(Items.WOODEN_AXE)) {
            return WeaponType.AXE;
        }

        if (weapon.is(Items.STONE_AXE)) {
            return WeaponType.AXE;
        }

        if (weapon.is(Items.IRON_AXE)) {
            return WeaponType.AXE;
        }

        if (weapon.is(Items.DIAMOND_AXE)) {
            return WeaponType.AXE;
        }

        if (weapon.is(Items.NETHERITE_AXE)) {
            return WeaponType.AXE;
        }

        if (weapon.is(Items.BOW)) {
            return WeaponType.BOW;
        }

        if (weapon.is(Items.CROSSBOW)) {
            return WeaponType.CROSSBOW;
        }

        if (weapon.is(Items.TRIDENT)) {
            return WeaponType.TRIDENT;
        }

        if (weapon.is(Items.MACE)) {
            return WeaponType.MACE;
        }
        return WeaponType.UNKNOWN;
    }
        
    private double getWeaponDamage(WeaponType weaponType) {
        switch (weaponType) {
        case SWORD:
            return 7.0;
        case AXE:
            return 9.0;
        case MACE:
            return 10.0;
        case TRIDENT:
            return 8.0;
        case BOW:
            return 6.0;
        case CROSSBOW:
            return 7.0;
        default:
            return 1.0;
        }
    }


    @SubscribeEvent
    // This method will be called when a player attacks an entity
    public void onPlayerAttack(AttackEntityEvent event) {
        LOGGER.info("================================");
        if (event.getEntity().level().isClientSide()) {
            return;
        }
        Player attacker = event.getEntity();
        Entity target = event.getTarget();

        LOGGER.info("ATTACK DETECTED!");
        
        event.setCanceled(true);
        
        // Get the weapon used by the attacker
        ItemStack weapon = attacker.getMainHandItem();
        
        // Identify the weapon type and log the attack
        WeaponType weaponType = identifyWeapon(weapon);
        if (weaponType == WeaponType.SWORD) {
            LOGGER.info("SWORD ATTACK");
        }
        if (weaponType == WeaponType.AXE) {
            LOGGER.info("AXE ATTACK");
        }
        if (weaponType == WeaponType.BOW) {
            LOGGER.info("BOW ATTACK");
        }
        if (weaponType == WeaponType.CROSSBOW) {
            LOGGER.info("CROSSBOW ATTACK");
        }
        if (weaponType == WeaponType.TRIDENT) {
            LOGGER.info("TRIDENT ATTACK");
        }
        if (weaponType == WeaponType.MACE) {
            LOGGER.info("MACE ATTACK");
        }


        // Identify the attack strength based on the attack strength scale and log it
        float attackStrength = attacker.getAttackStrengthScale(0.0F);
        AttackStrength strength = identifyAttackStrength(attackStrength);
        
        // Increment the combo counter and reset the combo timer 
        ComboManager comboManager = getComboManager(attacker);

       comboManager.nextCombo();

        boolean validCombo = true;

        if (weaponType == WeaponType.SWORD) {
            validCombo = checkSwordCombo(comboManager, strength);
        }

        int combo = comboManager.getCombo();

        LOGGER.info("COMBO: {}", combo);
        LOGGER.info("COMBO TIMER: {}", comboManager.getComboTimer());
        

        // If the target is a LivingEntity, apply effects based on the weapon type
        if(target instanceof LivingEntity) {
            // Give the target a glow effect based on the weapon type
            LivingEntity livingTarget = (LivingEntity) target;
            
            DamageSource source =
            livingTarget.damageSources().playerAttack(attacker);
            
            double weaponDamage = getWeaponDamage(weaponType);

            double comboMultiplier = 1.0;

            if (validCombo) {
                comboMultiplier = getComboMultiplier(combo);
            }

            double finalDamage = weaponDamage * comboMultiplier;

            LOGGER.info("WEAPON DAMAGE: {}", weaponDamage);
            LOGGER.info("COMBO MULTIPLIER: {}", comboMultiplier);
            LOGGER.info("FINAL DAMAGE: {}", finalDamage);

            livingTarget.hurt(source, (float) finalDamage);

            switch (weaponType) {
                case SWORD:
                    livingTarget.addEffect(
                        new MobEffectInstance(MobEffects.GLOWING, 100, 0)
                );
                break;

                case AXE:
                    livingTarget.addEffect(
                        new MobEffectInstance(MobEffects.POISON, 100, 0)
                    );
                break;
                
                case MACE:
                    livingTarget.addEffect(
                        new MobEffectInstance(MobEffects.WEAKNESS, 100, 0)
                    );
                break;
            }
        }
        

        // Get the attack damage attribute of the attacker and log it
        AttributeInstance attributeInstance =
        attacker.getAttribute(Attributes.ATTACK_DAMAGE);
        
        // Log the attack damage value if the attribute instance is not null
        if (attributeInstance != null) {
            double attackDamage = attributeInstance.getValue();
            LOGGER.info("ATTACK DAMAGE: {}", attackDamage);
        }
        
        // Check for specific weapon and attack strength combinations
        if (weaponType == WeaponType.SWORD &&
            strength == AttackStrength.FULL) {

                LOGGER.info("SWORD FULL ATTACK!");
            }

        //vaos juntar a informação do ataque e logar no console
        // as informações que vamos juntar são o WeaponType e o WeaponStrength
        if (weaponType == WeaponType.MACE && strength == AttackStrength.FULL) {
            LOGGER.info("MACE CRITICAL ATTACK");
        }
        if (weaponType == WeaponType.AXE && strength == AttackStrength.FULL) {
            LOGGER.info("AXE CRITICAL ATTACK");
        }
        if (weaponType == WeaponType.SWORD && strength == AttackStrength.FULL) {
            LOGGER.info("SWORD CRITICAL ATTACK");
        }
        if (weaponType == WeaponType.TRIDENT && strength == AttackStrength.FULL) {
            LOGGER.info("TRIDENT CRITICAL ATTACK");
        }

        // Get the attack damage attribute of the attacker and log it
        double attackDamage = 0.0;
        if (attributeInstance != null) {
            attackDamage = attributeInstance.getValue();
        }     
        
        LOGGER.info("ATTACK DAMAGE: {}", attackDamage);
        LOGGER.info("ATTACK STRENGTH VALUE: {}", attackStrength);
        LOGGER.info("ATTACK STRENGTH TYPE: {}", strength);
   
    }

// Identify the attack strength based on the attack strength scale
    private AttackStrength identifyAttackStrength(float attackStrength) {
        if (attackStrength >= 1.0F) {
            return AttackStrength.FULL;
        }
        else if (attackStrength >= 0.75F) {
            return AttackStrength.STRONG;
        }
        else if (attackStrength >= 0.50F) {
            return AttackStrength.MEDIUM;
        }
        else {
            return AttackStrength.WEAK;
        }
    }

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent.Post event) {

        Player player = event.getEntity();

        if (player.level().isClientSide()) {
            return;
        }

        ComboManager comboManager = getComboManager(player);

        comboManager.tick();
    }

    private double getComboMultiplier(int combo) {
        switch (combo) {
            case 1:
                return 1.0;
            case 2:
                return 1.1;
            case 3:
                return 1.25;
            case 4:
                return 1.5;
            default:
                return 1.0;
        }
    
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.accept(EXAMPLE_BLOCK_ITEM);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @EventBusSubscriber(modid = DynamicCombat.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    static class ClientModEvents {
        @SubscribeEvent
        static void onClientSetup(FMLClientSetupEvent event) {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}