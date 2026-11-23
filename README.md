<div align="center">

<img src="docs/icon.png" width="120" alt="每日一句 icon" />

# 每日一句 · DailyQuote

**一张磨砂玻璃卡片，装下今天的一句好话。**

蓝白粉配色 · 磨砂玻璃质感 · 大圆角 · 手写级流畅动效

![Java](https://img.shields.io/badge/Java-8+-orange?style=flat-square)
![minSdk](https://img.shields.io/badge/minSdk-24-brightgreen?style=flat-square)
![Target](https://img.shields.io/badge/target-34-blue?style=flat-square)
![Dependencies](https://img.shields.io/badge/dependencies-0-success?style=flat-square)
![License](https://img.shields.io/badge/license-MIT-blue?style=flat-square)
![Build](https://github.com/hanhanTvT/daily-quote/actions/workflows/build.yml/badge.svg)

[特性](#特性) · [快速开始](#快速开始) · [界面结构](#界面结构) · [设计规范](#设计规范) · [自定义](#自定义指南) · [Roadmap](#roadmap)

</div>

---

## 这是什么

一个只有 600 多行 Java、**零第三方依赖**的 Android 小 App。

打开就是一张半透明的磨砂卡片，里面躺着一句话。点卡片、点「换一句」，句子淡出、上移、再淡入，像有人轻轻把纸抽走又放回。喜欢的一句可以收进收藏，下次打开还在。

不用登录、不联网、不要权限，装了就能用。

## 特性

- **磨砂玻璃卡片** — 半透明白 + 1dp 高光描边 + 28dp 大圆角，背景插画透过卡片若隐若现
- **三层页签** — 今日 / 收藏 / 我的，底部 QQ 风格胶囊导航栏，选中项有蓝色高亮块弹性滑动
- **32 句内置文案** — 随机抽取且不会连续重复，中文原文 + 出处
- **收藏持久化** — `SharedPreferences` 本地存储，收藏页长按即可删除
- **阅读统计** — 「我的」页实时显示已读句数、收藏句数，可一键清空
- **长按复制 / 一键分享** — 复制到剪贴板，或通过 `ACTION_SEND` 分享给任意 App
- **动效细节** — 入场错峰上浮、换句淡出淡入、点击缩放到 0.95 再弹性回弹、导航指示器 Overshoot 滑动
- **沉浸式状态栏** — 透明状态栏 + 深色图标，内容自然延伸到屏幕顶端
- **零依赖** — 没有 AndroidX、没有 Retrofit、没有 Glide，`dependencies { }` 是空的

## 快速开始

### 方式一：Android Studio（推荐）

```bash
git clone https://github.com/<your-name>/daily-quote.git
```

Android Studio（Hedgehog 及以上）→ `Open` → 选择项目根目录 → 等待 Gradle Sync → 点 ▶ Run。

> 首次同步 Gradle 会自动下载 AGP 8.1.4 与 Gradle 8.4，需要联网。

### 方式二：在手机上改（CodeAssist）

本项目最初就是在手机上用 [CodeAssist](https://github.com/tyron12233/CodeAssist) 写完的：

1. 新建一个 Android App 项目（Java、minSdk 24）
2. 把 `app/src/main/` 下的 `java/`、`res/`、`AndroidManifest.xml` 全部覆盖进去
3. 运行

### 方式三：直接装 APK

到 [Releases](../../releases) 页下载最新 `app-debug.apk` 安装即可。支持 Android 7.0（API 24）及以上。

## 界面结构

```
┌───────────────────────────────────────┐
│  ◉  每日一句                    ⤴  ⟳  │   ← 顶部显示条：头像 / 标题 / 分享 / 刷新
│     今天也要好好生活                   │
│                                       │
│  ┌─────────────────────────────────┐  │
│  │ 今日                       🔖   │  │   ← 磨砂玻璃卡片（28dp 圆角）
│  │ 种一棵树最好的时间是十年前，    │  │
│  │ 其次是现在。                    │  │
│  │ —— 谚语                ▬▬▬     │  │
│  └─────────────────────────────────┘  │
│                                       │
│            [  换一句  ]               │
│      点卡片也能换一句 · 长按复制       │
│                                       │
│  ╭─────────────────────────────────╮  │
│  │   ⬤今日      ☆收藏      ◯我的   │  │   ← 胶囊导航栏 + 滑动高亮块
│  ╰─────────────────────────────────╯  │
└───────────────────────────────────────┘
```

三页签说明：

| 页签 | 内容 |
|---|---|
| 今日 | 大卡片展示当前句子，右上角书签收藏，下方换句按钮 |
| 收藏 | 收藏列表（卡片形式，逐条淡入），长按删除，空状态有引导文案 |
| 我的 | 已读 / 收藏计数、清空收藏、版本信息 |

## 设计规范

### 配色

| 用途 | 变量 | 色值 |
|---|---|---|
| 主蓝 | `blue_main` | `#5B8CFF` |
| 深蓝（选中/强调） | `blue_alt` | `#4A90E2` |
| 浅蓝底 | `blue_soft` | `#EAF1FF` |
| 粉色点缀 | `pink_main` | `#FF6B9D` |
| 浅粉底 | `pink_soft` | `#FFE7EF` |
| 主文字 | `text_main` | `#1E2233` |
| 次文字 | `text_sub` | `#5C6478` |
| 提示文字 | `text_hint` | `#98A0B3` |
| 卡片玻璃 | `glass_white` | `#E0FFFFFF` |
| 卡片描边 | `glass_stroke` | `#99FFFFFF` |
| 导航栏 | `nav_glass` | `#F0FFFFFF` |

### 圆角与层次

- 卡片：`28dp`，`elevation 10dp`
- 导航栏：`30dp` 胶囊，内部高亮块 `24dp`
- 按钮：`100dp` 全圆角胶囊
- 玻璃感来源：半透明白色渐变 + `1dp #AAFFFFFF` 描边 + 全屏柔光遮罩 + 背景图轻微模糊

### 动效

| 场景 | 曲线 | 时长 |
|---|---|---|
| 入场（顶部条 / 卡片 / 导航栏错峰） | Decelerate + Overshoot(0.8) | 420–540ms，delay 100 起每层 +85ms |
| 换句（旧句淡出上移） | Decelerate | 140ms，位移 12dp |
| 换句（新句淡入回落） | Decelerate | 300ms，起点位移 16dp |
| 点击缩放 | 按下 0.95（90ms）→ 回弹 Overshoot(2.4) | 260ms |
| 页签切换 | 旧页淡出上移 140ms → 新页淡入下移 300ms | — |
| 导航高亮块滑动 | Overshoot(1.1) | 280ms |

## 项目结构

```
app/src/main/
├── AndroidManifest.xml
├── java/com/example/dailyquote/
│   └── MainActivity.java          # 全部逻辑：618 行，单 Activity
└── res/
    ├── layout/activity_main.xml   # 单布局：顶部条 + 三页 + 导航栏
    ├── values/
    │   ├── colors.xml             # 全部配色令牌
    │   ├── strings.xml            # 全部文案
    │   └── themes.xml             # NoActionBar + 透明系统栏
    ├── drawable/                  # 14 个 shape + 7 个 vector 图标
    ├── drawable-nodpi/
    │   └── bg_photo.jpg           # 背景插画（已做磨砂处理）
    └── mipmap-*/                  # 5 个密度的启动图标
```

## 自定义指南

### 改句子

打开 `MainActivity.java`，找到 `QUOTES` 数组。每条是 `{ 正文, 出处 }`：

```java
private static final String[][] QUOTES = {
    { "种一棵树最好的时间是十年前，其次是现在。", "谚语" },
    { "不要温和地走进那个良夜。", "狄兰·托马斯" },
    // ... 想加多少条都行
};
```

### 换背景图

替换 `res/drawable-nodpi/bg_photo.jpg`。建议 1440×1440、JPEG、先在别处做 Gaussian Blur（半径 1.5–2）+ 提亮 6%，这样叠在白色遮罩下最不抢字。

### 换图标

替换 `res/mipmap-{mdpi,hdpi,xhdpi,xxhdpi,xxxhdpi}/ic_launcher.png`，尺寸分别是 `48 / 72 / 96 / 144 / 192` px。图标必须是方形、无圆角的原图，系统会自动裁圆。

### 换主色

只改 `res/values/colors.xml` 里的 `blue_main` / `pink_main` 即可全局换肤；卡片透明度改 `glass_white` 的 alpha（当前 `E0` ≈ 88%）。

### 改包名

1. `app/build.gradle` 里的 `namespace` 与 `applicationId`
2. `MainActivity.java` 的 `package` 行
3. 目录 `java/com/example/dailyquote/` 改成对应路径

## 已知限制

- 句子硬编码在 Java 数组里，改文案需要重新编译（准备改成 `assets/quotes.json`）
- 无深色模式（正在做）
- 无桌面小组件、无每日定时通知
- 单 Activity + 手写页签切换，没有用 Fragment/ViewPager —— 有意为之，减少依赖和层级

## Roadmap

- [ ] 句子数据迁出到 `assets/quotes.json`（可热更新文案）
- [ ] 深色模式（跟随系统 + 手动切换）
- [ ] 桌面小组件：每天自动换一句
- [ ] 每日定时提醒（WorkManager）
- [ ] 收藏导出为图片（带磨砂卡片样式分享）

## 贡献

欢迎 PR 和 Issue。提 PR 前请先看 [CONTRIBUTING.md](CONTRIBUTING.md)。一句文案、一个配色建议、一处动效微调都算贡献。

如果这个 App 让你今天心情好了一点，点个 ⭐ 就行。

## 许可

[MIT](LICENSE) © 2026 YG

背景插画与图标使用 AI 生成图像，随项目以 MIT 许可分发；若你打算商用，请自行确认素材授权。
