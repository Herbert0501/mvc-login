package top.kangyaocoding.training.service.user;

import com.baomidou.mybatisplus.extension.service.IService;
import top.kangyaocoding.training.dto.LoginFormDTO;
import top.kangyaocoding.training.dto.UpdateFormDTO;
import top.kangyaocoding.training.dto.UserDTO;
import top.kangyaocoding.training.entity.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * 描述: 用户服务类接口
 *
 * @author K·Herbert
 * @since 2024-06-22 17:34
 */

public interface IUserService extends IService<User> {

    /**
     * 发送验证码到指定邮箱
     *
     * @param email   目标邮箱
     * @param session 当前会话
     * @return 包含验证码的 Map
     */
    Map<String, String> sendVerificationCode(String email, HttpSession session);

    /**
     * 使用邮箱验证码进行登录
     *
     * @param loginForm 登录表单数据传输对象
     * @param session   当前会话
     * @return 包含登录信息的 Map
     */
    Map<String, String> loginByVerificationCode(LoginFormDTO loginForm, HttpSession session);

    /**
     * 使用密码进行登录
     *
     * @param loginForm 登录表单数据传输对象
     * @param session   当前会话
     * @return 包含登录信息的 Map
     */
    Map<String, String> loginByPassword(LoginFormDTO loginForm, HttpSession session);

    /**
     * 用户注销
     *
     * @param request 当前请求
     * @return 包含注销信息的 Map
     */
    Map<String, String> logoutUser(HttpServletRequest request);

    /**
     * 获取当前用户信息
     *
     * @return 当前用户的数据传输对象
     */
    UserDTO getCurrentUser();

    /**
     * 根据用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户的数据传输对象
     */
    UserDTO getUserById(Long userId);

    /**
     * 更新用户密码
     */
    Boolean updatePassword(UpdateFormDTO request);
}
