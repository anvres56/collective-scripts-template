// ==========================================
// AutoJump.kts — Автоматический прыжок при беге
// ==========================================

import me.collective.category.Category

val jumpMod = ctx.module("AutoJump", Category.MOVEMENT, "Автоматически прыгает при зажатой клавише бега вперед")

jumpMod.onEnable {
    print("§aAutoJump включен!")
}

jumpMod.onDisable {
    print("§cAutoJump выключен.")
}

jumpMod.onTick { mod ->
    val player = mc.player ?: return@onTick
    val options = mc.options

    // Если игрок на земле и нажата кнопка движения вперед — прыгаем
    if (player.isOnGround && options.forwardKey.isPressed && !player.isSneaking) {
        player.jump()
    }
}

print("Скрипт AutoJump.kts успешно загружен!")
