package com.example.dailyquote;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

public class MainActivity extends Activity {

    /** 句子库：{正文, 出处} */
    private static final String[][] QUOTES = {
            {"生活不是等待暴风雨过去，而是学会在雨中起舞。", "维维安·格林"},
            {"世界上只有一种真正的英雄主义，那就是认清生活的真相后依然热爱生活。", "罗曼·罗兰"},
            {"我们都在阴沟里，但仍有人仰望星空。", "奥斯卡·王尔德"},
            {"你能在浪费时间中获得乐趣，就不是浪费时间。", "伯特兰·罗素"},
            {"当你真心渴望某样东西时，整个宇宙都会联合起来帮你完成。", "《牧羊少年奇幻之旅》"},
            {"重要的东西，用眼睛是看不见的。", "《小王子》"},
            {"每一个不曾起舞的日子，都是对生命的辜负。", "尼采"},
            {"我走得很慢，但我从不后退。", "林肯"},
            {"不要温和地走进那个良夜。", "狄兰·托马斯"},
            {"人生如逆旅，我亦是行人。", "苏轼"},
            {"种一棵树最好的时间是十年前，其次是现在。", "谚语"},
            {"不要因为走得太远，忘了我们为什么出发。", "纪伯伦"},
            {"把每一个平凡的日子，过成自己喜欢的模样。", "佚名"},
            {"慢慢来，比较快。", "佚名"},
            {"热爱可抵岁月漫长。", "佚名"},
            {"不必着急，最好的总会在最不经意的时候出现。", "佚名"},
            {"今天也要好好生活，因为你值得。", "佚名"},
            {"光会照进来的，你只要把窗打开。", "佚名"},
            {"山高路远，看世界，也找自己。", "佚名"},
            {"与其抱怨黑暗，不如点亮一盏灯。", "佚名"},
            {"保持热爱，奔赴山海。", "佚名"},
            {"向下扎根，向上生长。", "佚名"},
            {"星光不问赶路人，时光不负有心人。", "佚名"},
            {"别慌，月亮也正在大海某处迷茫。", "佚名"},
            {"去做你害怕的事，害怕自然就会消失。", "佚名"},
            {"顺其自然，是竭尽所能之后的不强求。", "佚名"},
            {"愿你成为自己的太阳，无需凭借谁的光。", "佚名"},
            {"愿你所行皆坦途，所遇皆温柔。", "佚名"},
            {"把日子过成诗，把生活熬成糖。", "佚名"},
            {"生活总会给你答案，但不会马上把一切都告诉你。", "佚名"},
            {"不要等着被照亮，成为那束光。", "佚名"},
            {"今天的一小步，是明天的一大步。", "佚名"}
    };

    private static final String PREFS = "daily_quote";
    private static final String KEY_SAVED = "saved";
    private static final String KEY_SEEN = "seen";

    private final Random random = new Random();
    private int lastIndex = -1;
    private int currentTab = 0;

    private LinearLayout header, pageToday, pageSaved, pageMe;
    private FrameLayout card, navWrap, btnBookmark;
    private TextView tvDate, tvQuote, tvAuthor, tvTag, btnRefresh, tvHint, tvSavedCount, tvSavedEmpty;
    private TextView tvSeen, tvSavedStat, btnClear;
    private LinearLayout savedList, navRow;
    private View navIndicator;
    private ImageView icBookmark;
    private final View[] navItems = new View[3];
    private final ImageView[] navIcons = new ImageView[3];
    private final TextView[] navLabels = new TextView[3];
    private final View[] pages = new View[3];

    private SharedPreferences sp;
    private final Handler ui = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupWindow();
        setContentView(R.layout.activity_main);
        sp = getSharedPreferences(PREFS, Context.MODE_PRIVATE);

        bindViews();
        setupNav();
        setupActions();
        applyNextQuote(false);
        tvDate.setText(todayLabel());
        safeAlphaRestore();
        playIntro();
    }

    private void bindViews() {
        header = findViewById(R.id.header);
        card = findViewById(R.id.card);
        navWrap = findViewById(R.id.navWrap);
        navRow = findViewById(R.id.navRow);
        navIndicator = findViewById(R.id.navIndicator);

        pageToday = findViewById(R.id.pageToday);
        pageSaved = findViewById(R.id.pageSaved);
        pageMe = findViewById(R.id.pageMe);
        pages[0] = pageToday;
        pages[1] = pageSaved;
        pages[2] = pageMe;

        tvDate = findViewById(R.id.tvDate);
        tvQuote = findViewById(R.id.tvQuote);
        tvAuthor = findViewById(R.id.tvAuthor);
        tvTag = findViewById(R.id.tvTag);
        btnRefresh = findViewById(R.id.btnRefresh);
        tvHint = findViewById(R.id.tvHint);
        tvSavedCount = findViewById(R.id.tvSavedCount);
        tvSavedEmpty = findViewById(R.id.tvSavedEmpty);
        savedList = findViewById(R.id.savedList);
        tvSeen = findViewById(R.id.tvSeen);
        tvSavedStat = findViewById(R.id.tvSavedStat);
        btnClear = findViewById(R.id.btnClear);
        btnBookmark = findViewById(R.id.btnBookmark);
        icBookmark = findViewById(R.id.icBookmark);

        navItems[0] = findViewById(R.id.navItem0);
        navItems[1] = findViewById(R.id.navItem1);
        navItems[2] = findViewById(R.id.navItem2);
        navIcons[0] = findViewById(R.id.navIcon0);
        navIcons[1] = findViewById(R.id.navIcon1);
        navIcons[2] = findViewById(R.id.navIcon2);
        navLabels[0] = findViewById(R.id.navLabel0);
        navLabels[1] = findViewById(R.id.navLabel1);
        navLabels[2] = findViewById(R.id.navLabel2);

        navRow.post(new Runnable() {
            @Override
            public void run() {
                placeIndicator(currentTab, false);
            }
        });
    }

    private void setupActions() {
        View.OnClickListener refresh = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPress(v);
                refreshQuote();
            }
        };
        card.setOnClickListener(refresh);
        btnRefresh.setOnClickListener(refresh);
        findViewById(R.id.btnRefreshTop).setOnClickListener(refresh);

        card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                copyQuote();
                return true;
            }
        });

        findViewById(R.id.btnShare).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPress(v);
                shareQuote();
            }
        });

        btnBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPress(v);
                toggleBookmark();
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPress(v);
                sp.edit().putStringSet(KEY_SAVED, new HashSet<String>()).apply();
                refreshSavedPage();
                toast(getString(R.string.toast_cleared));
            }
        });

        playPress(card);
        playPress(btnRefresh);
        playPress(navItems[0]);
    }

    private void setupNav() {
        for (int i = 0; i < 3; i++) {
            final int index = i;
            navItems[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectTab(index);
                }
            });
            playPress(navItems[i]);
        }
        updateNavColors(0);
    }

    // ---------------- 页面切换 ----------------

    private void selectTab(int index) {
        if (index == currentTab) {
            bounce(pages[index]);
            return;
        }
        final View out = pages[currentTab];
        final View in = pages[index];
        currentTab = index;

        out.animate().alpha(0f).translationY(-dp(10)).setDuration(140)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        out.setVisibility(View.GONE);
                        out.setAlpha(1f);
                        out.setTranslationY(0f);
                    }
                }).start();

        in.animate().cancel();
        in.setAlpha(0f);
        in.setTranslationY(dp(16));
        in.setVisibility(View.VISIBLE);
        in.animate().alpha(1f).translationY(0f).setDuration(300)
                .setInterpolator(new DecelerateInterpolator())
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        in.setAlpha(1f);
                        in.setTranslationY(0f);
                    }
                }).start();

        placeIndicator(index, true);
        updateNavColors(index);

        if (index == 1) {
            refreshSavedPage();
        } else if (index == 2) {
            refreshMePage();
        }
        ui.postDelayed(new Runnable() {
            @Override
            public void run() {
                in.setAlpha(1f);
            }
        }, 500);
    }

    private void placeIndicator(final int index, boolean animate) {
        if (navRow.getWidth() == 0) {
            navRow.post(new Runnable() {
                @Override
                public void run() {
                    placeIndicator(index, false);
                }
            });
            return;
        }
        int itemWidth = navRow.getWidth() / 3;
        int width = Math.max(dp(40), itemWidth - dp(22));
        ViewGroup.LayoutParams lp = navIndicator.getLayoutParams();
        if (lp.width != width) {
            lp.width = width;
            lp.height = dp(42);
            navIndicator.setLayoutParams(lp);
        }
        float target = index * itemWidth + (itemWidth - width) / 2f;
        if (animate) {
            navIndicator.animate().translationX(target).setDuration(280)
                    .setInterpolator(new OvershootInterpolator(1.1f)).start();
        } else {
            navIndicator.animate().cancel();
            navIndicator.setTranslationX(target);
        }
    }

    private void updateNavColors(int index) {
        int active = getColorSafe(R.color.blue_alt);
        int idle = getColorSafe(R.color.text_hint);
        for (int i = 0; i < 3; i++) {
            boolean on = (i == index);
            navLabels[i].setTextColor(on ? active : idle);
            navLabels[i].setTypeface(null, on ? Typeface.BOLD : Typeface.NORMAL);
            navIcons[i].setColorFilter(on ? active : idle, PorterDuff.Mode.SRC_IN);
        }
        navIndicator.setBackgroundResource(R.drawable.bg_nav_indicator);
    }

    // ---------------- 句子 ----------------

    private void refreshQuote() {
        tvQuote.animate().cancel();
        tvAuthor.animate().cancel();
        tvQuote.animate().alpha(0f).translationY(-dp(14)).setDuration(150).start();
        tvAuthor.animate().alpha(0f).translationY(-dp(14)).setDuration(150)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        applyNextQuote(true);
                    }
                }).start();
    }

    private void applyNextQuote(boolean animate) {
        int index = random.nextInt(QUOTES.length);
        if (QUOTES.length > 1) {
            int guard = 0;
            while (index == lastIndex && guard++ < 20) {
                index = random.nextInt(QUOTES.length);
            }
        }
        lastIndex = index;
        tvQuote.setText(QUOTES[index][0]);
        tvAuthor.setText("—— " + QUOTES[index][1]);
        updateBookmarkIcon();

        int seen = sp.getInt(KEY_SEEN, 0) + 1;
        sp.edit().putInt(KEY_SEEN, seen).apply();

        if (animate) {
            tvQuote.setTranslationY(dp(20));
            tvQuote.setAlpha(0f);
            tvAuthor.setTranslationY(dp(20));
            tvAuthor.setAlpha(0f);
            tvQuote.animate().alpha(1f).translationY(0f).setDuration(320)
                    .setInterpolator(new DecelerateInterpolator()).start();
            tvAuthor.animate().alpha(1f).translationY(0f).setDuration(320)
                    .setStartDelay(60).setInterpolator(new DecelerateInterpolator()).start();
            ui.postDelayed(new Runnable() {
                @Override
                public void run() {
                    tvQuote.setAlpha(1f);
                    tvAuthor.setAlpha(1f);
                }
            }, 700);
        }
    }

    private String currentKey() {
        return tvQuote.getText().toString() + "|" + tvAuthor.getText().toString();
    }

    private void toggleBookmark() {
        String key = currentKey();
        Set<String> saved = new HashSet<String>(sp.getStringSet(KEY_SAVED, new HashSet<String>()));
        if (saved.contains(key)) {
            saved.remove(key);
            toast(getString(R.string.toast_unsaved));
        } else {
            saved.add(key);
            toast(getString(R.string.toast_saved));
        }
        sp.edit().putStringSet(KEY_SAVED, saved).apply();
        updateBookmarkIcon();
        if (currentTab == 1) {
            refreshSavedPage();
        }
    }

    private void updateBookmarkIcon() {
        boolean on = sp.getStringSet(KEY_SAVED, new HashSet<String>()).contains(currentKey());
        icBookmark.setImageResource(on ? R.drawable.ic_bookmark_filled : R.drawable.ic_bookmark);
        icBookmark.setColorFilter(on ? getColorSafe(R.color.pink_main) : getColorSafe(R.color.blue_alt),
                PorterDuff.Mode.SRC_IN);
        tvTag.setText(on ? R.string.tag_saved : R.string.tag_today);
    }

    private void copyQuote() {
        String text = tvQuote.getText() + "\n" + tvAuthor.getText();
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if (cm != null) {
            cm.setPrimaryClip(ClipData.newPlainText("每日一句", text));
            toast(getString(R.string.toast_copied));
        }
    }

    private void shareQuote() {
        String text = tvQuote.getText() + "\n" + tvAuthor.getText() + "\n—— 来自「每日一句」";
        Intent it = new Intent(Intent.ACTION_SEND);
        it.setType("text/plain");
        it.putExtra(Intent.EXTRA_TEXT, text);
        try {
            startActivity(Intent.createChooser(it, "分享这一句"));
        } catch (Exception e) {
            copyQuote();
        }
    }

    // ---------------- 收藏页 ----------------

    private void refreshSavedPage() {
        savedList.removeAllViews();
        Set<String> saved = sp.getStringSet(KEY_SAVED, new HashSet<String>());
        tvSavedCount.setText(saved.size() + " 条");
        if (saved.isEmpty()) {
            tvSavedEmpty.setVisibility(View.VISIBLE);
            savedList.setVisibility(View.GONE);
            return;
        }
        tvSavedEmpty.setVisibility(View.GONE);
        savedList.setVisibility(View.VISIBLE);

        int i = 0;
        for (final String key : saved) {
            String[] parts = key.split("\\|");
            String text = parts.length > 0 ? parts[0] : key;
            String author = parts.length > 1 ? parts[1] : "";

            LinearLayout box = new LinearLayout(this);
            box.setOrientation(LinearLayout.VERTICAL);
            box.setBackgroundResource(R.drawable.bg_item);
            int pad = dp(18);
            box.setPadding(pad, pad, pad, pad);
            LinearLayout.LayoutParams blp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            blp.bottomMargin = dp(12);
            box.setLayoutParams(blp);
            box.setElevation(dp(4));

            TextView t = new TextView(this);
            t.setText(text);
            t.setTextSize(16);
            t.setTextColor(getColorSafe(R.color.text_main));
            t.setLineSpacing(dp(4), 1f);
            box.addView(t);

            TextView a = new TextView(this);
            a.setText(author);
            a.setTextSize(12);
            a.setTextColor(getColorSafe(R.color.text_hint));
            LinearLayout.LayoutParams alp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            alp.topMargin = dp(6);
            a.setLayoutParams(alp);
            box.addView(a);

            box.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Set<String> set = new HashSet<String>(sp.getStringSet(KEY_SAVED, new HashSet<String>()));
                    set.remove(key);
                    sp.edit().putStringSet(KEY_SAVED, set).apply();
                    refreshSavedPage();
                    updateBookmarkIcon();
                    return true;
                }
            });

            box.setAlpha(0f);
            box.setTranslationY(dp(12));
            box.animate().alpha(1f).translationY(0f).setDuration(240)
                    .setStartDelay(40L * i).start();
            i++;
            savedList.addView(box);
        }
        ui.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int k = 0; k < savedList.getChildCount(); k++) {
                    savedList.getChildAt(k).setAlpha(1f);
                }
            }
        }, 600);
    }

    private void refreshMePage() {
        tvSeen.setText(String.valueOf(sp.getInt(KEY_SEEN, 0)));
        tvSavedStat.setText(String.valueOf(sp.getStringSet(KEY_SAVED, new HashSet<String>()).size()));
    }

    // ---------------- 动效 ----------------

    private void playIntro() {
        header.setTranslationY(-dp(16));
        header.setAlpha(0f);
        header.animate().alpha(1f).translationY(0f).setDuration(420)
                .setInterpolator(new DecelerateInterpolator()).start();

        card.setScaleX(0.96f);
        card.setScaleY(0.96f);
        card.setAlpha(0f);
        card.animate().alpha(1f).scaleX(1f).scaleY(1f).setStartDelay(90)
                .setDuration(460).setInterpolator(new OvershootInterpolator(0.9f)).start();

        navWrap.setTranslationY(dp(28));
        navWrap.setAlpha(0f);
        navWrap.animate().alpha(1f).translationY(0f).setStartDelay(150)
                .setDuration(420).setInterpolator(new DecelerateInterpolator()).start();

        btnRefresh.setAlpha(0f);
        btnRefresh.setTranslationY(dp(14));
        btnRefresh.animate().alpha(1f).translationY(0f).setStartDelay(230)
                .setDuration(320).start();
    }

    /** 兜底：任何动画异常都不会让界面留白 */
    private void safeAlphaRestore() {
        ui.postDelayed(new Runnable() {
            @Override
            public void run() {
                header.setAlpha(1f);
                card.setAlpha(1f);
                btnRefresh.setAlpha(1f);
                tvHint.setAlpha(1f);
                navWrap.setAlpha(1f);
                tvQuote.setAlpha(1f);
                tvAuthor.setAlpha(1f);
                for (int i = 0; i < 3; i++) {
                    if (pages[i].getVisibility() == View.VISIBLE) {
                        pages[i].setAlpha(1f);
                    }
                }
            }
        }, 900);
    }

    private void playPress(final View v) {
        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        view.animate().scaleX(0.95f).scaleY(0.95f)
                                .setDuration(90).setInterpolator(new DecelerateInterpolator()).start();
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        view.animate().scaleX(1f).scaleY(1f)
                                .setDuration(260).setInterpolator(new OvershootInterpolator(2.4f)).start();
                        break;
                }
                return false;
            }
        });
    }

    private void bounce(View v) {
        v.animate().cancel();
        v.setScaleX(0.97f);
        v.setScaleY(0.97f);
        v.animate().scaleX(1f).scaleY(1f).setDuration(300)
                .setInterpolator(new OvershootInterpolator(2.6f)).start();
    }

    // ---------------- 基础 ----------------

    private void setupWindow() {
        Window w = getWindow();
        w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            w.setStatusBarColor(Color.TRANSPARENT);
            w.setNavigationBarColor(Color.TRANSPARENT);
            w.getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    private String todayLabel() {
        Calendar c = Calendar.getInstance();
        String date = new SimpleDateFormat("M月d日", Locale.CHINA).format(c.getTime());
        String[] week = {"日", "一", "二", "三", "四", "五", "六"};
        return date + " · 星期" + week[c.get(Calendar.DAY_OF_WEEK) - 1];
    }

    private int getColorSafe(int res) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return getColor(res);
        }
        return getResources().getColor(res);
    }

    private void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private int dp(float value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateBookmarkIcon();
    }
}
