package fuzs.armoredfoes.common.world.level.storage.loot.functions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fuzs.armoredfoes.common.world.level.storage.loot.predicates.EffectiveDifficultyCheck;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.codec.RegistryFixedCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.providers.EnchantmentProvider;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.Optional;

/**
 * Apply enchantments from an {@link EnchantmentProvider}.
 *
 * @see net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction
 * @see net.minecraft.world.level.storage.loot.functions.EnchantWithLevelsFunction
 */
public class ApplyEnchantmentProviderFunction extends LootItemConditionalFunction {
    public static final MapCodec<ApplyEnchantmentProviderFunction> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> commonFields(
            instance).and(instance.group(RegistryFixedCodec.create(Registries.ENCHANTMENT_PROVIDER)
                    .fieldOf("provider")
                    .forGetter((ApplyEnchantmentProviderFunction function) -> function.provider)).t1())
            .apply(instance, ApplyEnchantmentProviderFunction::new));

    private final Holder<EnchantmentProvider> provider;

    public ApplyEnchantmentProviderFunction(Optional<Holder<LootItemCondition>> condition, Holder<EnchantmentProvider> provider) {
        super(condition);
        this.provider = provider;
    }

    @Override
    public MapCodec<? extends LootItemConditionalFunction> codec() {
        return MAP_CODEC;
    }

    @Override
    protected ItemStack run(ItemStack itemStack, LootContext context) {
        DifficultyInstance difficulty = EffectiveDifficultyCheck.getCurrentDifficultyAt(context);
        if (difficulty != null) {
            RegistryAccess registryAccess = context.getLevel().registryAccess();
            EnchantmentHelper.enchantItemFromProvider(itemStack,
                    registryAccess,
                    this.provider.unwrapKey().orElseThrow(),
                    difficulty,
                    context.getRandom());
        }

        return itemStack;
    }

    public static ApplyEnchantmentProviderFunction.Builder fromProvider(HolderGetter<EnchantmentProvider> enchantmentProviders, ResourceKey<EnchantmentProvider> resourceKey) {
        return new ApplyEnchantmentProviderFunction.Builder(enchantmentProviders.getOrThrow(resourceKey));
    }

    public static class Builder extends LootItemConditionalFunction.Builder<ApplyEnchantmentProviderFunction.Builder> {
        private final Holder<EnchantmentProvider> provider;

        public Builder(Holder<EnchantmentProvider> provider) {
            this.provider = provider;
        }

        @Override
        protected ApplyEnchantmentProviderFunction.Builder getThis() {
            return this;
        }

        @Override
        public LootItemFunction build() {
            return new ApplyEnchantmentProviderFunction(this.getCondition(), this.provider);
        }
    }
}
