<div align="center">

<img src="docs/icon.png" width="120" alt="DailyQuote icon" />

# DailyQuote · 每日一句

**One good sentence a day, on a frosted-glass card.**

Blue / white / pink palette · frosted glass · large radii · buttery motion

![Java](https://img.shields.io/badge/Java-8+-orange?style=flat-square)
![minSdk](https://img.shields.io/badge/minSdk-24-brightgreen?style=flat-square)
![Dependencies](https://img.shields.io/badge/dependencies-0-success?style=flat-square)
![License](https://img.shields.io/badge/license-MIT-blue?style=flat-square)

</div>

---

## What it is

A tiny Android app — a single `Activity`, ~600 lines of Java, **zero third-party dependencies**.

Open it and you get one translucent card with a quote inside. Tap the card or the button and the quote fades out, drifts up, then the next one fades in. Save the ones you like; they are still there next time.

No login. No network. No permissions.

## Features

- Frosted-glass card: translucent white, 1dp highlight stroke, 28dp radius, artwork showing through
- Three tabs — Today / Saved / Me — with a QQ-style pill navigation bar and an elastic sliding indicator
- 32 built-in quotes, random pick with no immediate repeats
- Saved quotes persisted with `SharedPreferences`, long-press to delete
- Reading stats: quotes seen, quotes saved, clear-all
- Long-press to copy, share button via `ACTION_SEND`
- Motion: staggered entrance, cross-fade quote switch, press-to-0.95 with overshoot rebound, indicator slide
- Edge-to-edge with transparent status bar
- `dependencies { }` is genuinely empty — pure Android Framework, no AndroidX

## Getting started

```bash
git clone https://github.com/<your-name>/daily-quote.git
```

Open the folder in Android Studio (Hedgehog or newer), let Gradle sync, hit Run.

Requirements: Android 7.0 (API 24) or newer, JDK 17 for building.

The project was originally written on a phone in [CodeAssist](https://github.com/tyron12233/CodeAssist) — you can still edit it there by copying `app/src/main/{java,res,AndroidManifest.xml}` into a new CodeAssist project.

## Customize

| Want to change | Where |
|---|---|
| The quotes | `QUOTES` array in `MainActivity.java` — each entry is `{ text, author }` |
| Background artwork | replace `res/drawable-nodpi/bg_photo.jpg` (1440×1440, pre-blurred) |
| Launcher icon | replace `res/mipmap-*/ic_launcher.png` (48/72/96/144/192 px) |
| Theme colors | `res/values/colors.xml` |

## Structure

```
app/src/main/
├── AndroidManifest.xml
├── java/com/example/dailyquote/MainActivity.java   # all logic
└── res/
    ├── layout/activity_main.xml                    # header + 3 pages + nav bar
    ├── values/{colors,strings,themes}.xml
    ├── drawable/                                   # shapes & vector icons
    ├── drawable-nodpi/bg_photo.jpg
    └── mipmap-*/                                   # launcher icons
```

## Roadmap

- [ ] Move quotes to `assets/quotes.json`
- [ ] Dark mode
- [ ] Home-screen widget that rotates a quote daily
- [ ] Daily reminder
- [ ] Export a quote as a shareable card image

## License

[MIT](LICENSE) © 2026 YG

Artwork and icon are AI-generated images, distributed under the same MIT license; verify asset licensing yourself before commercial use.
