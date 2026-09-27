package com.offline.jatranslator;
import android.content.Context; import android.content.SharedPreferences; import java.util.*;
public class HistoryStore {
    private final SharedPreferences p; private static final String KEY="rows";
    HistoryStore(Context c){p=c.getSharedPreferences("history",Context.MODE_PRIVATE);}
    public void add(String cn,String jp){ArrayList<String> a=new ArrayList<>(all()); a.add(0,cn+"\n→ "+jp); while(a.size()>30)a.remove(a.size()-1); p.edit().putString(KEY,join(a)).apply();}
    public List<String> all(){String s=p.getString(KEY,""); if(s.length()==0)return new ArrayList<>(); return new ArrayList<>(Arrays.asList(s.split("\\u001e",-1)));}
    public void clear(){p.edit().remove(KEY).apply();}
    private String join(List<String>a){StringBuilder b=new StringBuilder();for(String s:a){if(b.length()>0)b.append('\u001e');b.append(s.replace("\u001e"," "));}return b.toString();}
}
