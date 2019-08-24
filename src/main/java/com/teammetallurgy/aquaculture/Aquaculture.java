package com.teammetallurgy.aquaculture;

import com.teammetallurgy.aquaculture.init.AquaEntities;
import com.teammetallurgy.aquaculture.init.AquaItems;
import com.teammetallurgy.aquaculture.item.crafting.ConditionFactory;
import com.teammetallurgy.aquaculture.loot.BiomeTagCheck;
import com.teammetallurgy.aquaculture.loot.FishReadFromJson;
import com.teammetallurgy.aquaculture.loot.FishWeightHandler;
import com.teammetallurgy.aquaculture.misc.AquaConfig;
import com.teammetallurgy.aquaculture.misc.ClientHandler;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;

@Mod(value = Aquaculture.MOD_ID)
public class Aquaculture {
    public final static String MOD_ID = "aquaculture";
    public static final Logger LOG = LogManager.getLogger(MOD_ID);
    public static final ItemGroup GROUP = new ItemGroup(Aquaculture.MOD_ID) {
        @Override
        @Nonnull
        public ItemStack createIcon() {
            return new ItemStack(AquaItems.IRON_FISHING_ROD);
        }
    };

    public Aquaculture() {
        final IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(this::setupCommon);
        modBus.addListener(this::setupClient);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, AquaConfig.spec);
    }

    private void setupCommon(FMLCommonSetupEvent event) {
        LootConditionManager.registerCondition(new BiomeTagCheck.Serializer());
        ConditionFactory.registerConditions();
        FishWeightHandler.registerFishData();
        AquaEntities.setSpawnPlacement();
        DeferredWorkQueue.runLater(AquaEntities::addEntitySpawns);
        DeferredWorkQueue.runLater(FishReadFromJson::addFishSpawns);
    }

    private void setupClient(FMLClientSetupEvent event) {
        ClientHandler.setupClient();
    }
}