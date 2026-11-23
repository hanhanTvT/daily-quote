# 设计规范

记录这个 App 的视觉与动效决策，改版时以此为准，避免越改越乱。

## 1. 视觉基调

三个词：**轻盈、通透、柔和**。

参考对象是 Telegram 的干净现代感，但把冷灰换成了蓝白粉。
所有元素都在「白 → 浅蓝 → 浅粉」这条轴线上，只有强调动作（换句按钮、选中态）才允许出现饱和色。

## 2. 色彩

| 令牌 | 色值 | 用途 |
|---|---|---|
| `blue_main` | `#5B8CFF` | 主蓝：按钮渐变起点、书签选中 |
| `blue_alt` | `#4A90E2` | 深蓝：选中文字、图标着色 |
| `blue_soft` | `#EAF1FF` | 浅蓝底：高亮块、统计块 |
| `pink_main` | `#FF6B9D` | 粉：按钮渐变终点、装饰点 |
| `pink_soft` | `#FFE7EF` | 浅粉底：标签胶囊 |
| `text_main` | `#1E2233` | 正文 |
| `text_sub` | `#5C6478` | 出处、副标题 |
| `text_hint` | `#98A0B3` | 未选中页签、提示 |
| `glass_white` | `#E0FFFFFF` | 卡片填充（88% 白） |
| `glass_stroke` | `#99FFFFFF` | 卡片 1dp 描边 |
| `nav_glass` | `#F0FFFFFF` | 导航栏（94% 白） |

对比度：正文 `#1E2233` 落在 `#E0FFFFFF` 叠图上是 12.8:1，远超 AA。

## 3. 形状

- 主卡片圆角 **28dp**，卡片内边距 24dp
- 导航栏圆角 **30dp**（高度 72dp，视觉上是完整胶囊）
- 导航高亮块圆角 **24dp**，宽度 = 单格宽 − 22dp
- 按钮 **100dp**（全圆角），高 52dp
- 标签胶囊 **100dp**，高 26dp

## 4. 层次

| 层 | 做法 |
|---|---|
| 底 | 插画 `bg_photo.jpg`（1440²，Gaussian Blur 1.6，提亮 6%） |
| 遮罩 | `bg_scrim`：白色 270° 渐变（顶部 40% → 底部 78% 不透明） |
| 卡片 | `bg_card_glass`：白 240→217 竖渐变 + 1dp `#AAFFFFFF` 描边 |
| 文字 | 深色，直接压在玻璃上 |

阴影只在卡片（`elevation 10dp`）和主按钮（`8dp`）上出现，其余一律不要，否则会脏。

## 5. 动效

| 场景 | 实现 | 时长 |
|---|---|---|
| 页面入场 | 顶部条 `translationY -10dp→0`、卡片 `scale .96→1`、导航栏 `translationY 24dp→0`，逐层 delay 100/185/270ms | 420–540ms |
| 换句-出 | `alpha 1→0` + `translationY 0→-12dp` | 140ms |
| 换句-入 | `alpha 0→1` + `translationY 16dp→0` | 300ms |
| 按下 | `scale 1→0.95`，Decelerate | 90ms |
| 抬起 | `scale →1`，Overshoot(2.4) | 260ms |
| 页签切换-出 | `alpha 1→0` + `translationY 0→10dp` | 140ms |
| 页签切换-入 | `alpha 0→1` + `translationY 14dp→0` | 300ms |
| 指示器滑动 | `translationX`，Overshoot(1.1) | 280ms |
| 卡片呼吸 | `scale 1→0.985→1`（换句后触发一次） | 400ms |

原则：

1. 位移不超过 16dp，超过就显得「重」
2. 进场慢、退场快（140ms 出 / 300ms 入）
3. 只有「被点击的那个东西」可以回弹，其他元素保持平静
4. 任何 alpha 动画都必须有兜底：900ms 后强制恢复为 1，防止动画中断后内容永久隐形

## 6. 排版

- 正文 22sp / 行高 1.6 / `text_main` / Medium
- 出处 13sp / 右对齐 / `text_sub`
- 页签文字 11sp，选中加粗并转 `blue_alt`
- 标题 17sp Bold，副标题 11.5sp `text_sub`
- 数字统计 26sp Bold，单位说明 11sp `text_hint`
