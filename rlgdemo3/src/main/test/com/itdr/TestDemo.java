package com.itdr;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring.xml")
public class TestDemo {
    @Test
    public void test1(){
        ApplicationContext a = new ClassPathXmlApplicationContext("spring.xml");
        DriverManagerDataSource dataSource = (DriverManagerDataSource) a.getBean("dataSource");
        String url = dataSource.getUrl();
        System.out.println(url);


    }
}

