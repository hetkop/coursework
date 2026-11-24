# Мебельный магазин — мобильное приложение (Android, Kotlin)

Стек: Kotlin, Jetpack Compose (Material 3), Room (SQLite), KSP, MVVM.

## Запуск
1. Android Studio (Koala 2024.1 или новее) -> File -> Open -> папка проекта.
2. Дождаться Gradle Sync (нужен интернет, JDK 17 входит в Android Studio).
3. Запустить на эмуляторе или устройстве (Android 8.0+, minSdk 26).

## Структура
- data/Entities.kt — сущности Room (11 таблиц, ключи, индексы)
- data/StoreDao.kt — DAO: SQL-запросы (@Query), транзакции (@Transaction)
- data/AppDatabase.kt — RoomDatabase; в onCreate выполняются триггеры и начальные данные
- data/Schema.kt — SQL: 10 триггеров и начальные данные
- data/Repository.kt — слой над DAO, перевод ошибок СУБД в сообщения
- ui/ — экраны Compose, ViewModel, общие компоненты
- docs/schema_equivalent.sql — SQL, эквивалентный структуре таблиц, которую создаёт Room

## Роли
- Администратор: товары, заказы, клиенты, поставки, справочники, отчёты
- Руководитель: только отчёты

Примечание: проект собран без компиляции в Android Studio. При ошибках сборки пришлите текст ошибки.
