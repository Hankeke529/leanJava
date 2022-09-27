package com.atguigu.controller;


import com.alibaba.dubbo.config.annotation.Reference;

import com.atguigu.entity.UserInfo;
import com.atguigu.result.Result;
import com.atguigu.result.ResultCodeEnum;
import com.atguigu.service.UserInfoService;
import com.atguigu.util.MD5;
import com.atguigu.vo.LoginVo;
import com.atguigu.vo.RegisterVo;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/userInfo")
public class UserInfoController{


    @Reference
    private UserInfoService userInfoService;


    @RequestMapping("/sendCode/{phone}")
    public Result sendCode(@PathVariable("phone") String phone ,HttpServletRequest request){
        String  code = "8888";

        request.getSession().setAttribute("code",code);


        return Result.ok(code);
    }

    @RequestMapping("/register")
    public Result register(@RequestBody RegisterVo registerVo, HttpSession session){

        String phone = registerVo.getPhone();
        String code = registerVo.getCode();
        String password = registerVo.getPassword();
        String nickName = registerVo.getNickName();
        //验空

        if (StringUtils.isEmpty(phone)||StringUtils.isEmpty(code)||StringUtils.isEmpty(password)
                ||StringUtils.isEmpty(nickName)){
            return Result.build(null, ResultCodeEnum.PARAM_ERROR);
        }

        String sessionCode = (String) session.getAttribute("code");
        if (!code.equals(sessionCode)){
            return Result.build(null,ResultCodeEnum.CODE_ERROR);
        }

        //判断手机号是否存在
        UserInfo userInfo = userInfoService.getUserInfoByPhone(phone);
        if (null != userInfo){
            //返回手机号已经注册
            return Result.build(null,ResultCodeEnum.PHONE_REGISTER_ERROR);
        }

        //将数据存入数据库
        UserInfo userInfo1 = new UserInfo();
        userInfo1.setPhone(phone);
        userInfo1.setNickName(nickName);
        userInfo1.setPassword(MD5.encrypt(password));

        userInfo1.setStatus(1);
        userInfoService.insert(userInfo1);

        //调用userinfoservice中插入用户的方法re
        return  Result.ok();
    }

    @RequestMapping("/login")
    public Result login(@RequestBody LoginVo loginVo , HttpSession session){
        String phone = loginVo.getPhone();
        String password = loginVo.getPassword();
        if (StringUtils.isEmpty(password)||StringUtils.isEmpty(phone)){
            return Result.build(null,ResultCodeEnum.PARAM_ERROR);
        }

        //根据手机号查询用户信息
        UserInfo userInfoByPhone = userInfoService.getUserInfoByPhone(phone);
        if (userInfoByPhone == null){
            return Result.build(null,ResultCodeEnum.ACCOUNT_ERROR);
        }

        //验证密码
        if (!MD5.encrypt(password).equals(userInfoByPhone.getPassword())){
            return Result.build(null,ResultCodeEnum.PARAM_ERROR );
        }
        //判断用户是否被锁定
        if (userInfoByPhone.getStatus() == 0){
            return Result.build(null,ResultCodeEnum.ACCOUNT_LOCK_ERROR);
        }


        //将用户信息存放在session中 便于后台判断用户是否登录 与前台显示无关
        session.setAttribute("user" ,userInfoByPhone);

        //用于存放用户昵称
        Map map= new HashMap<>();

        map.put("nickName",userInfoByPhone.getNickName());
        map.put("phone",userInfoByPhone.getPhone());
        return Result.ok(map);

    }

    @RequestMapping("/logout")
    public Result logout(HttpSession session){
        session.removeAttribute("user");
        return Result.ok();
    }

}
