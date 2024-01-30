package org.demo.entity;

import lombok.Data;

import java.io.*;
import java.util.Date;

/**
 * @ClassName User
 * @description: TODO
 * @author: suhaoran
 * @date 2023年07月14日
 * @version: 1.0
 */
@Data
public class User implements Serializable {

    private static final long serialVersionUID = -3423363034791718842L;

    private Long id;

    private transient String name;

    private Integer age;

    private Date birthday;

    public static void main(String[] args) {
        User user = new User();
        user.setAge(10);
        user.setId(10L);
        user.setName("测试序列化");
        user.setBirthday(new Date());
        Dept dept = user.getDept();
        dept.setName("测试级联");
        System.out.println(user);
    }

    private Dept dept=new Dept();


    public void setName(String name){
        this.name=name;
    }

    public void setName(Integer a){
        this.name = "sss";
    }

    public User(){

    }
    public User(String name,Integer age,Date birthday){
        this.name=name;
        this.age=age;
        this.birthday=birthday;
    }

}
