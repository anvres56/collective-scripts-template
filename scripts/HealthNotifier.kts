// ==========================================
// HealthNotifier.kts — Оповещение при низком HP
// ==========================================

import me.collective.category.Category

val healthMod = ctx.module("HealthNotifier", Category.COMBAT, "Оповещает звуком и сообщением в чат при падении здоровья ниже порога")

var lastAlertTime = 0L

healthMod.onTick { mod ->
    val player = mc.player ?: return@onTick
    val health = player.health

    val now = System.currentTimeMillis()
    if (health <= 6.0f && (now - lastAlertTime > 3000L)) {
        lastAlertTime = now
        print("§c[ОПАСНОСТЬ] §eВаше здоровье критически низко: §4" + String.format("%.1f", health) + " HP!")
    }
}

print("Скрипт HealthNotifier.kts успешно загружен!")
