package com.userm.contorller.user;

import java.util.Arrays;
import java.util.List;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
//@RestController注解能够使项目支持Rest
@RestController
public class userConroller {
    @Autowired
    private JdbcTemplate jdbc;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String helloWeb() {
        return "hello world8";
    }

    @RequestMapping("/getArray")
    public List<String> getArray(){
        return Arrays.asList(new String[] {"Aa","Bb","Cc"});
    }

     // 二级路径：/path/childPath http://localhost:8080/springboot/getUserByGet?userName=zzy
    //@RestController注解能够使项目支持Rest
    @RestController
    @SpringBootApplication
    //表示该controller类下所有的方法都公用的一级上下文根
    @RequestMapping(value = "/user")
    public class UserController {
        // 这里使用@RequestMapping注解表示该方法对应的二级上下文路径
        @RequestMapping(value = "/getUserByGet", method = RequestMethod.GET)
        String getUserByGet(@RequestParam(value = "username") String username){
            return "Hello " + username;
        }
        // 跨表查询user_tb和userinfo_tb
        @RequestMapping(value = "/detail", method = RequestMethod.GET)
        Object getUserDetail(@RequestParam(value = "username") String username){
            String sql = "select * from user_tb left join userinfo_tb on user_tb.userid=userinfo_tb.userid";
            return jdbc.queryForList(sql);
        }
    }

    // POST
    //通过RequestMethod.POST表示请求需要时POST方式
    @RequestMapping(value = "/getUserByPost", method = RequestMethod.POST)
    String getUserByPost(@RequestParam(value = "userName") String userName){
        return "Hello " + userName;
    }

    // 请求参数为JSON格式的请求方法的写法
    // 注意如果请求参数是像上面那样通过url form形式提交的请求参数，那么必须使用@RequestParam注解来标示参数，如果使用的请求报文是POST形势的JSON串，那么这里在入参的注解一定要使用@RequestBody，否则会报json解析错误。
    @RequestMapping(value = "/getUserByJson",method = RequestMethod.POST)
    String getUserByJson(@RequestBody String data){
        return "Json is " + data;
    }
    // 无法使用原因未知
    @RequestMapping(value = "/getUser", method = RequestMethod.GET)
    public String getUser() {
        Connection connection = null;
        Statement statement = null;
        try {
            String url = "jdbc:postgresql://192.168.0.28:5432/userms";//换成自己PostgreSQL数据库实例所在的ip地址，并设置自己的端口
            String user = "postgres";
            String password = "admin";
            Class.forName("org.postgresql.Driver");
            connection= DriverManager.getConnection(url, user, password);
            System.out.println("是否成功连接pg数据库"+connection);
            String sql = "select * from user_tb";
            statement = connection.createStatement();
            /**
             * 关于ResultSet的理解：Java程序中数据库查询结果的展现形式，或者说得到了一个结果集的表
             * 在文档的开始部分有详细的讲解该接口中应该注意的问题，请阅读JDK
             * */
            ResultSet resultSet = statement.executeQuery(sql);
            while(resultSet.next()){
                //取出列值
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                System.out.println(id+","+name+",");

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally{
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }finally{
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }

        }
        return "";
    }


    // @Autowired
    // private JdbcTemplate jdbc;

    @RequestMapping(value = "/getDate")
    public Object getSqlUser(){
        String sql = "select * from user_tb";
        return jdbc.queryForList(sql);
    }
}
