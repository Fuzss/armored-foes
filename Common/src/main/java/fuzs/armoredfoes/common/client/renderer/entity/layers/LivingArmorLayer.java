package fuzs.armoredfoes.common.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.armoredfoes.common.client.handler.EquipmentRenderingHandler;
import fuzs.puzzleslib.common.api.client.renderer.v1.RenderStateExtraData;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.ArmorModelSet;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.resources.model.EquipmentClientInfo.LayerType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.Equippable;
import org.jspecify.annotations.Nullable;

import java.util.Collections;

/**
 * @see HumanoidArmorLayer
 * @see fuzs.puzzleslib.common.api.client.renderer.v1.layers.SimpleHumanoidArmorLayer
 */
public class LivingArmorLayer<S extends LivingEntityRenderState, M extends EntityModel<S>, A extends EntityModel<S>> extends RenderLayer<S, M> {
    private final ArmorModelSet<A> modelSet;
    @Nullable
    private final ArmorModelSet<A> babyModelSet;
    private final EquipmentLayerRenderer equipmentRenderer;

    public LivingArmorLayer(RenderLayerParent<S, M> renderer, ArmorModelSet<A> modelSet, EquipmentLayerRenderer equipmentRenderer) {
        this(renderer, modelSet, modelSet, equipmentRenderer);
    }

    public LivingArmorLayer(RenderLayerParent<S, M> renderer, ArmorModelSet<A> modelSet, @Nullable ArmorModelSet<A> babyModelSet, EquipmentLayerRenderer equipmentRenderer) {
        super(renderer);
        this.modelSet = modelSet;
        this.babyModelSet = babyModelSet;
        this.equipmentRenderer = equipmentRenderer;
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, S state, float yRot, float xRot) {
        this.renderArmorPiece(poseStack, submitNodeCollector, EquipmentSlot.CHEST, lightCoords, state);
        this.renderArmorPiece(poseStack, submitNodeCollector, EquipmentSlot.LEGS, lightCoords, state);
        this.renderArmorPiece(poseStack, submitNodeCollector, EquipmentSlot.FEET, lightCoords, state);
        this.renderArmorPiece(poseStack, submitNodeCollector, EquipmentSlot.HEAD, lightCoords, state);
    }

    private void renderArmorPiece(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, EquipmentSlot slot, int lightCoords, S state) {
        ItemStack item = this.getItem(state, slot);
        Equippable equippable = item.get(DataComponents.EQUIPPABLE);
        if (equippable != null && HumanoidArmorLayer.shouldRender(equippable, slot)) {
            A model = this.getArmorModel(state, slot);
            if (model != null) {
                LayerType layerType = this.useBabyLayer(state) ? LayerType.HUMANOID_BABY :
                        this.usesInnerModel(slot) ? LayerType.HUMANOID_LEGGINGS : LayerType.HUMANOID;
                this.equipmentRenderer.renderLayers(layerType,
                        equippable.assetId().orElseThrow(),
                        model,
                        state,
                        item,
                        poseStack,
                        submitNodeCollector,
                        lightCoords,
                        state.outlineColor);
            }
        }
    }

    private ItemStack getItem(S state, EquipmentSlot slot) {
        return RenderStateExtraData.getOrDefault(state,
                EquipmentRenderingHandler.ARMOR_EQUIPMENT_KEY,
                Collections.emptyMap()).getOrDefault(slot, ItemStack.EMPTY);
    }

    private @Nullable A getArmorModel(S state, EquipmentSlot slot) {
        ArmorModelSet<A> modelSet = state.isBaby ? this.babyModelSet : this.modelSet;
        return modelSet != null ? modelSet.get(slot) : null;
    }

    private boolean usesInnerModel(EquipmentSlot slot) {
        return slot == EquipmentSlot.LEGS;
    }

    private boolean useBabyLayer(S state) {
        return state.isBaby && state.entityType != EntityTypes.ARMOR_STAND;
    }
}
