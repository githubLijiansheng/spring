package com.ljs.user.service.impl;

import com.ljs.user.entity.User;
import com.ljs.user.mapper.UserMapper;
import com.ljs.user.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jiansheng.li
 * @since 2020-12-30
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
