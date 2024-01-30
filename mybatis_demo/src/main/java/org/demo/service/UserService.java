package org.demo.service;

import org.demo.entity.User;

/**
 * @ClassName UserService
 * @description: TODO
 * @author: suhaoran
 * @date 2023年07月14日
 * @version: 1.0
 */
public interface UserService {

    /**
     * 插入一个用户
     * @param user
     * @return
     */
    int insert(User user);

}
