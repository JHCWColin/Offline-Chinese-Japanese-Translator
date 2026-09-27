package com.offline.jatranslator;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.*;
import java.util.List;

public class MainActivity extends Activity {
    private EditText input; private TextView output, status, modelStatus, historyView;
    private TranslationEngine engine; private HistoryStore history; private ModelManager models;
    private int black = Color.rgb(20,20,20);
    @Override public void onCreate(Bundle state) { super.onCreate(state); requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setNavigationBarColor(Color.WHITE); getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        engine = new TranslationEngine(this); history = new HistoryStore(this); models = new ModelManager(this);
        buildUi(); refreshHistory(); refreshModelStatus();
    }
    private TextView label(String text, int size) { TextView v = new TextView(this); v.setText(text); v.setTextColor(black); v.setTextSize(size); v.setGravity(Gravity.CENTER_VERTICAL); v.setPadding(0, 5, 0, 5); return v; }
    private Button button(String text) { Button b = new Button(this); b.setText(text); b.setTextColor(black); b.setTextSize(16); b.setAllCaps(false); b.setMinHeight(48); b.setBackgroundResource(com.offline.jatranslator.R.drawable.button_border); return b; }
    private void buildUi() {
        ScrollView scroll = new ScrollView(this); scroll.setFillViewport(true); scroll.setBackgroundColor(Color.rgb(247,247,242));
        LinearLayout root = new LinearLayout(this); root.setOrientation(LinearLayout.VERTICAL); root.setPadding(18, 14, 18, 18); scroll.addView(root);
        TextView title = label("离线中日翻译器", 25); title.setTypeface(null, 1); root.addView(title, new LinearLayout.LayoutParams(-1, 48));
        status = label("本地运行 · 无需联网", 14); status.setTextColor(Color.DKGRAY); root.addView(status);
        root.addView(label("中文输入", 17));
        input = new EditText(this); input.setTextSize(19); input.setTextColor(black); input.setGravity(Gravity.TOP|Gravity.LEFT); input.setSingleLine(false); input.setMinLines(4); input.setPadding(12,12,12,12); input.setBackgroundResource(R.drawable.panel_border); input.setHint("输入中文句子，例如：你好，很高兴认识你"); input.setHintTextColor(Color.GRAY); root.addView(input, new LinearLayout.LayoutParams(-1, 140));
        LinearLayout actions = new LinearLayout(this); actions.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL); Button clear = button("清空"); Button translate = button("翻译"); actions.addView(clear, new LinearLayout.LayoutParams(110, 54)); LinearLayout.LayoutParams tp = new LinearLayout.LayoutParams(130,54); tp.setMargins(10,0,0,0); actions.addView(translate,tp); root.addView(actions);
        clear.setOnClickListener(v -> { input.setText(""); output.setText("等待翻译"); });
        root.addView(label("日语结果", 17));
        output = label("等待翻译", 21); output.setGravity(Gravity.TOP|Gravity.LEFT); output.setMinHeight(115); output.setPadding(12,12,12,12); output.setBackgroundResource(R.drawable.panel_border); root.addView(output);
        translate.setOnClickListener(v -> doTranslate());
        root.addView(label("模型管理", 17));
        LinearLayout modelRow = new LinearLayout(this); modelStatus = label("轻量内置引擎", 14); modelRow.addView(modelStatus, new LinearLayout.LayoutParams(0,58,1)); Button download = button("下载模型"); modelRow.addView(download, new LinearLayout.LayoutParams(130,58)); root.addView(modelRow);
        download.setOnClickListener(v -> downloadModel());
        root.addView(label("翻译历史", 17));
        historyView = label("暂无记录", 16); historyView.setGravity(Gravity.TOP|Gravity.LEFT); historyView.setPadding(12,10,12,10); historyView.setBackgroundResource(R.drawable.panel_border); root.addView(historyView);
        Button clearHistory = button("清空历史"); LinearLayout.LayoutParams hp = new LinearLayout.LayoutParams(-2,52); hp.gravity = Gravity.RIGHT; root.addView(clearHistory,hp); clearHistory.setOnClickListener(v -> { history.clear(); refreshHistory(); });
        TextView note = label("模型下载后保存在本机，断网也能使用。墨水屏设备建议一次输入短句。", 13); note.setTextColor(Color.DKGRAY); root.addView(note);
        setContentView(scroll);
    }
    private void doTranslate() { String text=input.getText().toString().trim(); if(text.length()==0){ output.setText("请输入中文"); return; } String result=engine.translate(text); output.setText(result); history.add(text,result); refreshHistory(); status.setText("本地运行 · 已完成"); }
    private void refreshHistory() { if(historyView==null)return; List<String> rows=history.all(); if(rows.size()==0){historyView.setText("暂无记录"); return;} StringBuilder s=new StringBuilder(); for(String r:rows){ if(s.length()>0)s.append("\n\n"); s.append(r); } historyView.setText(s.toString()); }
    private void refreshModelStatus(){ if(modelStatus!=null) modelStatus.setText(models.isInstalled()?"标准模型配置已下载 · 本地可用":"轻量内置引擎 · 可下载标准模型配置"); }
    private void downloadModel(){ new AlertDialog.Builder(this).setTitle("下载轻量模型").setMessage("将下载 Helsinki-NLP 中日模型的标准配置文件。完整权重较大，低内存设备默认使用内置轻量推理。是否继续？").setNegativeButton("取消",null).setPositiveButton("下载",(d,w)->{ modelStatus.setText("下载中…"); models.download(new ModelManager.Callback(){ public void done(boolean ok,String msg){ runOnUiThread(()->{ modelStatus.setText(ok?"标准模型配置已下载 · 本地可用":msg); }); }}); }).show(); }
}
