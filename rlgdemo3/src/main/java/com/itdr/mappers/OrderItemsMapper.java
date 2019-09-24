package com.itdr.mappers;

import com.itdr.pojo.OrderItems;

import java.util.List;

public interface OrderItemsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderItems record);

    int insertSelective(OrderItems record);

    OrderItems selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderItems record);

    int updateByPrimaryKey(OrderItems record);

    /* 根据订单号查对应商品详情*/
    List<OrderItems> selectByOrderNo(Long oid);
}