package com.gec._03_wiki.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gec._03_wiki.exception.BusinessException;
import com.gec._03_wiki.exception.BusinessExceptionCode;
import com.gec._03_wiki.mapper.UserMapper;
import com.gec._03_wiki.pojo.User;
import com.gec._03_wiki.pojo.req.UserLoginReq;
import com.gec._03_wiki.pojo.req.UserQueryReq;
import com.gec._03_wiki.pojo.req.UserResetPasswordReq;
import com.gec._03_wiki.pojo.req.UserSaveReq;
import com.gec._03_wiki.pojo.resp.CommonResp;
import com.gec._03_wiki.pojo.resp.PageResp;
import com.gec._03_wiki.pojo.resp.UserLoginResp;
import com.gec._03_wiki.pojo.resp.UserQueryResp;
import com.gec._03_wiki.service.UserService;
import com.gec._03_wiki.utils.CopyUtil;
import com.gec._03_wiki.utils.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
* @author 751200
* @description 针对表【user】的数据库操作Service实现
* @createDate 2023-09-08 09:50:00
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private SnowFlake snowFlake;

    @Autowired
    private RedisTemplate redisTemplate;

    public PageResp<UserQueryResp> getUserListByPage(UserQueryReq req) {
        Page<User> page = new Page<>(req.getPage(), req.getSize());

        QueryWrapper<User> wrapper = new QueryWrapper<>();
        if (!ObjectUtils.isEmpty(req.getLoginName())){
            wrapper.eq("login_name",req.getLoginName());
        }

        page = this.page(page,wrapper);
        List<User> list = page.getRecords();

        LOG.info("总行数：{}",page.getTotal()+"");
        LOG.info("总页数：{}",page.getPages()+"");

        List<UserQueryResp> UserResps = CopyUtil.copyList(list, UserQueryResp.class);

        PageResp<UserQueryResp> respPage = new PageResp<>();
        respPage.setTotal(page.getTotal());
        respPage.setList(UserResps);
        return respPage;
    }

    public void saveUser(UserSaveReq req) {
        User user = CopyUtil.copy(req, User.class);

        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        //这里是修改
        if (user.getId()!=null&&!ObjectUtils.isEmpty(user.getId())){
            //虽然前端已经禁用修改登录名，但是还是要为防止黑客恶意修改，
            // 所以进入这里面将登录名置空，数据库该字段就不回去修改
            user.setLoginName(null);
            user.setPassword(null);
            this.updateById(user);
            return;
        }
        //下面是新增  处理用户名唯一
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("login_name",req.getLoginName());
        //根据用户名获取对象
        User userDB = this.getOne(wrapper);
        if (userDB==null){
            //没有重复就新增
            this.save(user);
        } else {
            //重复就报出自定义异常
            throw new BusinessException(BusinessExceptionCode.USER_LOGIN_NAME_EXIST);
        }
    }

    public void resetPassword(UserResetPasswordReq req) {
        User user = CopyUtil.copy(req, User.class);
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        this.updateById(user);
    }
    public UserLoginResp userLogin(UserLoginReq req) {
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        //根据用户名查出用户
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("login_name",req.getLoginName());
        //根据用户名获取对象
        User userDB = this.getOne(wrapper);
        if (ObjectUtils.isEmpty(userDB)){
            //用户名不存在
            LOG.info("用户名不存在{}",req.getLoginName());
            throw new BusinessException(BusinessExceptionCode.LOGIN_USER_ERROR);
        } else {
            //传递进来的也是经过两次加密的
            if (userDB.getPassword().equals(req.getPassword())){
                //登录成功
                UserLoginResp userLoginResp = CopyUtil.copy(userDB, UserLoginResp.class);


                Long token = snowFlake.nextId();
                LOG.info("生成单点登录token:{}，并存入redis",token);
                userLoginResp.setToken(token+"");
                redisTemplate.opsForValue().set(token.toString(), JSONObject.toJSONString(userLoginResp),3600*24, TimeUnit.SECONDS);
//                return userLoginResp;
                CommonResp<UserLoginResp> resp = new CommonResp<>();
                resp.setContent(userLoginResp);
                return userLoginResp;
            } else {
                //密码不对
                LOG.info("密码不对，输入密码为{}",req.getPassword(),userDB.getPassword());
                throw new BusinessException(BusinessExceptionCode.LOGIN_USER_ERROR);
            }
        }
    }
    public void logout(String token) {
        redisTemplate.delete(token);
        LOG.info("从redis中删除token:{}",token);
    }
}




