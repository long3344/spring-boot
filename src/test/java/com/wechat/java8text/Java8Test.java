package com.wechat.java8text;

import com.wechat.SpringBoot;
import com.wechat.StringRedisTemplateTest;
import com.wechat.model.Admin;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 描述：
 * 作者: TWL
 * 创建日期: 2017/9/1
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringBoot.class)
@ActiveProfiles("dev")
public class Java8Test {

    private static final Logger logger = LoggerFactory.getLogger(StringRedisTemplateTest.class);

    public static  void main(String[] arge0 ){

        /*Arrays.asList( "a", "b", "d" ).forEach(e -> System.out.println("java8循环："+ e ) );

        Arrays.asList( "a", "b", "d" ).forEach( e -> {
            System.out.print( e );
            System.out.print( e );
        } );*/

        /*String separator = ",";
        Arrays.asList( "a", "b", "d" ).forEach(
                ( String e ) -> System.out.print( e + separator ) );*/

//Lambda可能会返回一个值
        /*Arrays.asList( "a", "b", "d" ).sort( ( e1, e2 ) -> {
            int result = e1.compareTo( e2 );
            return result;
        } );*/

        /*Optional< String > fullName = Optional.ofNullable( null);
        System.out.println( "Full Name is set? " + fullName.isPresent() );//判空，空返回false，不空返回true
        System.out.println( "Full Name: " + fullName.orElseGet( () -> "[none]" ) );//若为空 选择默认值[none]
        System.out.println( fullName.map( s -> "Hey " + s + "!" ).orElse( "Hey Stranger!" ) );//fullName为空 则执行后面的*/


        /*List<Admin> aa=new ArrayList<>();
        aa.add(new Admin(1,"张三","123456"));
        aa.add(new Admin(1,"李四","123456"));
        aa.add(new Admin(2,"王五","123456"));
        aa.add(new Admin(3,"赵六","123456"));
        final Map< Integer ,List< Admin >> map = aa
                .stream()
                .collect( Collectors.gropuingBy(Admin::getId) );//根据id分组
        System.out.println( map );*/
    }

}
