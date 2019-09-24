package com.itdr.services.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.itdr.common.Const;
import com.itdr.common.ServerResponse;
import com.itdr.mappers.*;
import com.itdr.pojo.OrderItems;
import com.itdr.pojo.Orders;
import com.itdr.pojo.Payinfos;
import com.itdr.pojo.pay.Configs;
import com.itdr.pojo.pay.ZxingUtils;
import com.itdr.services.AliPayService;
import com.itdr.utils.DateUtils;
import com.itdr.utils.JsonUtils;
import com.itdr.utils.PoToVoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AliPayServiceImpl implements AliPayService {
    @Autowired
    private OrdersMapper orderMapper;
    @Autowired
    private OrderItemsMapper orderItemMapper;
    @Autowired
    private PayinfosMapper payInFoMapper;




    //用户支付
    @Override
    public ServerResponse alipay(Long orderno, Integer uid) {
        //参数非空判断
        if (orderno == null || orderno <= 0){
            return ServerResponse.defeateRS("参数不能为空");
        }
        //判断订单是否存在
        Orders order = orderMapper.selectByOrderNo(orderno);
        if (order == null){
            return ServerResponse.defeateRS("该订单不存在");
        }
        //判断订单状态是否是未付款
        if (order.getStatus() != 10){
            return ServerResponse.defeateRS("该订单不是未付款状态");
        }
        //判断订单和用户是否匹配
        int i = orderMapper.selectByOrderNoAndUid(orderno,uid);
        if (i<=0){
            return ServerResponse.defeateRS("订单和用户不匹配");
        }


        //根据订单号查询对应商品详情
        List<OrderItems> orderItems = orderItemMapper.selectByOrderNo(order.getOrderNo());

        //调用支付宝接口获取支付二维码


        try {
            //使用封装方法获得预下单成功后返回的二维码信息串
            AlipayTradePrecreateResponse response = test_trade_precreate(order, orderItems);
            //成功执行下一步
            if (response.isSuccess()){
                //将二维码信息串生成图片，并保存，（需要修改为运行机器上的路径）
                String filePath = String.format(Configs.getSavecode_test()+"qr-%s.png",
                        response.getOutTradeNo());
                ZxingUtils.getQRCodeImge(response.getQrCode(), 256, filePath);

                //预下单成功获取返回信息（二维码字符串）
                Map map = new HashMap();
                map.put("orderNo", order.getOrderNo());

                map.put("qrCode", filePath);
                return ServerResponse.successRS(map);
            }else {
                //预下单失败
                return ServerResponse.defeateRS("下单失败");
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            //出现异常，预下单失败
            return ServerResponse.defeateRS("下单失败");
        }

        //返回生成二维码后的图片地址和订单号信息给前端

        //后期图片会放到图片服务器上

    }


    @Override
    public ServerResponse selectByOrderNo(Long orderno) {
        return null;
    }



    //支付宝回调
    @Override
    public ServerResponse alipayCallback(Map<String, String> Map) {
        //获取orderNo,就是订单号
        Long orderNo = Long.parseLong(Map.get("out_trade_no"));
        //获取流水号
        String trade_no = Map.get("trade_no");
        //获取支付状态
        String tradeStatus = Map.get("trade_status");
        //获取支付时间
        String paymentTime = Map.get("gmt_payment");
        //获取订单金额
        BigDecimal totalAmount = new BigDecimal(Map.get("total_amount"));
        //获取订单中的用户ID
        //   int uid = Integer.parseInt(newMap.get("uid"));

        //验证订单号是否存在
        Orders order = orderMapper.selectByOrderNo(orderNo);
        if (order == null){
            //不是要付款的订单
            return ServerResponse.defeateRS("不是要付款的订单");
        }
        //验证订单金额和数据库中订单金额是否相同
        if (!totalAmount.equals(order.getPayment())){
            return ServerResponse.defeateRS("订单不匹配");
        }


     /*   //订单和用户是否匹配
        int i = orderMapper.selectByOrderNoAndUid(orderNo, uid);
        if (i<=0){
            return ServerResponse.defeateRS("订单和用户不匹配");
        }*/

        //防止支付宝重复回调
        if (order.getStatus() != 10){
            return ServerResponse.defeateRS("订单不是未付款状态");
        }
        //交易状态判断
        if (tradeStatus.equals(Const.TRADE_SUCCESS)){
            //校验状态码，支付成功
            //更改数据库中订单的状态+更改支付时间+更新时间+删除用过的本地二维码
            order.setStatus(20);
            order.setPaymentTime(DateUtils.strToDate(paymentTime));
            orderMapper.updateByOrder(order);

            //支付成功，删除本地存在的二维码图片
            String str = String.format(Configs.getSavecode_test()+"qr-%s.png",
                    order.getOrderNo());
            File file = new File(str);
            boolean b = file.delete();

            /*
             * 判断图片删除是否成功？？？？、
             * */
        }
        //保存支付宝支付信息（任何状态都保存）
        Payinfos payInfo = new Payinfos();
        payInfo.setOrderNo(orderNo);
        payInfo.setPayPlatform(Const.PaymentPlatformEnum.ALIPAY.getCode());
        payInfo.setPlatformStatus(tradeStatus);
        payInfo.setPlatformNumber(trade_no);
        payInfo.setUserId(order.getUserId());
        //将支付信息插入到数据表中
        int result = payInFoMapper.insertPayInfo(payInfo);
        if (result <= 0){
            return ServerResponse.defeateRS("支付信息保存失败");
        }
        //支付信息保存成功返回结果SUCCESS，
        return ServerResponse.successRS("SUCCESS");
    }



    //测试当面付2.0生成支付二维码
    private AlipayTradePrecreateResponse test_trade_precreate(Orders order, List<OrderItems> orderItems) throws AlipayApiException {
        //读取配置文件信息
        Configs.init("zfbinfo.properties");

        //实例化支付宝客户端
        AlipayClient alipayClient = new DefaultAlipayClient(Configs.getOpenApiDomain(),
                Configs.getAppid(), Configs.getPrivateKey(), "json", "utf-8",
                Configs.getAlipayPublicKey(), Configs.getSignType());

        //创建API对应的request类
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();

        //获取一个BizContent对象,并转换成json格式
        String str = JsonUtils.obj2String(PoToVoUtil.getBizContent(order, orderItems));
        request.setBizContent(str);
        //设置支付宝回调路径
        request.setNotifyUrl(Configs.getNotifyUrl_test());
        //获取响应,这里要处理一下异常
        AlipayTradePrecreateResponse response = alipayClient.execute(request);

        //返回响应的结果
        return response;
    }

}
