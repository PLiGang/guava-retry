package com.runjing.finance.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.runjing.common.core.exception.ServiceException;
import com.runjing.common.core.response.BasePageResponse;
import com.runjing.common.core.response.BaseResponse;
import com.runjing.common.core.utils.DataUtils;
import com.runjing.common.core.utils.DateUtils;
import com.runjing.common.core.utils.StringUtils;
import com.runjing.common.task.annotation.GuavaRetry;
import com.runjing.finance.mapper.PromotionSubsidyMapper;
import com.runjing.finance.model.ExcelTempDTO;
import com.runjing.finance.model.FranchiseeShopvo;
import com.runjing.finance.model.PromotionSubsidyReq;
import com.runjing.finance.model.PromotionSubsidyVO;
import com.runjing.finance.model.SubsidyDetaiReq;
import com.runjing.finance.model.SubsidyDetailVO;
import com.runjing.finance.model.SubsidyEmailReq;
import com.runjing.finance.model.SubsidyResp;
import com.runjing.finance.model.SubsisyEamilDTO;
import com.runjing.finance.model.entity.SubsidyResendEntity;
import com.runjing.finance.service.PromotionSubsidyService;
import com.runjing.finance.service.RetryListenerHandler;
import com.runjing.goods.client.controller.SkuInfoForeignClient;
import com.runjing.goods.client.model.request.sku.BaseSkuInfoRequest;
import com.runjing.goods.client.model.request.skuInfoForeign.SkuInfoBatchRequest;
import com.runjing.goods.client.model.response.sku.BaseSkuResponse;
import com.runjing.goods.client.model.response.skuInfoForeign.SkuInfoResponse;
import com.runjing.store.controller.FranchiseeControllerClient;
import com.runjing.store.controller.ShopControllerClient;
import com.runjing.store.model.request.ShopRequest;
import com.runjing.store.model.request.franchisee.FranchiseeRequest;
import com.runjing.store.model.request.franchisee.FranchiseeShopSnRequest;
import com.runjing.store.model.vo.BaseShopVo;
import com.runjing.store.model.vo.FranchiseeVo;
import com.runjing.store.model.vo.franchisee.FranchiseeShopSnVo;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;



@Service
@Slf4j
public class Demo  {



  @GuavaRetry(maxAttemptNumber = 5,exceptionType ={NullPointerException.class,NumberFormatException.class},retryListeners = RetryListenerHandler.class)
  public void demo(String code) {
    log.info("测试入参：{}", code);
    Integer.parseInt("");
    throw new NullPointerException ("ddddd");
  }





}
