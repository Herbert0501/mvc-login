package top.kangyaocoding.training.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import top.kangyaocoding.training.dto.LoginFormDTO;
import top.kangyaocoding.training.dto.UpdateFormDTO;
import top.kangyaocoding.training.dto.UserDTO;
import top.kangyaocoding.training.service.user.IUserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * 描述:
 *
 * @author K·Herbert
 * @since 2024-06-22 20:15
 */

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private IUserService userService;

    /**
     * 发送验证码
     */
    @PostMapping("/code")
    public Map<String, String> sendVerificationCode(@RequestParam("email") String email, HttpSession session) {
        // 发送短信验证码并保存验证码
        return userService.sendVerificationCode(email, session);
    }

    /**
     * 邮箱验证码登录功能
     *
     * @param loginForm 登录参数，包含手机号、邮箱、验证码；
     */
    @PostMapping("/login/code")
    public Map<String, String> loginByVerificationCode(@RequestBody LoginFormDTO loginForm, HttpSession session) {
        // 实现登录功能
        return userService.loginByVerificationCode(loginForm, session);
    }

    /**
     * 账号密码登录功能
     *
     * @param loginForm 登录参数，包含手机号、邮箱、密码；
     */
    @PostMapping("/login/password")
    public Map<String, String> loginByPassword(@RequestBody LoginFormDTO loginForm, HttpSession session) {
        // 实现登录功能
        return userService.loginByPassword(loginForm, session);
    }

    @PostMapping("/update/password")
    public Boolean updatePassword(@RequestBody UpdateFormDTO request){
        // 更新密码
        return userService.updatePassword(request);
    }

    /**
     * 登出功能
     */
    @PostMapping("/logout")
    public Map<String, String> logoutUser(HttpServletRequest request) {
        // 实现登出功能
        return userService.logoutUser(request);
    }

    @GetMapping("/me")
    public UserDTO getCurrentUser() {
        // 查询当前用户详情
        return userService.getCurrentUser();
    }

    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable("id") Long userId) {
        // 通过ID查询用户详情
        return userService.getUserById(userId);
    }
}
