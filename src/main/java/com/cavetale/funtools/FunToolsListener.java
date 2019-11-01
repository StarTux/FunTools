package com.cavetale.funtools;

import com.cavetale.dirty.Dirty;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@RequiredArgsConstructor
final class FunToolsListener implements Listener {
    private final FunToolsPlugin plugin;

    @EventHandler(ignoreCancelled = true)
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        ItemStack item;
        Player player = event.getPlayer();
        if (event.getItem().isSimilar(player.getInventory().getItemInMainHand())) {
            item = player.getInventory().getItemInMainHand();
        } else if (event.getItem().isSimilar(player.getInventory().getItemInOffHand())) {
            item = player.getInventory().getItemInMainHand();
        } else {
            return;
        }
        // The candy item from the Halloween eventu34
        if (hasCustomId(item, "halloween_candy")) {
            giveHalloweenPotionEffect(event.getPlayer());
            return;
        }
    }

    void giveHalloweenPotionEffect(Player player) {
        this.plugin.getLogger().info(player.getName() + " ate halloween candy");
        List<PotionEffectType> types = Arrays.asList(PotionEffectType.ABSORPTION,
                                                     PotionEffectType.DOLPHINS_GRACE,
                                                     PotionEffectType.FAST_DIGGING,
                                                     PotionEffectType.INCREASE_DAMAGE,
                                                     PotionEffectType.GLOWING,
                                                     PotionEffectType.FIRE_RESISTANCE,
                                                     PotionEffectType.REGENERATION,
                                                     PotionEffectType.SATURATION,
                                                     PotionEffectType.SPEED,
                                                     PotionEffectType.WATER_BREATHING,
                                                     PotionEffectType.LUCK);
        PotionEffectType pet = types.get(ThreadLocalRandom.current().nextInt(types.size()));
        int amplifier = ThreadLocalRandom.current().nextInt(3);
        int duration = 20 * 30 + ThreadLocalRandom.current().nextInt(40 * 30);
        player.addPotionEffect(new PotionEffect(pet, duration, amplifier));
        player.playSound(player.getEyeLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.MASTER, 0.25f, 2.0f);
    }

    // Legacy

    static <T> Optional<T> getMarker(@NonNull ItemStack item,
                                     @NonNull String key,
                                     @NonNull Class<T> clazz) {
        Optional<Object> tag = Dirty.accessItemNBT(item, false);
        if (!tag.isPresent()) return Optional.empty();
        tag = Dirty.getNBT(tag, key);
        Object result = Dirty.fromNBT(tag);
        if (!clazz.isInstance(result)) return Optional.empty();
        return Optional.of(clazz.cast(result));
    }

    static Optional<String> getCustomId(@NonNull ItemStack item) {
        return getMarker(item, "cavetale.id", String.class);
    }

    static boolean hasCustomId(@NonNull ItemStack item,
                               @NonNull String customId) {
        return customId.equals(getCustomId(item).orElse(null));
    }
}
