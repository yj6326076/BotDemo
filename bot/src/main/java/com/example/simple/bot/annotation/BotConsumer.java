package com.example.simple.bot.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 机器人消费类注解
 * 通过regex配置的正则匹配消息内容选择执行注解的服务类
 * 逻辑与配置里的bot.config里配置的功能保持一致
 * application.yml里的执行顺序先于注解配置
 * @author yj632
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface BotConsumer {
    String regex();
    boolean isWhite() default false;
    boolean isAdmin() default false;
    boolean isInnerWhite() default false;
}
