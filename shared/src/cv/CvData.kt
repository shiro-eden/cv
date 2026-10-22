package org.example.cv

/** Технология проекта: `label` — полное имя, `short` — подпись на значке в плитке. */
enum class Tech(val label: String, val short: String) {
    KOTLIN("Kotlin", "Kt"),
    ANDROID("Android", "An"),
    COMPOSE("Compose", "C"),
    KMP("KMP", "KM"),
    PYTHON("Python", "Py"),
    PYGAME("Pygame", "Pg"),
    LATEX("LaTeX", "TeX"),
    GIT("Git", "Git"),
    GRADLE("Gradle", "Gd"),
    MVVM("MVVM", "MV"),
    JUNIT("JUnit", "JU"),
    XML("XML", "XML"),
}

data class Project(
    val title: String,
    /** `owner/repo` — для ссылки на GitHub. */
    val repo: String,
    val year: String,
    val summary: String,
    val techs: List<Tech>,
    val readme: String,
) {
    val githubUrl: String get() = "https://github.com/$repo"
}

data class EducationEntry(
    val title: String,
    val detail: String,
)

enum class ContactType {
    EMAIL,
    TELEGRAM,
    GITHUB,
}

data class Contact(
    val type: ContactType,
    /** Текст, который показывается на экране. */
    val value: String,
    /** Куда ведёт клик. */
    val url: String,
)

data class CvProfile(
    val name: String,
    val role: String,
    val about: String,
    val education: List<EducationEntry>,
    val skills: List<String>,
    val contacts: List<Contact>,
    val projects: List<Project>,
)

private const val README_CV = """
# cv — интерактивное резюме

Резюме-сайт, который вы сейчас читаете: Compose Multiplatform, одна кодовая база
для веба (Kotlin/Wasm) и Android. Материал 3 Expressive: крупные скругления,
spring-анимации, «шторка» проектов как в Android.

## Как устроено

- левая панель — профиль: фото, образование, навыки, контакты;
- правая панель — «шторка» с проектами, по 4 плитки на странице;
- клик по плитке открывает README проекта (этот текст).

## Сборка

```sh
./kotlin run -m webApp
./kotlin build -m webApp -v release
```

Release-артефакты — статические файлы, которые публикуются на GitHub Pages.
"""

private const val README_MOVIES = """
# EducationMovieApp

Учебный каталог фильмов: список с постерами и экран карточки фильма с рейтингом.

## Что внутри

- слоистая архитектура `data` / `domain` / `ui`;
- MVVM: `ViewModel` переживает повороты экрана;
- репозиторий спрятан за интерфейсом в domain-слое — данные легко подменить.

## Технологии

Kotlin, Android SDK, Fragment + XML, RecyclerView, ViewModel + LiveData.
"""

private const val README_CALC = """
# CalculateLogNormal

Калькулятор логнормального распределения: вводите **μ** и **σ²** — приложение
считает плотность вероятности и основные характеристики распределения.

## Что внутри

- валидация ввода с сообщениями об ошибках;
- MVVM: `ViewModel` + `LiveData` + DataBinding;
- юнит-тесты вычислений.
"""

private const val README_EOSD = """
# EoSD-3D-Remake

Трёхмерный ремейк вертикального скролл-шутера **Touhou 6: Embodiment of Scarlet
Devil** на Pygame. Собственный 3D-движок: отрисовка моделей, теней, анимация.

## Что внутри

- 6 уровней с боссами;
- 2 играбельных персонажа;
- меню и таблица рекордов;
- проект вёлся по этапам — от альфы до защиты с документацией.
"""

private const val README_TEX = """
# HSE-TeX-Calculus-2

Конспекты второго семестра математического анализа в LaTeX: пределы, ряды,
кратные интегралы — с вёрсткой формул и чертежей.

Участвовал в общем проекте конспектов `hse-tex`.
"""

private const val README_RIZUMU = """
# Rizumu

Ритм-игра на Pygame, командный проект (2 человека): 4 дорожки, одиночные
и длинные ноты, персонажи с уникальными способностями.

## Что внутри

- экраны выбора трека и персонажа;
- подсчёт очков и комбо;
- герои меняют геймплей: у каждого своя способность.
"""

private const val README_MAPS = """
# Maps-API

Десктоп-утилита для поиска адресов: запросы к Yandex Maps API, отображение
снимков карты, сохранение изображений на диск.
"""

/** Все данные резюме в одном месте — правьте здесь. */
val cvProfile = CvProfile(
    name = "Жуков Андрей",
    role = "Начинающий Android-разработчик",
    about = "Студент 1 курса программы «Программная инженерия». Пишу на Kotlin и Python: " +
        "делаю Android-приложения (от калькуляторов до каталога фильмов) и небольшие игры " +
        "на Pygame. Сейчас собираю интерактивное резюме на Compose Multiplatform — оно же " +
        "эта страница. Веду конспекты по математике в LaTeX.",
    education = listOf(
        // TODO: впишите свой вуз
        EducationEntry(
            title = "Программная инженерия, 1 курс",
            detail = "2026 — 2030",
        ),
    ),
    skills = listOf(
        "Kotlin",
        "Compose Multiplatform",
        "Android SDK",
        "MVVM",
        "ViewModel",
        "LiveData",
        "Python",
        "Pygame",
        "Git",
        "LaTeX",
    ),
    contacts = listOf(
        // TODO: впишите свою почту
        Contact(ContactType.EMAIL, "you@example.com", "mailto:you@example.com"),
        // TODO: впишите свой Telegram
        Contact(ContactType.TELEGRAM, "@shiro_eden", "https://t.me/shiro_eden"),
        Contact(ContactType.GITHUB, "github.com/shiro-eden", "https://github.com/shiro-eden"),
    ),
    projects = listOf(
        Project(
            title = "cv",
            repo = "shiro-eden/cv",
            year = "2026",
            summary = "Интерактивное резюме на Compose Multiplatform: одна кодовая база для " +
                "Android и веба (wasmJS), Material 3, публикация на GitHub Pages.",
            techs = listOf(Tech.KOTLIN, Tech.COMPOSE, Tech.KMP, Tech.GRADLE),
            readme = README_CV,
        ),
        Project(
            title = "EducationMovieApp",
            repo = "shiro-eden/EducationMovieApp",
            year = "2025",
            summary = "Каталог фильмов: список с постерами и экран карточки фильма " +
                "с рейтингом. Слоистая архитектура data / domain / ui.",
            techs = listOf(Tech.KOTLIN, Tech.ANDROID, Tech.MVVM, Tech.XML),
            readme = README_MOVIES,
        ),
        Project(
            title = "CalculateLogNormal",
            repo = "shiro-eden/CalculateLogNormal",
            year = "2024",
            summary = "Калькулятор логнормального распределения по введённым μ и σ². " +
                "MVVM, валидация ввода, юнит-тесты вычислений.",
            techs = listOf(Tech.KOTLIN, Tech.ANDROID, Tech.MVVM, Tech.JUNIT),
            readme = README_CALC,
        ),
        Project(
            title = "EoSD-3D-Remake",
            repo = "shiro-eden/EoSD-3D-Remake",
            year = "2024",
            summary = "3D-ремейк вертикального скролл-шутера Touhou 6: собственный движок " +
                "на Pygame, 6 уровней с боссами, 2 персонажа, меню и рекорды.",
            techs = listOf(Tech.PYTHON, Tech.PYGAME, Tech.GIT),
            readme = README_EOSD,
        ),
        Project(
            title = "HSE-TeX-Calculus-2",
            repo = "shiro-eden/HSE-TeX-Calculus-2",
            year = "2025",
            summary = "Конспекты второго семестра матанализа в LaTeX: формулы и чертежи. " +
                "Участие в общем проекте конспектов hse-tex.",
            techs = listOf(Tech.LATEX, Tech.GIT),
            readme = README_TEX,
        ),
        Project(
            title = "Rizumu",
            repo = "shiro-eden/Rizumu",
            year = "2020 — 2021",
            summary = "Ритм-игра на Pygame (командный проект): 4 дорожки, длинные ноты, " +
                "персонажи со способностями, выбор трека и персонажа.",
            techs = listOf(Tech.PYTHON, Tech.PYGAME, Tech.GIT),
            readme = README_RIZUMU,
        ),
        Project(
            title = "Maps-API",
            repo = "shiro-eden/Maps-API",
            year = "2021",
            summary = "Поиск адресов на карте: запросы к Yandex Maps API, снимки карты, " +
                "сохранение изображений.",
            techs = listOf(Tech.PYTHON, Tech.GIT),
            readme = README_MAPS,
        ),
    ),
)
