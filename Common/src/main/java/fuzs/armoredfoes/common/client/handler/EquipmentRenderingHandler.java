package fuzs.armoredfoes.common.client.handler;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.armoredfoes.common.ArmoredFoes;
import fuzs.armoredfoes.common.client.model.geom.ModModelLayers;
import fuzs.armoredfoes.common.client.renderer.entity.layers.LivingArmorLayer;
import fuzs.armoredfoes.common.init.ModTags;
import fuzs.puzzleslib.common.api.client.renderer.v2.RenderStateExtraData;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.monster.illager.IllagerModel;
import net.minecraft.client.model.monster.witch.WitchModel;
import net.minecraft.client.model.npc.BabyVillagerModel;
import net.minecraft.client.model.npc.VillagerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.ArmorModelSet;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.state.*;
import net.minecraft.core.Holder;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.illager.AbstractIllager;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

public class EquipmentRenderingHandler {
    public static final ContextKey<Map<EquipmentSlot, ItemStack>> ARMOR_EQUIPMENT_KEY = new ContextKey<>(ArmoredFoes.id(
            "armor_equipment"));

    public static void onExtractRenderState(Entity entity, EntityRenderState renderState, float partialTick) {
        // IllagerRenderState extends HumanoidRenderState as of Minecraft 1.21.11 but does not set up most of the properties.
        if ((!(renderState instanceof HumanoidRenderState) || renderState instanceof IllagerRenderState)
                && entity instanceof LivingEntity livingEntity && entity.is(ModTags.EntityTypes.SHOWS_WORN_ARMOR_ENTITY_TAG)) {
            ImmutableMap.Builder<EquipmentSlot, ItemStack> builder = ImmutableMap.builder();
            for (EquipmentSlot equipmentSlot : EquipmentSlotGroup.ARMOR) {
                builder.put(equipmentSlot, HumanoidMobRenderer.getEquipmentIfRenderable(livingEntity, equipmentSlot));
            }

            RenderStateExtraData.set(renderState, ARMOR_EQUIPMENT_KEY, builder.build());
        }
    }

    @SuppressWarnings("unchecked")
    public static void addLivingEntityRenderLayers(EntityType<?> entityType, LivingEntityRenderer<?, ?, ?> entityRenderer, EntityRendererProvider.Context context) {
        Holder<EntityType<?>> holder = entityType.builtInRegistryHolder();
        if (entityRenderer.getModel() instanceof IllagerModel<?>) {
            ((LivingEntityRenderer<?, IllagerRenderState, IllagerModel<IllagerRenderState>>) entityRenderer).addLayer(
                    new LivingArmorLayer<>((LivingEntityRenderer<?, IllagerRenderState, IllagerModel<IllagerRenderState>>) entityRenderer,
                            ModModelLayers.bake(ModModelLayers.ILLAGER_CROSSED_ARMOR,
                                    context.getModelSet(),
                                    IllagerModel::new),
                            null,
                            context.getEquipmentRenderer()) {
                        @Override
                        public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, IllagerRenderState state, float yRot, float xRot) {
                            if (holder.is(ModTags.EntityTypes.SHOWS_WORN_ARMOR_ENTITY_TAG)
                                    && state.armPose == AbstractIllager.IllagerArmPose.CROSSED) {
                                super.submit(poseStack, submitNodeCollector, lightCoords, state, yRot, xRot);
                            }
                        }
                    });
            ((LivingEntityRenderer<?, IllagerRenderState, IllagerModel<IllagerRenderState>>) entityRenderer).addLayer(
                    new LivingArmorLayer<>((LivingEntityRenderer<?, IllagerRenderState, IllagerModel<IllagerRenderState>>) entityRenderer,
                            ModModelLayers.bake(ModModelLayers.ILLAGER_ARMOR, context.getModelSet(), IllagerModel::new),
                            null,
                            context.getEquipmentRenderer()) {
                        @Override
                        public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, IllagerRenderState state, float yRot, float xRot) {
                            if (holder.is(ModTags.EntityTypes.SHOWS_WORN_ARMOR_ENTITY_TAG)
                                    && state.armPose != AbstractIllager.IllagerArmPose.CROSSED) {
                                super.submit(poseStack, submitNodeCollector, lightCoords, state, yRot, xRot);
                            }
                        }
                    });
        } else if (entityRenderer.getModel() instanceof WitchModel) {
            ((LivingEntityRenderer<?, WitchRenderState, WitchModel>) entityRenderer).addLayer(new LivingArmorLayer<>((LivingEntityRenderer<?, WitchRenderState, WitchModel>) entityRenderer,
                    ModModelLayers.bake(ModModelLayers.WITCH_ARMOR, context.getModelSet(), WitchModel::new),
                    null,
                    context.getEquipmentRenderer()) {
                @Override
                public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, WitchRenderState state, float yRot, float xRot) {
                    if (holder.is(ModTags.EntityTypes.SHOWS_WORN_ARMOR_ENTITY_TAG)) {
                        super.submit(poseStack, submitNodeCollector, lightCoords, state, yRot, xRot);
                    }
                }
            });
        } else if (entityRenderer.getModel() instanceof VillagerModel) {
            ((LivingEntityRenderer<?, VillagerRenderState, VillagerModel>) entityRenderer).addLayer(new LivingArmorLayer<>(
                    (LivingEntityRenderer<?, VillagerRenderState, VillagerModel>) entityRenderer,
                    ModModelLayers.bake(ModModelLayers.VILLAGER_ARMOR, context.getModelSet(), VillagerModel::new),
                    ModModelLayers.bake(ModModelLayers.VILLAGER_BABY_ARMOR,
                            context.getModelSet(),
                            BabyVillagerModel::new),
                    context.getEquipmentRenderer()) {
                @Override
                public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, VillagerRenderState state, float yRot, float xRot) {
                    if (holder.is(ModTags.EntityTypes.SHOWS_WORN_ARMOR_ENTITY_TAG)) {
                        super.submit(poseStack, submitNodeCollector, lightCoords, state, yRot, xRot);
                    }
                }
            });
        } else if (entityRenderer.getModel() instanceof HumanoidModel<?>) {
            ((LivingEntityRenderer<?, HumanoidRenderState, HumanoidModel<HumanoidRenderState>>) entityRenderer).addLayer(
                    new HumanoidArmorLayer<>((LivingEntityRenderer<?, HumanoidRenderState, HumanoidModel<HumanoidRenderState>>) entityRenderer,
                            ArmorModelSet.bake(ModModelLayers.HUMANOID_ARMOR,
                                    context.getModelSet(),
                                    HumanoidModel::new),
                            ArmorModelSet.bake(ModModelLayers.HUMANOID_BABY_ARMOR,
                                    context.getModelSet(),
                                    HumanoidModel::new),
                            context.getEquipmentRenderer()) {
                        @Override
                        public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, HumanoidRenderState state, float yRot, float xRot) {
                            if (holder.is(ModTags.EntityTypes.SHOWS_WORN_ARMOR_ENTITY_TAG)) {
                                super.submit(poseStack, submitNodeCollector, lightCoords, state, yRot, xRot);
                            }
                        }
                    });
        }
    }
}
