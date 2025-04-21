**MusicPlayerApp** — это Android-приложение для поиска, воспроизведения и управления музыкальными треками с использованием Deezer API.

Возможности
- Поиск треков по названию, артисту, альбому
- Отображение музыкального чарта (по умолчанию при запуске)
- Скачивание треков
- Воспроизведение музыки
- Переключение треков вперёд/назад
- Прогресс трека и seekBar
- Управление воспроизведением через уведомление (Foreground Service)

## Архитектура

Проект построен по принципам **Clean Architecture** и разделён на слои:

- `domain` — UseCase'ы и сущности
- `data` — репозитории, работа с Deezer API, локальное хранилище
- `app` — di, навигация
- `core_navigation` — навигация через интерфейсы и реализации
- `core-player` — работа с MediaPlayer
- `player-service` — работа плеера в фоне + уведомления
- `ui-player` — экран воспроизведения музыки
- `ui-saved-tracks` — экран сохраненной музыки
- `ui-search-tracks` — экран поиска музыки через API
- `ui-tracks-core` — адаптер, BaseFragment

## Технологии

- Kotlin + Coroutines + Flow
- Retrofit (сетевые запросы)
- Coil (загрузка обложек)
- MediaPlayer (воспроизведение)
- ForegroundService + Notification с кастомным layout
- Jetpack Navigation
- ViewModel + StateFlow
- Koin (внедрение зависимостей)
- SharedPreferences

Примечание
Для доступа к трекам используется публичный Deezer API.
Подключение к API не требует ключей или авторизации.

## Запуск
Клонируй репозиторий:
   ```bash
   git clone https://github.com/XimaDK/MusicPlayerApp.git

