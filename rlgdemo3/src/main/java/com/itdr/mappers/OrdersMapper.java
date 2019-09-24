package com.itdr.mappers;

import com.itdr.pojo.Orders;
import org.apache.ibatis.annotations.Param;

public interface OrdersMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Orders record);

    int insertSelective(Orders record);

    Orders selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Orders record);

    int updateByPrimaryKey(Orders record);

    /* 按订单编号查找订单*/
    Orders selectByOrderNo(Long orderno);

    /* 根据订单编号和用户ID判断数据 */
    int selectByOrderNoAndUid(@Param("orderno") Long orderno, @Param("uid")Integer uid);

    void updateByOrder(Orders order);
}