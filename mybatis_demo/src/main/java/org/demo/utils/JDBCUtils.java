package org.demo.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

/**
 * @author : Cyan_RA9
 * @version : 21.0
 */
public class JDBCUtils {
    private static String driver;       //Driver驱动
    private static String url;          //Uniform Resource Locator，包含数据库信息
    private static String user;         //用户名
    private static String password;     //用户密码

    static {
        Properties properties = new Properties();
        try {
            //Class.forName(driver);

            properties.load(new FileInputStream("src/main/resources/mybatis/mysql.properties"));
            driver = properties.getProperty("driver");
            url = properties.getProperty("url");
            user = properties.getProperty("user");
            password = properties.getProperty("password");
        } catch (IOException e) {
            /*
                实际开发中，往往将捕获到的编译期异常转换为一个运行期异常抛出去;
                这时对于调用者来说，
                既可以选择捕获该异常，进行进一步的业务处理，也可以选择默认处理该异常，比较方便。
             */
            throw new RuntimeException(e);
        }
    }

    //获取连接
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //释放资源
    /**
     * @param resultSet : 结果集（DQL产生）
     * @param statement : Statement接口或PreparedStatement接口的实现类
     * @param connection : 即通过getConnection方法获取到的连接
     */
    public static void close(ResultSet resultSet, Statement statement, Connection connection) {
        /*
            DML的执行不产生ResultSet结果集，可以传入一个null;
            因此要先判断传入的对象是否为空，若非空则调用close方法关闭资源（动态绑定）
         */
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}