// ==========================================
// CustomRadarHUD.kts — Кастомный скриптовый HUD
// ==========================================

import me.collective.font.FontManager
import me.collective.render.RenderUtil
import java.awt.Color

// Регистрируем кастомный визуальный элемент HUD с координатами по умолчанию
val radarHud = ctx.hud("ScriptRadar", "Отображает радар сущностей через скрипт", 10.0f, 150.0f)
radarHud.setSize(120.0f, 40.0f)

radarHud.onRender { context, delta ->
    val player = mc.player ?: return@onRender
    val world = mc.world ?: return@onRender

    val count = world.entities.count { it != player && it.isAlive }

    // Отрисовка стильной подложки с границей
    RenderUtil.drawRect(context, 0.0f, 0.0f, 120.0f, 32.0f, Color(20, 20, 25, 180).rgb)
    RenderUtil.drawOutline(context, 0.0f, 0.0f, 120.0f, 32.0f, 1.0f, Color(130, 80, 240, 220).rgb)

    // Текстовая информация
    FontManager.INSTANCE.drawString(context, "§d⚡ Script Radar", 5.0f, 4.0f, Color.WHITE)
    FontManager.INSTANCE.drawString(context, "§7Сущностей рядом: §f$count", 5.0f, 18.0f, Color(220, 220, 220))
}

// Регистрируем кастомную команду чата
ctx.command("saycoords", "Отправляет координаты в чат", { args ->
    val p = mc.player ?: return@command
    ctx.sendChatMessage("Мои координаты: X=${p.blockX} Y=${p.blockY} Z=${p.blockZ}")
}, "coordsay", "sc")

print("Скрипт CustomRadarHUD.kts успешно загружен!")
