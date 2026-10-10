// ==========================================
// CombatAssistant.kts — Мощный боевой скрипт
// ==========================================

import me.collective.category.Category
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.mob.Monster
import net.minecraft.util.Hand
import java.awt.Color

val combat = ctx.module("CombatAssistant", Category.COMBAT, "Автоматическая наводка, атака и ротации")

// Настройки модуля в ClickGUI
val rangeSetting = combat.numberSetting("Range", 4.2, 1.0, 6.0, 0.1)
val playersOnly = combat.boolSetting("PlayersOnly", true)
val silentAim = combat.boolSetting("SilentAim", true)

combat.onEnable {
    print("§a[CombatAssistant] Модуль активирован!")
}

combat.onDisable {
    print("§c[CombatAssistant] Модуль деактивирован.")
}

combat.onTick { mod ->
    val player = mc.player ?: return@onTick
    val world = mc.world ?: return@onTick

    // Поиск ближайшей валидной цели в радиусе
    val maxDist = rangeSetting.value
    var target: net.minecraft.entity.Entity? = null
    var closestDist = maxDist

    for (entity in world.entities) {
        if (entity == player) continue
        if (!entity.isAlive) continue

        if (playersOnly.value && entity !is PlayerEntity) continue
        if (!playersOnly.value && entity !is PlayerEntity && entity !is Monster) continue

        val dist = player.distanceTo(entity).toDouble()
        if (dist <= closestDist) {
            closestDist = dist
            target = entity
        }
    }

    if (target != null) {
        // Вычисляем целевой вектор и выполняем тихий серверный поворот
        val targetPos = target.pos.add(0.0, target.height * 0.75, 0.0)
        
        if (silentAim.value) {
            ctx.rotateTo(targetPos)
        }

        // Авто-атака с учетом кулдауна оружия 1.9+
        if (player.getAttackCooldownProgress(0.5f) >= 1.0f) {
            mc.interactionManager?.attackEntity(player, target)
            player.swingHand(Hand.MAIN_HAND)
        }
    }
}

print("Боевой скрипт CombatAssistant.kts успешно загружен!")
