# 离线中日翻译器

面向安卓 8.1（API 27）及资源紧缺墨水屏设备的中文到日语离线翻译器。界面使用原生 Android View，采用高对比度、较少动画和较少依赖，适合低刷新率屏幕。

## 功能

- 中文输入与日语结果显示
- 轻量本地推理：常用词与句式无需网络即可翻译
- 翻译历史记录，最多保留 30 条，使用应用私有存储
- 模型管理：可下载 Helsinki-NLP `opus-mt-zh-ja` 的标准配置文件，完整模型权重可按设备存储情况扩展
- APK 使用自签名证书构建，证书密码为 `一二三四五六七八`

## 构建

需要 Android SDK（compileSdk 35）和 Java 11。执行：

```powershell
gradle assembleRelease
```

生成文件位于 `app/build/outputs/apk/release/app-release.apk`。

## 设备要求

最低 Android 8.1（API 27）。应用不依赖云端服务，模型配置下载完成后可断网使用；未下载模型配置时也可直接使用内置轻量引擎。
