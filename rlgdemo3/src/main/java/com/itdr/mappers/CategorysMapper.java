package com.itdr.mappers;

import com.alipay.api.domain.Category;
import com.itdr.pojo.Categorys;

import java.util.List;

public interface CategorysMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Categorys record);

    int insertSelective(Categorys record);

    Categorys selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Categorys record);

    int updateByPrimaryKey(Categorys record);

    //根据父id查询所有一级子ID
    List<Categorys> selectByParentId(Integer pid);

}