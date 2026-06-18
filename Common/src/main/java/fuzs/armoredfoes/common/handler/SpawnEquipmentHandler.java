package fuzs.armoredfoes.common.handler;

import fuzs.armoredfoes.common.ArmoredFoes;
import fuzs.armoredfoes.common.config.ServerConfig;
import fuzs.armoredfoes.common.init.ModLootTables;
import fuzs.armoredfoes.common.init.ModRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jspecify.annotations.Nullable;

import java.util.Collections;

public class SpawnEquipmentHandler {

    public static void onEntityLoad(Entity entity, ServerLevel serverLevel, boolean isLoadedFromDisk, @Nullable EntitySpawnReason entitySpawnReason) {
        if (!isLoadedFromDisk && entity instanceof Mob mob) {
            // We cannot determine here when summoned via command if any specific nbt was specified which we should not interfere with (like purposefully setting empty equipment slots).
            // In that case, though, the spawn reason will be null, so we can filter that out like so.
            if (entitySpawnReason != null) {
                ResourceKey<EntityType<?>> entityType = entity.typeHolder().unwrapKey().orElseThrow();
                ResourceKey<LootTable> resourceKey = ModLootTables.createEntityEquipmentTable(entityType);
                LootTable lootTable = serverLevel.getServer().reloadableRegistries().getLootTable(resourceKey);
                if (lootTable != LootTable.EMPTY && prepareEquipmentSlots(mob)) {
                    mob.equip(resourceKey, Collections.emptyMap());
                }
            }
        }
    }

    private static boolean prepareEquipmentSlots(Mob mob) {
        // Mobs in this tag have their equipment cleared before trying to apply equipment from the loot table.
        if (mob.is(ModRegistry.DISCARDS_ORIGINAL_EQUIPMENT_ENTITY_TAG)) {
            for (EquipmentSlot equipmentSlot : ArmoredFoes.CONFIG.get(ServerConfig.class).clearedEquipmentSlots) {
                mob.setItemSlot(equipmentSlot, ItemStack.EMPTY);
            }
        } else {
            // All other mobs must have empty equipment slots.
            for (EquipmentSlot equipmentSlot : ArmoredFoes.CONFIG.get(ServerConfig.class).clearedEquipmentSlots) {
                if (!mob.getItemBySlot(equipmentSlot).isEmpty()) {
                    return false;
                }
            }
        }

        return true;
    }
}
