package com.itdr.services;

import com.itdr.common.ServerResponse;

import javax.servlet.http.HttpSession;
import java.util.Map;

public interface AliPayService {


    /**
     * 订单支付
     * @param orderno
     * @param uid
     * @return
     */
    ServerResponse alipay(Long orderno, Integer uid);

    /**
     * 查询订单支付状态
     * @param orderno
     * @return
     */
    ServerResponse selectByOrderNo(Long orderno);

    /**
     * 回调成功后做的处理
     * @param params
     * @return
     */
    ServerResponse alipayCallback(Map<String,String> params);
}
