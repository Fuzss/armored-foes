package fuzs.armoredfoes.common.init;

import fuzs.armoredfoes.common.ArmoredFoes;
import fuzs.armoredfoes.common.config.ServerConfig;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class ModTags {
    public static class EntityTypes {
        /**
         * Entities in this tag are allowed to show equipped armor with the custom armor render layers added by this
         * mod.
         * <p>
         * This filter exists as the provided layers are only applicable to mobs with the same size as their vanilla
         * counterparts which the layers are built for.
         */
        public static final TagKey<EntityType<?>> SHOWS_WORN_ARMOR_ENTITY_TAG = register("shows_worn_armor");
        /**
         * Entities in this tag have all their existing equipment cleared in the slots defined by
         * {@link ServerConfig#clearedEquipmentSlots} before new equipment from the equipment loot table is applied.
         * <p>
         * Note that it is possible for the loot table to provide no equipment at all. Clearing existing equipment
         * depends merely on the presence of the equipment loot table.
         */
        public static final TagKey<EntityType<?>> DISCARDS_ORIGINAL_EQUIPMENT_ENTITY_TAG = register(
                "discards_original_equipment");

        private static TagKey<EntityType<?>> register(String name) {
            return TagKey.create(Registries.ENTITY_TYPE, ArmoredFoes.id(name));
        }
    }
}
