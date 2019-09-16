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
