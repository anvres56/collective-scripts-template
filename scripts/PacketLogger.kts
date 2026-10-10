// ==========================================
// PacketLogger.kts — Логирование сетевых пакетов
// ==========================================

import me.collective.category.Category

val packetMod = ctx.module("PacketLogger", Category.MISC, "Логирует отправленные и полученные пакеты Minecraft")

packetMod.onEnable {
    print("§aPacketLogger активирован!")
}

packetMod.onPacketSend { event ->
    val packetName = event.packet.javaClass.simpleName
    // Фильтруем слишком частые пакеты движения для чистоты лога
    if (!packetName.contains("Move") && !packetName.contains("PlayerInput")) {
        log("[SEND] " + packetName)
    }
}

packetMod.onPacketReceive { event ->
    val packetName = event.packet.javaClass.simpleName
    if (!packetName.contains("Chunk") && !packetName.contains("EntityPosition")) {
        log("[RECV] " + packetName)
    }
}

print("Скрипт PacketLogger.kts успешно загружен!")
