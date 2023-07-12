# hookframe
基于XPosed框架，简化应用间通信，支持多app hook

### 步骤：
1. 集成XPosed开发环境
2. 在入口类MainHook构造方法中注册，eg: HookHelp.register()...
3. 在入口类MainHook#handleLoadPackage()中，调用HookHelp.handleLoadPackage(lpparam);
4. 在Activity通过ClientManager启动服务， 发送消息也通过ClientManager
5. 实现自定义的HookManager(对应每个目标应用)继承BaseHookManager
6. 需要配置当前应用的包名：Constance.PACKAGE_NAME，以在目标应用启动服务

### 脱壳：
+ 参考：[https://juejin.im/post/5c3fe899f265da6144204be4]
+ 参考：[https://github.com/WrBug/dumpDex]

### EdXposed安装
一、框架安装
因Xposed仅支持Android 7及以下版本，以上作者并没有维护，替换方案则是采用EdXposed。如果手机版本7及以下还是安装Xposed环境即可（简单很多）。
该步骤基于Mi 11 LE手机Android 11版本，其他机型都有可能存在兼容问题，特别注意android版本跟软件版本匹配问题。不要统一安装对应版本，可能变砖。
1. 安装Magisk
2. 安装Riru模块
  1. 下周Riru v25.4.4版本。注意android 11最好下载该版本，低版本则不能该版本（可能导致开不了机）。而且需要跟EdXposed版本统一，否则可能不匹配。注意不要安装最新版本（v26+），会导致EdXposed安装失败。
Due to the low quality of Riru's documentation and the high frequency of API changes, the development team has decided not to adapt Riru 26 for the time being. Please wait for the development team member solohsu for the subsequent maintenance plan. We deeply regret this irresponsible change of Riru.
Please do NOT update to Riru 26, we recommend using a version below 25. The last available version is 25.4.4: https://t.me/EdXposed/136
  2. 将上一步下载的zip包拷贝到手机本地，通过Magisk安装Riru。安装成功之后会在模块列表中展示出来。
  
[图片]
3. 安装EdXposed模块
  1. 下载0.5.2.2 zip包
  2. 同上在Magisk中安装EdXposed模块
  3. 安装成功之后可以重启系统
4. 安装EdXposed Manager
  下载并安装EdXposed Manager v4.6.2 apk文件，同其他apk文件安装一致。
  打开EdXposed Manager app，如果页面如下提示，恭喜，已安装完成EdXposed整个运行环境。
[图片]
  
5. 安装需要的模块
一般安装支持Xposed模块，会默认在EdXposed Manager模块中提示，打开开关，然后重启就会生效。
