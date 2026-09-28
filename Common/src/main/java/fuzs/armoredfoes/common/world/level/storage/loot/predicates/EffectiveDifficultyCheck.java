package fuzs.armoredfoes.common.world.level.storage.loot.predicates;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProvider;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProviders;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.Set;

/**
 * A random condition based on {@link DifficultyInstance#getSpecialMultiplier()}.
 *
 * @see net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition
 */
public record EffectiveDifficultyCheck(Holder<ContextFloatProvider> chance) implements LootItemCondition {
    public static final MapCodec<EffectiveDifficultyCheck> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                    ContextFloatProviders.CODEC.fieldOf("chance").forGetter(EffectiveDifficultyCheck::chance))
            .apply(instance, EffectiveDifficultyCheck::new));

    @Override
    public MapCodec<? extends LootItemCondition> codec() {
        return MAP_CODEC;
    }

    @Override
    public boolean test(LootContext context) {
        DifficultyInstance difficulty = getCurrentDifficultyAt(context);
        if (difficulty != null) {
            float difficultyBasedChance = this.chance.value().getFloat(context) * difficulty.getSpecialMultiplier();
            return context.getRandom().nextFloat() < difficultyBasedChance;
        } else {
            return false;
        }
    }

    @Override
    public Set<ContextKey<?>> getReferencedContextParams() {
        return ImmutableSet.of(LootContextParams.ORIGIN);
    }

    public static @Nullable DifficultyInstance getCurrentDifficultyAt(LootContext context) {
        Vec3 origin = context.getOptional(LootContextParams.ORIGIN);
        if (origin != null) {
            BlockPos blockPos = BlockPos.containing(origin);
            return context.getLevel().getCurrentDifficultyAt(blockPos);
        } else {
            return null;
        }
    }

    public static LootItemCondition.Builder randomChance(float chance) {
        return () -> new EffectiveDifficultyCheck(ContextFloatProviders.exactly(chance));
    }

    public static LootItemCondition.Builder randomChance(Holder<ContextFloatProvider> chance) {
        return () -> new EffectiveDifficultyCheck(chance);
    }
}
