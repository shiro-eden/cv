# CV — Жуков Андрей

Интерактивное резюме на Compose Multiplatform (Kotlin/Wasm), дизайн — Material 3 Expressive
в духе шторки Android. Публикуется на GitHub Pages.

## Структура

- `shared/` — весь UI резюме (Compose), компоненты разложены по файлам в `shared/src/cv/`
- `webApp/` — точка входа для веба (`ComposeViewport`) и HTML-обвязка
- `shared/composeResources/drawable/avatar.svg` — фото-заглушка, замените на свою фотографию
  с GitHub (можно положить `avatar.png` и поменять `Res.drawable.avatar` на `Res.drawable.avatar_png`
  в `ProfilePhoto.kt`)

## Запуск

```sh
./kotlin run -m webApp
```

Локальный сервер на http://localhost:8080.

## Сборка для публикации

```sh
./kotlin build -m webApp -v release
```

Статические файлы появятся в `build/artifacts/CompiledWebArtifact/webAppwasmJsrelease/` —
их содержимое выкладывается на GitHub Pages.

## Данные резюме

Вся информация (имя, образование, контакты, проекты и их README) — в `shared/src/cv/CvData.kt`.
