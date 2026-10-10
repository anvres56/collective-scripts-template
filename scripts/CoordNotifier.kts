// ==========================================
// CoordCopier.kts — Копирование координат игрока
// ==========================================

import me.collective.category.Category

val coordMod = ctx.module("CoordNotifier", Category.RENDER, "Отображает и логирует текущие координаты")

var lastSecond = 0L

coordMod.onTick { mod ->
    val player = mc.player ?: return@onTick
    val now = System.currentTimeMillis()

    // Раз в 10 секунд
    if (now - lastSecond > 10000L) {
        lastSecond = now
        val x = player.blockX
        val y = player.blockY
        val z = player.blockZ
        print("§7Ваши координаты: §bX: §f$x §bY: §f$y §bZ: §f$z")
    }
}

print("Скрипт CoordNotifier.kts успешно загружен!")
