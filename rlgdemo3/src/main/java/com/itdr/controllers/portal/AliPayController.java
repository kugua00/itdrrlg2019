package com.itdr.controllers.portal;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.itdr.common.Const;
import com.itdr.common.ServerResponse;
import com.itdr.pojo.Users;
import com.itdr.pojo.pay.Configs;
import com.itdr.services.AliPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.HttpSessionRequiredException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.HttpServletBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@Controller
@ResponseBody
@RequestMapping("/pay/")
public class AliPayController {

    //日志有关



    //注入支付业务层
    @Autowired
    AliPayService aliPayService;


    /**
     * 订单支付
     * @param orderno
     * @param session
     * @return
     */
    @RequestMapping("alipay.do")
    private ServerResponse alipay(Long orderno , HttpSession session){
        //用户是否登录
        Users users = (Users) session.getAttribute(Const.LOGINUSER);
        if (users == null ){
            return ServerResponse.defeateRS(Const.UsersEnum.NO_LOGIN.getCode(),Const.UsersEnum.NO_LOGIN.getDesc());

        }
        return aliPayService.alipay(orderno,users.getId());
    }

    /**
     * 查询支付状态
     * @param orderno
     * @param session
     * @return
     */
    @RequestMapping("query_order_pay_status.do")
    private ServerResponse  query_order_pay_status(Long orderno ,HttpSession session){

        return aliPayService.selectByOrderNo(orderno);
    }


    /**
     * 支付宝回调
     * @param request
     * @return
     */
    @RequestMapping("alipay_callback.do")
    private String  alipay_callback(HttpServletRequest request){
    //    Users users = (Users) session.getAttribute(Const.LOGINUSER);

        //获取支付宝返回的参数，返回一个map集合
        Map<String, String[]> parameterMap = request.getParameterMap();

        //获取上面集合的键的set集合
        Set<String> strings = parameterMap.keySet();

        //获取该集合的迭代器
        Iterator<String> iterator = strings.iterator();

        //创建一个新的map集合用于存储支付宝的数据，去除不必要的数据
        Map<String, String> newMap = new HashMap<>();

        //遍历原始集合，键的集合中数据
        while (iterator.hasNext()) {
            //根据键获取parameterMap中的值
            String key = iterator.next();
            String[] strings1 = parameterMap.get(key);
            //遍历值的数组，重新拼装数据
            StringBuffer ss = new StringBuffer("");
            for (int i = 0; i < strings1.length; i++) {
                //如果只有一个元素，就保存一个元素
                //有多个元素时，每个元素之间用逗号隔开
                ss = (i == strings1.length - 1) ? ss.append(strings1[i])  : ss.append(strings1[i] + ",")  ;
            }
            //把新的数据以键值对的方式放入一个新的集合
            newMap.put(key, ss.toString());
        }

        //setp1:支付宝验签，是不是支付宝发送的请求，避免重复请求

        //去除参数中的这个参数（官方提示不需要的参数）
        newMap.remove("sign_type");
        try {

            //调用支付宝封装的方法进行验签操作，需要返回数据+公钥+编码集+类型定义
            boolean b = AlipaySignature.rsaCheckV2(newMap, Configs.getAlipayPublicKey(), "utf-8", Configs.getSignType());

            //验签通过执行下一步
            if (!b) {
                //验签失败，返回错误信息
                return "{'msg':'验签失败’}" ;
/*
                        ServerResponse.defeateRS(Const.PaymentPlatformEnum.VERIFY_SIGNATURE_FALSE.getCode(), Const.PaymentPlatformEnum.VERIFY_SIGNATURE_FALSE.getDesc());
*/
                //数据转换成json数据格式
                //数据返回给浏览器

            }
        } catch (AlipayApiException e){
            e.printStackTrace();
            return "{'msg':'验证失败'}";
        }

        //验签通过，去业务层执行业务
        ServerResponse sr = aliPayService.alipayCallback(newMap);

        //业务层处理完，返回对应的状态信息，这个信息是直接返回给支付宝服务器，所以必须严格要求准确
        if (sr.isSuccess()){
            return  "SUCCESS" ;
        }else {
            return "FAILED" ;
        }
    }
}
