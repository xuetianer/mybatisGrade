package org.demo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.demo.entity.User;

import java.util.List;


/**
 * @ClassName UserMapper
 * @description: TODO
 * @author: suhaoran
 * @date 2023年07月14日
 * @version: 1.0
 */
@Mapper
public interface UserMapper {

    /**
     * 插入用户
     * @param user
     * @return
     */
    int insert(User user);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    User selectById(@Param("id") Long id,String name);


    List<User> list();

    User testProceDure(@Param("id")Long id );
}
