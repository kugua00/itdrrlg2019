package com.itdr.services;

import com.itdr.common.ServerResponse;
import com.itdr.pojo.Products;
import com.itdr.pojo.vo.CartVO;

public interface CartService {
    //购物车添加商品
    ServerResponse<CartVO> addOne(Integer productId, Integer count, Integer id);


    //获取登录用户的购物车列表
    ServerResponse<CartVO> listCart(Integer id);


    //购物车更新商品
    ServerResponse<CartVO> updateCart(Integer productId, Integer count, Integer id);


    //购物车删除商品
    ServerResponse<CartVO> deleteCart(String productIds, Integer id);


    //查询在购物车里的商品信息条数
    ServerResponse<Integer> getCartProductCount(Integer id);

    //改变购物车中商品选中状态
    ServerResponse<CartVO> selectOrUnSelect(Integer id,Integer check,Integer productId);
}
