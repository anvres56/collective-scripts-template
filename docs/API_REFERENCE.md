# Collective Client Scripting API Reference

Полная техническая спецификация мощного скриптового движка Collective Client. Скрипты имеют **неограниченный** доступ ко всем подсистемам клиента и игры.

---

## 🌟 Глобальные переменные в скриптах

| Переменная | Тип Java/Kotlin | Описание |
|---|---|---|
| `ctx` | `ScriptContext` | Контекст скрипта: создание модулей, команд, элементов HUD, управление ротациями, пакетами и Baritone |
| `mc` / `client` | `MinecraftClient` | Инстанс клиента Minecraft (`player`, `world`, `interactionManager`, `options`, `networkHandler`) |
| `modules` | `ModuleManager` | Менеджер модулей: доступ ко всем модулям чита, их состояниям и настройкам |
| `commands` | `CommandManager` | Менеджер команд чита: вызов любых встроенных команд через `commands.execute(".help")` |
| `events` | `EventBus` | Глобальная шина событий клиента (`subscribe`, `unsubscribe`, `post`) |
| `rotations` | `RotationUtils` | Серверный движок тихих и плавных ротаций без дергания камеры |
| `baritone` | `BaritoneBridge` | Прямое управление навигацией и поиском путей Baritone |
| `inv` | `InventoryUtil` | Поиск, свап предметов в хотбаре и инвентаре |
| `fonts` | `FontManager` | Отрисовка кастомного сглаженного CFont шрифта с кириллицей |
| `blur` | `BlurEngine` | Аппаратный шейдерный Frosted Glass Blur |
| `print(msg)` | `Consumer<Object>` | Вывод сообщения в чат игры с префиксом скрипта |
| `log(msg)` | `Consumer<Object>` | Логирование в системную консоль `stdout` |

---

## 1. Регистрация Модулей и Настроек (`ctx.module`)

Скрипт может регистрировать полноценный модуль, который появляется в интерфейсе ClickGUI:

```kotlin
val mod = ctx.module("Killaura", Category.COMBAT, "Автоматическая атака врагов")

// Создание интерактивных настроек для ClickGUI
val range = mod.numberSetting("Range", 3.8, 1.0, 6.0, 0.1)
val playersOnly = mod.boolSetting("PlayersOnly", true)
val espColor = mod.colorSetting("Color", java.awt.Color.RED)
val mode = mod.stringSetting("Mode", "Single")
```

### Поддерживаемые события модуля:
- `onEnable { mod -> ... }` — при активации
- `onDisable { mod -> ... }` — при деактивации
- `onTick { mod -> ... }` — каждый клиентский тик
- `onPacketSend { event -> ... }` — при отправке пакета (можно отменять: `event.cancel()`)
- `onPacketReceive { event -> ... }` — при приеме пакета от сервера (`event.cancel()`)
- `onRender3D { event -> ... }` — рендер в 3D мире (линии, боксы, трассеры)
- `onPlayerMove { event -> ... }` — модификация вектора перемещения игрока
- `onBreakBlock { event -> ... }` — начало ломания блока
- `onSendMessage { event -> ... }` — перехват сообщений перед отправкой в чат
- `onMouseClick { event -> ... }` — клики мыши
- `onKeyInput { event -> ... }` — нажатия клавиатуры
- `onInteractEntity { event -> ... }` — взаимодействие с сущностями

---

## 2. Создание Кастомных Элементов HUD (`ctx.hud`)

Скрипты могут добавлять собственные виджеты на экран, перемещаемые в `HudEditor`:

```kotlin
val myHud = ctx.hud("MyWatermark", "Кастомный ватермарк", 10.0f, 10.0f)
myHud.setSize(100.0f, 20.0f)

myHud.onRender { context, delta ->
    // context: DrawContext
    fonts.drawString(context, "§bCollective §fCustom Script", 2.0f, 2.0f, java.awt.Color.WHITE)
}
```

---

## 3. Регистрация Кастомных Команд Чайта (`ctx.command`)

```kotlin
ctx.command("teleport_macro", "Быстрый макрос", { args ->
    print("Вызвана команда со значением: ${args.joinToString()}")
}, "tpmacro", "tpm")
```

---

## 4. Методы автоматизации в `ctx`

- `ctx.rotateSilent(yaw, pitch)` — тихий поворот для сервера.
- `ctx.rotateTo(targetPos)` / `ctx.rotateTo(x, y, z)` — автоматическое вычисление углов и наводка на координаты.
- `ctx.sendPacket(packet)` — отправка любого сырого пакета Minecraft.
- `ctx.pathTo(x, y, z)` — запуск авто-навигации Baritone к целевому блоку.
- `ctx.cancelPath()` — отмена навигации Baritone.
- `ctx.sendChatMessage(msg)` — отправка сообщения или команды ванильного сервера (`/`).
- `ctx.executeClientCommand(cmd)` — вызов любой команды Collective Client (например, `.toggle Killaura`).
