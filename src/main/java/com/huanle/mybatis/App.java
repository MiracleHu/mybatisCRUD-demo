package com.huanle.mybatis;
import java.io.Reader;
import java.text.MessageFormat;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.huanle.mybatis.dao.IUser;
import com.huanle.mybatis.models.User;

public class App {
	private static SqlSessionFactory sqlSessionFactory;
    private static Reader reader;

    static {
        try {
            reader = Resources.getResourceAsReader("config/Configure.xml");
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * @param args
     */
    public static void main(String[] args) {
        
        SqlSession session = sqlSessionFactory.openSession();
        try {
        	//下面注释部分用的是xml这种方式是用 SqlSession 实例来直接执行已映射的SQL语句，这次我们使用interface的方法获取mapper
            //sqlSessionFactory.getConfiguration().addMapper(IUser.class);
            //User user = (User) session.selectOne( "com.yiibai.mybatis.mapper.UserMapper.getUserByID", 1);

            // 用户数据列表
            getUserList();
            // 插入数据
            //testInsert();

            // 更新用户
            //testUpdate();

            // 删除数据
            //testDelete();

        } finally {
            session.close();
        }
    }

    //需要注意的是在增加，更改，删除的时候需要调用 session.commit() 来提交事务，
   //这样才会真正对数据库进行操作提交保存，否则操作没有提交到数据中。
    public static void testInsert(){
        try{
            // 获取Session连接
            SqlSession session = sqlSessionFactory.openSession();
            // 获取Mapper
            IUser userMapper = session.getMapper(IUser.class);
            System.out.println("Test insert start...");
            // 执行插入
            User user = new User();
            user.setId(2);
            user.setName("Google");
            user.setDept("Tech");
            user.setWebsite("http://www.google.com");
            user.setPhone("120");
            userMapper.insertUser(user);
            // 提交事务
            session.commit();

            // 显示插入之后User信息
            System.out.println("After insert");
            getUserList();
            System.out.println("Test insert finished...");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    // 获取用户列表
    public static void getUserList() {
        try {
            SqlSession session = sqlSessionFactory.openSession();
            IUser iuser = session.getMapper(IUser.class);
            // 显示User信息
            System.out.println("Test Get start...");
            printUsers(iuser.getUserList());
            System.out.println("Test Get finished...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void testUpdate(){
        try{
            SqlSession session = sqlSessionFactory.openSession();
            IUser iuser = session.getMapper(IUser.class);
            System.out.println("Test update start...");
            printUsers(iuser.getUserList());
            // 执行更新
            User user = iuser.getUser(1);
            user.setName("New name");
            iuser.updateUser(user);
            // 提交事务
            session.commit();
            // 显示更新之后User信息
            System.out.println("After update");
            printUsers(iuser.getUserList());
            System.out.println("Test update finished...");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    // 删除用户信息
    public static void testDelete(){
        try{
            SqlSession session = sqlSessionFactory.openSession();
            IUser iuser = session.getMapper(IUser.class);
            System.out.println("Test delete start...");
            // 显示删除之前User信息
            System.out.println("Before delete");
            printUsers(iuser.getUserList());
            // 执行删除
            iuser.deleteUser(2);
            // 提交事务
            session.commit();
            // 显示删除之后User信息
            System.out.println("After delete");
            printUsers(iuser.getUserList());
            System.out.println("Test delete finished...");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 
     * 打印用户信息到控制台
     * 
     * @param users
     */
    private static void printUsers(final List<User> users) {
        int count = 0;

        for (User user : users) {
            System.out.println(MessageFormat.format(
                    "============= User[{0}]=================", ++count));
            System.out.println("User Id: " + user.getId());
            System.out.println("User Name: " + user.getName());
            System.out.println("User Dept: " + user.getDept());
            System.out.println("User Website: " + user.getWebsite());
        }
    }
}