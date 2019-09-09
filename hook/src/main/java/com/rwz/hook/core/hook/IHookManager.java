package com.rwz.hook.core.hook;

import com.rwz.hook.core.AppConfig;

/**
 * date： 2019/9/4 16:32
 * author： rwz
 * description：
 **/
public interface IHookManager {

    void setAppConfig(AppConfig appConfig);

    void init(ClassLoader loader);

}
