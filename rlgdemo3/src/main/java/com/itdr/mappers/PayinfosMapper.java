package com.itdr.mappers;

import com.itdr.pojo.Payinfos;

public interface PayinfosMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Payinfos record);

    int insertSelective(Payinfos record);

    Payinfos selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Payinfos record);

    int updateByPrimaryKey(Payinfos record);

    int insertPayInfo(Payinfos payInfo);
}