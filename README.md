# CV — Жуков Андрей

Интерактивное резюме на Compose Multiplatform (Kotlin/Wasm), дизайн — Material 3 Expressive
в духе шторки Android. Публикуется на GitHub Pages.

## Структура

- `shared/` — весь UI резюме (Compose), компоненты разложены по файлам в `shared/src/cv/`
- `webApp/` — точка входа для веба (`ComposeViewport`) и HTML-обвязка
- `shared/composeResources/drawable/avatar.png` — фото-заглушка, просто замените этот файл
  своей фотографией с GitHub

## Запуск

```sh
./kotlin run -m webApp
```

Локальный сервер на http://localhost:8080.

## Сборка для публикации

```sh
./kotlin build -m webApp -v release
```

Готовый статический сайт (обработанный `index.html` + `webApp.wasm` + ресурсы):
`build/tasks/_webApp_buildWasmJsAppWasmJsRelease/` — его содержимое выкладывается
на GitHub Pages.

В репозитории есть готовый workflow `.github/workflows/pages.yml`: при пуше в
`main` сайт собирается и публикуется на GitHub Pages (включите
Settings → Pages → Source: GitHub Actions).

## Данные резюме

Вся информация (имя, образование, контакты, проекты и их README) — в `shared/src/cv/CvData.kt`.
