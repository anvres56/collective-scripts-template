# Collective Client Scripting API Reference

Полная техническая спецификация встроенного скриптового движка Collective Client.

## Встроенные переменные (Script Bindings)

В каждом выполняемом скрипте автоматически доступны следующие глобальные объекты:

| Переменная | Тип Java/Kotlin | Описание |
|------------|-----------------|----------|
| `ctx` | `me.collective.scripting.ScriptContext` | Контекст скрипта: управление жизненным циклом, регистрация модулей |
| `mc` / `client` | `net.minecraft.client.MinecraftClient` | Синглтон клиента Minecraft (доступ к `mc.player`, `mc.world`, `mc.options` и т.д.) |
| `print(msg)` | `Consumer<Object>` | Выводит форматированное сообщение в чат игрока с префиксом скрипта |
| `log(msg)` | `Consumer<Object>` | Выводит сообщение в стандартный вывод консоли (`stdout`) |

---

## 1. Регистрация модулей через `ctx.module(...)`

Скрипт может регистрировать один или несколько полноценных модулей, которые сразу же появляются в интерфейсе ClickGUI Collective Client!

### Сигнатуры метода:
- `ctx.module(name: String, category: Category, description: String): ScriptModule`
- `ctx.module(name: String, category: Category): ScriptModule`
- `ctx.module(name: String, categoryName: String): ScriptModule`

### Категории (`me.collective.category.Category`):
- `Category.COMBAT` — Боевые модули
- `Category.MOVEMENT` — Модули передвижения
- `Category.RENDER` — Визуальные модули
- `Category.PLAYER` — Модули взаимодействия с игроком
- `Category.MISC` — Разное

---

## 2. События модуля (`ScriptModule`)

Объект `ScriptModule` предоставляет следующие обработчики жизненного цикла:

```kotlin
val myMod = ctx.module("MyCustomMod", Category.MISC, "Описание модуля")

// Вызывается при включении модуля игроком
myMod.onEnable {
    print("Модуль включен")
}

// Вызывается при выключении модуля
myMod.onDisable {
    print("Модуль выключен")
}

// Вызывается каждый тик клиента (20 раз в секунду) пока модуль активен
myMod.onTick { mod ->
    val player = mc.player ?: return@onTick
    // логика тика...
}

// Вызывается при отправке пакета на сервер
myMod.onPacketSend { event ->
    // event.packet — объект Packet<?>
    // event.cancel() — отменить отправку пакета
}

// Вызывается при получении пакета от сервера
myMod.onPacketReceive { event ->
    // event.packet — объект Packet<?>
    // event.cancel() — отменить обработку пакета
}
```

---

## 3. Управление жизненным циклом скриптов

При выгрузке или перезагрузке скрипта (`.scripts reload`):
1. Все модули, созданные в контексте этого скрипта, автоматически выключаются (`onDisable`).
2. Модули удаляются из глобального `ModuleManager` и из GUI.
3. Очищаются все ссылки, предотвращая утечки памяти.
