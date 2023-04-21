package com.runjing.common.task.aspect;

import com.alibaba.fastjson.JSON;
import com.github.rholder.retry.AttemptTimeLimiters;
import com.github.rholder.retry.BlockStrategies;
import com.github.rholder.retry.RetryException;
import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import com.runjing.common.task.annotation.GuavaRetry;
import com.runjing.common.task.handler.BaseRetryListenerHandler;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @description @Retry 注解过滤器
 */
@Aspect
@Component
@Slf4j
public class RetryHandlerAspect {

    @Pointcut("@annotation(com.runjing.common.task.annotation.GuavaRetry)")
    public void retryPointCut() {
    }

    @Around("retryPointCut()&&@annotation(retry)")
    public Object around(ProceedingJoinPoint pjp, GuavaRetry retry) throws Throwable {
        log.info("其注解参数:{}", retry);
        Signature signature = pjp.getSignature();

        //获取到所有的参数值的数组
        Object[] params = pjp.getArgs();
        MethodSignature methodSignature = (MethodSignature) signature;
        //获取到方法的所有参数名称的字符串数组
        String[] parameterNames = methodSignature.getParameterNames();
        log.info("切面around拦截到方法{}.{},参数name列表{}对应的value列表:{}", signature.getDeclaringTypeName(), signature.getName(), parameterNames, params);

        //请求入口
        Callable<Object> callable = () -> {
            try {
                return pjp.proceed();
            } catch (Throwable throwable) {
                log.error("{} 执行异常:", signature.getName(), throwable);
                if (throwable instanceof Exception) {
                    throw (Exception) throwable;
                } else {
                    throw new Exception(throwable);
                }
            }
        };
        //定义重试策略
        RetryerBuilder<Object> retryBuilder = RetryerBuilder.newBuilder()
                .withWaitStrategy(retry.waitStrategy().getWaitStrategy());

        //停止策略 重试N次
        if (retry.maxAttemptNumber() > 0) {
            retryBuilder.withStopStrategy(StopStrategies.stopAfterAttempt(retry.maxAttemptNumber()));
        }

        //停止策略 N秒后停止
        if (retry.maxDelay() > 0) {
            retryBuilder.withStopStrategy(StopStrategies.stopAfterDelay(retry.maxDelay(), TimeUnit.SECONDS));
        }

        //返回值不能为null
        if (retry.requireNonNull()) {
            retryBuilder.retryIfResult(Objects::isNull);
        }

        //RuntimeException()时进行重试
        if (retry.retryIfRuntimeException()) {
            retryBuilder.retryIfRuntimeException();
        }
        //单次任务执行时间限制
        if (retry.attemptTimeLimiter() > 0) {
            retryBuilder.withAttemptTimeLimiter(AttemptTimeLimiters.fixedTimeLimit(retry.attemptTimeLimiter(), TimeUnit.SECONDS));
        }
        //添加重试异常类型
        for (Class clazz : retry.exceptionType()) {
            retryBuilder.retryIfExceptionOfType(clazz);
        }

        //添加监听器列表
        for (Class<? extends BaseRetryListenerHandler> listener : retry.retryListeners()) {
            try {
                retryBuilder.withRetryListener(listener.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                log.warn("添加监听器异常：", e);
            }
        }
        retryBuilder.withBlockStrategy(BlockStrategies.threadSleepStrategy());
        Retryer retryPolicy = retryBuilder.build();
        try {
            Object result = retryPolicy.call(callable);
            log.info("{} 结果 result = {}", signature.getName(), JSON.toJSON(result));
            return result;
        } catch (ExecutionException e) {
            log.warn("Callable执行异常:", e);
            throw e;
        } catch (RetryException e) {
            log.warn("重试过程中发生异常结束重试:", e);
            throw e;
        }
    }

}
