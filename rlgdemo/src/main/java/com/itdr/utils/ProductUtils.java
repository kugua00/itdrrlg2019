package com.itdr.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class ProductUtils {

    private static SqlSessionFactory factory ;


    static {
    //加载核心配置文件
     String resource = "sqlMapConfig2.xml";
     InputStream in = null;
     try {
         in = Resources.getResourceAsStream(resource);
     } catch (IOException e) {
         e.printStackTrace();
     }

    //  SqlSessionFactoryBuilder（）可以从xml配置文件或通过java的方式构建出SqlSessionFactory工厂的实例
     //创建SqlSessionFactory工厂   mybatis 中的类创建工厂      （建议使用单例模式或静态单例模式）   （工厂模式 单例模式）
        //一个SqlSessionFactory工厂对应一个环境（environment） 多个数据库就配置多个对应的环境
         factory = new SqlSessionFactoryBuilder().build(in);
    }


    public static SqlSession getS(){

        SqlSession sqlSession = factory.openSession();


        return sqlSession;
    }
}
