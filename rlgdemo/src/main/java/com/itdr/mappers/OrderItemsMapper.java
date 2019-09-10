package com.itdr.mappers;

import com.itdr.pojo.OrderItems;

public interface OrderItemsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderItems record);

    int insertSelective(OrderItems record);

    OrderItems selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderItems record);

    int updateByPrimaryKey(OrderItems record);
}