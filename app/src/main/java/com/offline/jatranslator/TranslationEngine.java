package com.offline.jatranslator;

import android.content.Context;
import java.util.LinkedHashMap; import java.util.Map;

/** Small deterministic engine designed for devices where a neural runtime would be too costly. */
public class TranslationEngine {
    private final Map<String,String> words = new LinkedHashMap<>();
    public TranslationEngine(Context c) { add("你好","こんにちは"); add("您好","こんにちは"); add("谢谢","ありがとう"); add("谢谢你","ありがとう"); add("再见","さようなら"); add("早上好","おはようございます"); add("晚上好","こんばんは"); add("对不起","すみません"); add("请","お願いします"); add("是","です"); add("不是","ではありません"); add("我","私"); add("你","あなた"); add("我们","私たち"); add("日本","日本"); add("中国","中国"); add("朋友","友達"); add("认识","知り合う"); add("高兴","嬉しい"); add("喜欢","好きです"); add("想要","欲しいです"); add("吃饭","食事をする"); add("喝水","水を飲む"); add("今天","今日"); add("明天","明日"); add("昨天","昨日"); add("现在","今"); add("哪里","どこ"); add("什么","何"); add("为什么","なぜ"); add("多少钱","いくらですか"); add("可以","できます"); add("不可以","できません"); add("帮助","助けてください"); add("厕所","トイレ"); add("车站","駅"); add("商店","店"); add("天气","天気"); add("很好","とても良いです"); }
    private void add(String a,String b){words.put(a,b);}
    public String translate(String source) {
        String s=source.trim(); if(s.length()==0)return ""; String exact=words.get(s); if(exact!=null)return exact;
        String out=s; String[] keys=words.keySet().toArray(new String[0]); java.util.Arrays.sort(keys,(a,b)->Integer.compare(b.length(),a.length()));
        for(String k:keys) out=out.replace(k," "+words.get(k)+" ");
        out=out.replace("，", "、").replace(",", "、").replace("。", "。").replace("？","？").replace("?","？");
        out=out.replaceAll("\\s+", " ").trim();
        if(out.equals(s)) return "（轻量离线引擎暂未收录该词）\n"+s;
        return out;
    }
}
