package com.runjing.common.task.annotation;


import com.runjing.common.task.enums.retryStrategyEnum;
import com.runjing.common.task.handler.BaseRetryListenerHandler;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 基于Guava-retry的重试注解
 *
 * @author LiGang
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface GuavaRetry {

    /**
     * 重试次数N次之后停止
     * 与 maxDelay 互斥 只能选择其中一个
     *
     * @return
     */
    int maxAttemptNumber() default 3;

    /**
     * 重试超过N秒后结束
     * 与 maxAttemptNumber 互斥 只能选择其中一个
     *
     * @return
     */
    long maxDelay() default 0;

    /**
     * 等待策略
     *
     * @return
     */
    retryStrategyEnum.WaitStrategyEnum waitStrategy() default retryStrategyEnum.WaitStrategyEnum.FIXED_WAIT;

    /**
     * 要求Callable返回非空
     *
     * @return
     */
    boolean requireNonNull() default false;

    /**
     * 重试的异常类数组
     *
     * @return
     */
    Class<? extends Exception>[] exceptionType() default {};

    /**
     * If has RuntimeException to attempt
     *
     * @return
     */
    boolean retryIfRuntimeException() default false;


    /**
     * 表示单次任务执行时间限制(秒） 默认无限制
     *
     * @return
     */

    long attemptTimeLimiter() default 0L;

    /**
     * 监听器列表
     *
     * @return
     */
    Class<? extends BaseRetryListenerHandler>[] retryListeners() default {};
}

