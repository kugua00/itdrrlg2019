package com.itdr.controllers.backend;



import com.itdr.common.ServerResponse;
import com.itdr.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@ResponseBody
@RequestMapping("/manage/category")
public class CategoryManageController {


    @Autowired
    CategoryService categoryService;



    //根据分类Id查询所有子类(包括本身)
    @RequestMapping("get_deep_category.do")
    private ServerResponse getDeepCategory( Integer categoryId){
        ServerResponse sr = categoryService.getdeepcategory(categoryId);
        return null;
    }



}
