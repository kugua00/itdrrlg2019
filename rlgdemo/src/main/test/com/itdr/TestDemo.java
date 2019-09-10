package com.itdr;

import com.itdr.utils.ProductUtils;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;



public class TestDemo {
    @Test
    public void test1(){
        ApplicationContext a = new ClassPathXmlApplicationContext("spring.xml");
        DriverManagerDataSource dataSource = (DriverManagerDataSource) a.getBean("dataSource");
        String url = dataSource.getUrl();
        System.out.println(url);


    }
}

