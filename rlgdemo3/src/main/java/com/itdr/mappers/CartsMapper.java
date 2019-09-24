package com.itdr.mappers;

import com.itdr.common.Const;
import com.itdr.pojo.Carts;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface CartsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Carts record);

    int insertSelective(Carts record);

    Carts selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Carts record);

    int updateByPrimaryKey(Carts record);

    //根据用户id和商品id判断商品是否存在
    Carts selectByUidAndProductID(@Param("uid") Integer uid, @Param("productId")Integer productId);

    //根据用户ID查询多有购物数据
    List<Carts> selectByUid(Integer uid);

    //根据用户ID判断用户购物车是否全选
    int selectByUidCheck(@Param("uid")Integer uid , @Param("check")Integer check);

    //删除购物车内商品
    int deleteByProducts(@Param("productIds")List<String> productIds,@Param("id")Integer id);

    //改变购物车中商品选中状态
    int selectOrUnSelect(@Param("id")Integer id,@Param("check")Integer check,@Param("productId")Integer productId);
}