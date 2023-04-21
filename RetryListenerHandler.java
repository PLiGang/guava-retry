package com.runjing.finance.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.rholder.retry.Attempt;
import com.runjing.common.task.handler.BaseRetryListenerHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 监听器基类
 *
 * @author LiGang
 */
@Slf4j
public class RetryListenerHandler extends BaseRetryListenerHandler {

  @Override
  public <Boolean> void onRetry(Attempt<Boolean> attempt) {
    log.info("RetryListenerHandler 重试结果 time：{},delaySinceFirstAttempt:{},hasException:{}-{},hasResult:{}",
        attempt.getAttemptNumber(),
        attempt.getDelaySinceFirstAttempt(),
        attempt.hasException(),
        attempt.hasException() ? attempt.getExceptionCause().toString() : "NON",
        attempt.hasResult()
    );
  }
}
