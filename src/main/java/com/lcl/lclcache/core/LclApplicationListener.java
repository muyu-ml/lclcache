package com.lcl.lclcache.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * plugins entrypoint
 * @Author conglongli
 * @date 2024/6/22 18:23
 */
@Component
public class LclApplicationListener implements ApplicationListener<ApplicationEvent> {

    @Autowired
    List<LclPlugin> plugins;

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if(event instanceof ApplicationReadyEvent readyEvent){
            for(LclPlugin plugin : plugins){
                plugin.init();
                plugin.startup();
            }
        } else if (event instanceof ContextClosedEvent closeEvent) {
            for(LclPlugin plugin : plugins){
                plugin.shutdown();
            }
        }
    }
}
