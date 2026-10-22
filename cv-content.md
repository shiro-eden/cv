# Контент CV (для переноса в Compose)

Все секции ниже соответствуют полям `data class Cv` из `shared/src/Cv.kt`.
Заполнить вручную: отмечено `[ВПИСАТЬ]`. В конце файла — готовый сниппет для `Cv.kt`.

---

## Шапка → `name`, `role`

- **name:** Жуков Андрей
- **role:** Начинающий Android-разработчик

## О себе → `about`

Студент 1 курса программы "Программирование и инжиниринг компьютерных игр" `[ВПИСАТЬ: вуз]`.
Пишу на Kotlin и Python: делаю Android-приложения (от калькуляторов до
каталога фильмов) и небольшие игры на Pygame. Сейчас собираю интерактивное
резюме на Compose Multiplatform — оно же эта страница. Веду конспекты
по математике в LaTeX.

## Навыки → `skills` (чипы)

Kotlin · Compose / Compose Multiplatform · Android SDK · MVVM (ViewModel, LiveData, DataBinding) ·
Python · Pygame · Git · Gradle · JUnit · LaTeX

> Список для `listOf(...)`:
> `"Kotlin", "Compose Multiplatform", "Android SDK", "MVVM", "ViewModel", "LiveData", "Python", "Pygame", "Git", "LaTeX"`

## Проекты → `jobs`

> В `Cv.kt` секция называется "Опыт работы" — для студента честнее
> переименовать в "Проекты" (`Cv.kt:215`). Маппинг полей:
> `company` → где лежит проект, `role` → тип проекта, `period` → год,
> `summary` → описание.

### 1. CV-сайт на Compose Multiplatform — 2026

- **company:** `github.com/shiro-eden/cv` `[ВПИСАТЬ: ссылку, когда выложите репозиторий]`
- **role:** Kotlin Multiplatform · Android + Web
- **period:** 2026
- **summary:** Интерактивное резюме на Jetpack Compose (Compose Multiplatform):
  одна кодовая база для Android и веба (wasmJS), Material 3, адаптивная
  раскладка, публикация на GitHub Pages.

### 2. EducationMovieApp — 2025

- **company:** `github.com/shiro-eden/EducationMovieApp`
- **role:** Android-приложение (учебный проект)
- **period:** 2025
- **summary:** Каталог фильмов: список с постерами и экран карточки фильма
  с рейтингом. Слоистая архитектура data / domain / ui (MVVM, ViewModel,
  репозиторий с интерфейсом в domain-слое), Fragment + XML, RecyclerView.

### 3. CalculateLogNormal — 2024

- **company:** `github.com/shiro-eden/CalculateLogNormal`
- **role:** Android-приложение
- **period:** 2024
- **summary:** Калькулятор логнормального распределения по введённым μ и σ².
  Архитектура MVVM: ViewModel + LiveData + DataBinding, валидация ввода,
  юнит-тесты вычислений.

### 4. EoSD-3D-Remake — 2024

- **company:** `github.com/shiro-eden/EoSD-3D-Remake`
- **role:** 3D-игра (учебный проект с защитой)
- **period:** 2024
- **summary:** Трёхмерный ремейк вертикального скролл-шутера Touhou 6:
  собственный 3D-движок на Pygame (отрисовка моделей, теней, анимация),
  6 уровней с боссами, 2 играбельных персонажа, меню, таблица рекордов.
  Вёл проект по этапам от альфы до защиты с документацией.

### 5. Конспекты по матанализу — 2025

- **company:** `github.com/shiro-eden/HSE-TeX-Calculus-2`
- **role:** LaTeX
- **period:** 2025
- **summary:** Конспекты курса математического анализа (2 семестр) в LaTeX,
  вёрстка формул и чертежей; участвовал в общем проекте конспектов hse-tex.

### 6. Rizumu — 2020–2021

- **company:** `github.com/shiro-eden/Rizumu`
- **role:** Ритм-игра (командный проект, 2 человека)
- **period:** 2020–2021
- **summary:** Ритм-игра на Pygame: 4 дорожки, одиночные и длинные ноты,
  персонажи с уникальными способностями, экраны выбора трека и персонажа.

### 7. Maps-API — 2021

- **company:** `github.com/shiro-eden/Maps-API`
- **role:** Десктоп-утилита
- **period:** 2021
- **summary:** Приложение для поиска адресов на карте: запросы к Yandex Maps API,
  отображение снимков карты, сохранение изображений.

> Ранние учебные репозитории (Lost, Yellow-sphere, git_project1,
> Saratov-Zhukov и т.п.) в CV не включал — они не добавляют информации.
> Если нужны все 15 — проще дать ссылку на профиль.

## Образование

- студент НИУ ВШЭ СПБ, программа "Программная инженерия", 1 курс, 2026 — 2030

## Контакты → `email`, `telegram`, `github`

- **email:** `[ВПИСАТЬ]`
- **telegram:** `[ВПИСАТЬ, напр. @shiro_eden]`
- **github:** `github.com/shiro-eden`

---

# Готовый сниппет для `Cv.kt`

Заменяете `demoCv` на это (и заголовок "Опыт работы" → "Проекты" в `Cv.kt:215`):

```kotlin
val myCv = Cv(
    name = "Жуков Андрей",
    role = "Начинающий Android-разработчик",
    about = "Студент 1 курса программы \"Программирование и инжиниринг компьютерных игр\". " +
        "Пишу на Kotlin и Python: Android-приложения и игры на Pygame. " +
        "Сейчас собираю интерактивное резюме на Compose Multiplatform — оно же эта страница.",
    skills = listOf(
        "Kotlin", "Compose Multiplatform", "Android SDK", "MVVM",
        "ViewModel", "LiveData", "Python", "Pygame", "Git", "LaTeX",
    ),
    jobs = listOf(
        Job(
            company = "github.com/shiro-eden/cv",
            role = "CV-сайт · Kotlin Multiplatform (Android + Web)",
            period = "2026",
            summary = "Интерактивное резюме на Compose Multiplatform: одна кодовая база " +
                "для Android и веба (wasmJS), Material 3, адаптивная раскладка, GitHub Pages.",
        ),
        Job(
            company = "github.com/shiro-eden/EducationMovieApp",
            role = "Android-приложение (учебный проект)",
            period = "2025",
            summary = "Каталог фильмов со списком и экраном деталей. Слоистая архитектура " +
                "data/domain/ui, MVVM (ViewModel), Fragment + XML, RecyclerView.",
        ),
        Job(
            company = "github.com/shiro-eden/CalculateLogNormal",
            role = "Android-приложение",
            period = "2024",
            summary = "Калькулятор логнормального распределения (μ, σ²). MVVM: ViewModel + " +
                "LiveData + DataBinding, валидация ввода, юнит-тесты вычислений.",
        ),
        Job(
            company = "github.com/shiro-eden/EoSD-3D-Remake",
            role = "3D-игра (учебный проект с защитой)",
            period = "2024",
            summary = "3D-ремейк вертикального шутера Touhou 6: собственный движок на Pygame " +
                "(модели, тени, анимация), 6 уровней, 2 персонажа, меню и рекорды.",
        ),
        Job(
            company = "github.com/shiro-eden/HSE-TeX-Calculus-2",
            role = "Конспекты по матанализу · LaTeX",
            period = "2025",
            summary = "Конспекты второго семестра матанализа в LaTeX, участие в общем " +
                "проекте конспектов hse-tex.",
        ),
        Job(
            company = "github.com/shiro-eden/Rizumu",
            role = "Ритм-игра (командный проект)",
            period = "2020–2021",
            summary = "Ритм-игра на Pygame: 4 дорожки, длинные ноты, персонажи " +
                "со способностями, экраны выбора трека и персонажа.",
        ),
        Job(
            company = "github.com/shiro-eden/Maps-API",
            role = "Десктоп-утилита",
            period = "2021",
            summary = "Поиск адресов и сохранение снимков карты через Yandex Maps API.",
        ),
    ),
    email = "aazhukov_15@edu.hse.ru",
    telegram = "@shiro_eden",       
    github = "github.com/shiro-eden",
)
```
