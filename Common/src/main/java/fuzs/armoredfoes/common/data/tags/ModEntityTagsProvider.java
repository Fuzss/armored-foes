package fuzs.armoredfoes.common.data.tags;

import fuzs.armoredfoes.common.init.ModRegistry;
import fuzs.puzzleslib.common.api.data.v2.core.DataProviderContext;
import fuzs.puzzleslib.common.api.data.v2.tags.AbstractTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypeIds;

public class ModEntityTagsProvider extends AbstractTagProvider<EntityType<?>> {

    public ModEntityTagsProvider(DataProviderContext context) {
        super(Registries.ENTITY_TYPE, context);
    }

    @Override
    public void addTags(HolderLookup.Provider registries) {
        this.tag(ModRegistry.SHOWS_WORN_ARMOR_ENTITY_TAG)
                .add(EntityTypeIds.EVOKER,
                        EntityTypeIds.VINDICATOR,
                        EntityTypeIds.ILLUSIONER,
                        EntityTypeIds.PILLAGER,
                        EntityTypeIds.WITCH,
                        EntityTypeIds.VILLAGER,
                        EntityTypeIds.WANDERING_TRADER);
        // Piglins have some unique armor spawn rules in vanilla; we replace that with the same rules used by all other mobs.
        this.tag(ModRegistry.DISCARDS_ORIGINAL_EQUIPMENT_ENTITY_TAG).add(EntityTypeIds.PIGLIN);
    }
}
