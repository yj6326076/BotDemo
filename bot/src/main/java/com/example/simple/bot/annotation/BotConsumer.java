package com.example.simple.bot.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 机器人消费类注解
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
