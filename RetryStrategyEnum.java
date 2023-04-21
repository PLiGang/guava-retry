package com.runjing.common.task.enums;

import com.github.rholder.retry.WaitStrategies;
import com.github.rholder.retry.WaitStrategy;
import lombok.Getter;

import java.util.concurrent.TimeUnit;

public class etryStrategyEnum {


    /**
     * 等待策略
     */
    @Getter
    public enum WaitStrategyEnum {
        /**
         * 每3秒重试一次
         */
        FIXED_WAIT(WaitStrategies.fixedWait(3, TimeUnit.SECONDS)),
        /**
         * 3-10秒随机间隔
         */
        RANDOM_WAIT(WaitStrategies.randomWait(3, TimeUnit.SECONDS, 10, TimeUnit.SECONDS)),
        /**
         * 递增配置，初始3s，后面每次在前面的基础上加3s，等待时长: 3、6、9、12、15、18、21
         */
        INCREMENTING_WAIT(WaitStrategies.incrementingWait(3, TimeUnit.SECONDS, 3, TimeUnit.SECONDS)),
        /**
         * 当出现Runtime异常时，等待3s
         */
        EXCEPTION_WAIT(WaitStrategies.exceptionWait(RuntimeException.class, e -> 3000L)),
        /**
         * 固定时长策略 + 异常等待策略，对于空指针异常异常，等待3s，其它情况等待2s
         */
        JOIN_EXCEPTION_FIXED_WAIT(WaitStrategies.join(WaitStrategies.exceptionWait(RuntimeException.class, e -> 3000L), WaitStrategies.fixedWait(2, TimeUnit.SECONDS))),
        /**
         * 指定乘数multiplier 和 最大值，第一次失败后，依次等待时长，1*1000、1*1000、2*1000、3*1000、5*1000...直到最多5分钟，5分钟后每隔5分钟重试一次
         */
        FIBONACCI_WAIT(WaitStrategies.fibonacciWait(1000, 5, TimeUnit.MINUTES));

        private final WaitStrategy waitStrategy;

        public WaitStrategy getWaitStrategy() {
            return this.waitStrategy;
        }

        WaitStrategyEnum(WaitStrategy waitStrategy) {
            this.waitStrategy = waitStrategy;
        }
    }

}
