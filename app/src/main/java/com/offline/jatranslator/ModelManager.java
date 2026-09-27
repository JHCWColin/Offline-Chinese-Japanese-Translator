package com.offline.jatranslator;
import android.content.Context; import java.io.*; import java.net.*;
public class ModelManager {
    private static final String URL="https://huggingface.co/Helsinki-NLP/opus-mt-zh-ja/resolve/main/config.json";
    private final File file;
    public interface Callback { void done(boolean ok,String msg); }
    ModelManager(Context c){file=new File(c.getFilesDir(),"opus-mt-zh-ja.config.json");}
    public boolean isInstalled(){return file.exists()&&file.length()>20;}
    public void download(Callback cb){new Thread(()->{try{HttpURLConnection h=(HttpURLConnection)new URL(URL).openConnection();h.setConnectTimeout(12000);h.setReadTimeout(20000);h.setRequestProperty("User-Agent","OfflineJapaneseTranslator/1.0"); if(h.getResponseCode()!=200){cb.done(false,"下载失败：HTTP "+h.getResponseCode());return;} File tmp=new File(file.getPath()+".tmp"); InputStream in=h.getInputStream(); FileOutputStream out=new FileOutputStream(tmp); byte[] buf=new byte[8192]; int n; while((n=in.read(buf))!=-1)out.write(buf,0,n); in.close();out.close(); if(tmp.length()<20){tmp.delete();cb.done(false,"模型文件无效");return;} if(file.exists())file.delete();tmp.renameTo(file);cb.done(true,"ok");}catch(Exception e){cb.done(false,"下载失败，请检查网络");}}).start();}
}
