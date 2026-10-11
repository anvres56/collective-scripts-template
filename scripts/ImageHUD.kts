// =========================================================================
// ImageHUD.kts — Кастомный HUD модуль для отображения выбранной картинки
// =========================================================================

import me.collective.font.FontManager
import me.collective.render.RenderUtil
import net.minecraft.util.Identifier
import java.awt.Color

// 1. Создаем HUD модуль с начальными координатами на экране
val imageHud = ctx.hud("ImageHUD", "Выводит выбранную картинку или логотип на экран", 10.0f, 60.0f)

// 2. Настройки модуля, доступные прямо в ClickGUI:
// - Выбор режима/типа картинки
val imageMode = imageHud.modeSetting("Image", "Collective", "Collective", "Mio", "Nav", "Warning", "Triangle")

// - Размеры картинки (слайдеры ширины и высоты)
val imgWidth = imageHud.numberSetting("Width", 64.0, 16.0, 256.0, 1.0)
val imgHeight = imageHud.numberSetting("Height", 64.0, 16.0, 256.0, 1.0)

// - Дополнительные визуальные эффекты
val showBg = imageHud.boolSetting("Background", true)
val showBorder = imageHud.boolSetting("Border", true)
val borderColor = imageHud.colorSetting("BorderColor", Color(130, 80, 240, 220))
val rotation = imageHud.numberSetting("Rotation", 0.0, 0.0, 360.0, 5.0)

// 3. Заранее подготавливаем идентификаторы встроенных текстур
val TEXTURE_COLLECTIVE = Identifier.of("collective", "textures/collective.png")
val TEXTURE_MIO        = Identifier.of("collective", "textures/mio.png")
val TEXTURE_NAV        = Identifier.of("collective", "textures/nav.png")
val TEXTURE_WARNING    = Identifier.of("collective", "textures/warning.png")
val TEXTURE_TRIANGLE   = Identifier.of("collective", "textures/triangle.png")

// 4. Отрисовка элемента HUD каждый кадр
imageHud.onRender { context, delta ->
    val w = imgWidth.value.toFloat()
    val h = imgHeight.value.toFloat()

    // Динамически обновляем габариты хитбокса для удобного перетаскивания в HudEditor
    imageHud.setSize(w, h)

    // Определяем текстуру по выбранному в настройках режиму
    val texture = when (imageMode.value.lowercase()) {
        "mio" -> TEXTURE_MIO
        "nav" -> TEXTURE_NAV
        "warning" -> TEXTURE_WARNING
        "triangle" -> TEXTURE_TRIANGLE
        else -> TEXTURE_COLLECTIVE
    }

    // Отрисовка темной подложки с матовым эффектом
    if (showBg.value) {
        RenderUtil.drawRect(context, 0.0f, 0.0f, w, h, Color(15, 15, 20, 160).rgb)
    }

    // Отрисовка выбранного изображения (с поддержкой вращения угла)
    val angle = rotation.value.toFloat()
    if (angle != 0.0f) {
        RenderUtil.drawRotatedImage(context, texture, 0.0f, 0.0f, w, h, angle, 0xFFFFFFFF.toInt())
    } else {
        context.drawTexture(
            net.minecraft.client.gl.RenderPipelines.GUI_TEXTURED,
            texture,
            0,
            0,
            0.0f,
            0.0f,
            w.toInt(),
            h.toInt(),
            w.toInt(),
            h.toInt(),
            0xFFFFFFFF.toInt()
        )
    }

    // Отрисовка стильной рамки поверх картинки
    if (showBorder.value) {
        RenderUtil.drawOutline(context, 0.0f, 0.0f, w, h, 1.0f, borderColor.value.rgb)
    }
}

// 5. Кастомная команда чата для быстрого переключения картинки
ctx.command("hudimage", "Сменить изображение в ImageHUD", { args ->
    if (args.isEmpty()) {
        print("§7Использование: §e.hudimage <Collective|Mio|Nav|Warning|Triangle>")
        return@command
    }
    val choice = args[0]
    imageMode.setValue(choice)
    print("§a[ImageHUD] Картинка успешно изменена на: §f${imageMode.value}")
}, "setimage", "img")

print("§a[ImageHUD] Скрипт успешно загружен! Включите модуль в ClickGUI или переместите в HudEditor.")
