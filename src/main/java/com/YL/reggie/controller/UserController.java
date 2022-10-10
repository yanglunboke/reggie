package com.YL.reggie.controller;

import com.YL.reggie.common.R;
import com.YL.reggie.entity.User;
import com.YL.reggie.service.UserService;
import com.YL.reggie.utils.SMSUtils;
import com.YL.reggie.utils.ValidateCodeUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 移动端用户发送验证码
     * @param user
     * @param session
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        //获取手机号
        String phone=user.getPhone();

        if(phone!=null){
            //生成随机的四位验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info(code);
            //调用阿里云提供的短信服务API完成发送短信
            SMSUtils.sendMessage("阿里云短信测试", "SMS_154950909",phone,code);
            //需要将生成的验证码保存到Session
            session.setAttribute(phone,code);

            return R.success("手机验证码发送成功");
        }

        return R.error("短信发送失败");
    }

    /**
     * 移动端用户登录
     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map user,HttpSession session){
        log.info(user.toString());
        //获取手机号
        String phone = user.get("phone").toString();
        //获取验证码
        String code = user.get("code").toString();
        //从Session中获取保存的验证码
        Object codeInSession = session.getAttribute(phone);
        //进行验证码的比对（页面提交的验证码和Session中保存的验证码）
        if(codeInSession!=null&&codeInSession.equals(code)){
            //如果能够比对成功，说明登陆成功
            //判断当前手机号对应的用户是否为新用户，如果是新用户则自动完成注册
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,phone);

            User user1=userService.getOne(queryWrapper);
            if(user1==null){
                //是新用户
                user1 = new User();
                user1.setPhone(phone);
                userService.save(user1);
            }
            session.setAttribute("user",user1.getId());
            return R.success(user1);
        }
        return R.error("登录失败");
    }

    @PostMapping("/loginout")
    public R<String> loginout(HttpServletRequest request){

        request.getSession().removeAttribute("user");
        return R.success("退出成功");
    }
}
