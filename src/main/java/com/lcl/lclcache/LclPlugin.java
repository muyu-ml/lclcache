package com.lcl.lclcache;

/**
 * lcl cache plugin
 * @Author conglongli
 * @date 2024/6/22 18:21
 */
public interface LclPlugin {
    void init();
    void startup();

    void shutdown();
}
