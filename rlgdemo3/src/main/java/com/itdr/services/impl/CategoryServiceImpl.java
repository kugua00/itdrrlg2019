package com.itdr.services.impl;



import com.itdr.common.ServerResponse;
import com.itdr.mappers.CategorysMapper;
import com.itdr.pojo.Categorys;
import com.itdr.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategorysMapper categorysMapper;


    //根据分类Id查询所有子类(包括本身)
    @Override
    public ServerResponse getdeepcategory(Integer categoryId) {

        if (categoryId == null || categoryId < 0 ){
            return ServerResponse.defeateRS("非法参数");
        }
        List<Integer> li = new ArrayList<>();
        li.add(categoryId);
        getAll(categoryId,li);

        ServerResponse sr = ServerResponse.successRS(li);
        return sr;
    }


    private void getAll(Integer pid , List<Integer> list){
        List<Categorys> li = categorysMapper.selectByParentId(pid);

        if (li != null && li.size() != 0) {
            for (Categorys categorys: li
            ) {
                list.add(categorys.getId());
                getAll(categorys.getId(),list);
            }

        }
    }
}
