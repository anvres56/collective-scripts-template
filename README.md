# Collective Client — Kotlin Scripts Template & Documentation

Официальный репозиторий документации, руководств и готовых скриптов на **Kotlin (`.kts`)** для **Collective Client** (Minecraft 1.21.11 Fabric).

Скриптовый движок Collective Client позволяет создавать собственные чит-модули, макросы, перехватчики сетевых пакетов и автоматизацию без компиляции JAR-файлов — прямо на лету! Измените файл в текстовом редакторе, выполните команду `.scripts reload`, и изменения применятся мгновенно!

---

## 🚀 Возможности скриптов

- ⚡ **Мгновенная горячая перезагрузка (`Hot-Reload`)** — никаких перезапусков игры или компиляций Gradle!
- 🧩 **Полноценная интеграция в ClickGUI** — скриптовые модули отображаются в стандартном интерфейсе клиента, поддерживают бинды клавиш и сохранение состояния.
- 📦 **Доступ к Minecraft Client API** — прямое управление игроком, миром, инвентарем и сущностями через объект `mc`.
- 🌐 **Сетевой перехват пакетов** — поддержка обработки и отмены пакетов в реальном времени (`onPacketSend`, `onPacketReceive`).
- 🤖 **Связка с AI MCP Server** — модули, созданные в скриптах, мгновенно становятся видны нейросетям через встроенный MCP сервер Collective Client!

---

## 📁 Структура Репозитория

```text
collective-scripts-template/
├── docs/
│   └── API_REFERENCE.md      # Полная техническая документация API
├── scripts/
│   ├── AutoJump.kts          # Автоматический прыжок при беге вперед
│   ├── PacketLogger.kts      # Логирование сетевых пакетов клиента и сервера
│   ├── HealthNotifier.kts    # Оповещение и предупреждение при низком HP
│   └── CoordNotifier.kts     # Мониторинг и вывод координат игрока
└── README.md                 # Это руководство
```

---

## 📥 Как использовать скрипты в игре

1. Откройте папку скриптов Collective Client:
   - В чате игры напишите команду:
     ```
     .scripts folder
     ```
   - Либо откройте путь вручную:
     ```text
     .minecraft/collective/scripts/
     ```
2. Поместите любые `.kts` файлы из папки `scripts/` этого репозитория в папку `.minecraft/collective/scripts/`.
3. В чате игры выполните:
   ```
   .scripts reload
   ```
4. Откройте ClickGUI (по умолчанию правый `Shift` или `RShift`) — ваши новые модули появятся в соответствующих вкладках!

---

## 💡 Примеры Скриптов

### 1. Простой модуль: Авто-прыжок (`AutoJump.kts`)
```kotlin
import me.collective.category.Category

val jumpMod = ctx.module("AutoJump", Category.MOVEMENT, "Автоматически прыгает при беге")

jumpMod.onTick { mod ->
    val player = mc.player ?: return@onTick
    if (player.isOnGround && mc.options.forwardKey.isPressed && !player.isSneaking) {
        player.jump()
    }
}

print("AutoJump загружен!")
```

### 2. Перехват и фильтрация пакетов (`PacketLogger.kts`)
```kotlin
import me.collective.category.Category

val packetMod = ctx.module("PacketLogger", Category.MISC, "Логирует сетевые пакеты")

packetMod.onPacketSend { event ->
    val packetName = event.packet.javaClass.simpleName
    if (!packetName.contains("Move")) {
        log("[SEND] " + packetName)
    }
}
```

---

## 🎮 Команды скриптов в игре

| Команда | Описание |
|---------|----------|
| `.scripts list` | Выводит список всех загруженных скриптов и зарегистрированных ими модулей |
| `.scripts reload` | Горячая перезагрузка всех скриптов из папки `.minecraft/collective/scripts/` |
| `.scripts folder` | Открывает системную папку скриптов в проводнике |

---

## 📖 Полная документация API
Подробное описание всех методов, классов и доступных полей читайте в файле [API_REFERENCE.md](docs/API_REFERENCE.md).

---

## 📄 Лицензия
MIT License. Создавайте, делитесь и модифицируйте любые скрипты без ограничений.
