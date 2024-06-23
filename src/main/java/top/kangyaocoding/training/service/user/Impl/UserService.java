package top.kangyaocoding.training.service.user.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feiniaojin.gracefulresponse.GracefulResponse;
import com.feiniaojin.gracefulresponse.GracefulResponseDataException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import top.kangyaocoding.training.common.Constants;
import top.kangyaocoding.training.dto.LoginFormDTO;
import top.kangyaocoding.training.dto.UpdateFormDTO;
import top.kangyaocoding.training.dto.UserDTO;
import top.kangyaocoding.training.entity.User;
import top.kangyaocoding.training.mapper.UserMapper;
import top.kangyaocoding.training.service.user.IUserService;
import top.kangyaocoding.training.utils.RegexUtils;
import top.kangyaocoding.training.utils.SendMailUtils;
import top.kangyaocoding.training.utils.UserHolder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 描述: 用户服务类实现
 *
 * @author K·Herbert
 * @since 2024-06-22 17:38
 */
@Slf4j
@Service
public class UserService extends ServiceImpl<UserMapper, User> implements IUserService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private SendMailUtils sendMailUtils;
    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public Map<String, String> sendVerificationCode(String email, HttpSession session) {
        if (RegexUtils.isEmailInvalid(email)) {
            GracefulResponse.raiseException("400", "邮箱格式不正确！");
        }
        String code = RandomUtil.randomNumbers(6);
        // 存到 Redis
        stringRedisTemplate.opsForValue().set(Constants.LOGIN_CODE_KEY + email,
                code, Constants.LOGIN_CODE_TTL, TimeUnit.MINUTES);

        log.info("成功获取验证码: {}", code);
        // 发送邮件
        String subject = "邮箱验证 - Kang Yao Coding";
        String text = email + " 你好：<br>使用下面的动态验证码（OTP）验证你的电子邮件地址。";

        try {
            sendMailUtils.sendHtmlWithCode(subject, text, code, email);
            log.info("成功发送验证码邮件到邮箱: {}", email);
        } catch (Exception e) {
            log.error("发送验证码邮件失败: {}", e.getMessage());
            GracefulResponse.raiseException("500", "发送验证码邮件失败");
        }

        return Collections.singletonMap("code", code);
    }

    @Override
    public Map<String, String> loginByVerificationCode(LoginFormDTO loginForm, HttpSession session) {
        String email = loginForm.getEmail();
        if (RegexUtils.isEmailInvalid(email)) {
            GracefulResponse.raiseException("400", "邮箱格式不正确！");
        }
        // 从Redis获取验证码
        String cacheCode = stringRedisTemplate.opsForValue().get(Constants.LOGIN_CODE_KEY + email);
        String code = loginForm.getCode();
        if (cacheCode == null) {
            log.info("验证码不存在！email: {}", email);
            GracefulResponse.raiseException("400", "验证码不存在！");
        } else if (!code.equals(cacheCode)) {
            log.info("验证码不正确！email: {}", email);
            GracefulResponse.raiseException("400", "验证码不正确！");
        }

        User user = query().eq("email", email).one();
        if (user == null) {
            log.info("用户不存在，开始创建用户。");
            user = createUserWithEmail(email);
            log.info("用户不存在，创建用户完成。User: {}", user);
        }
        return saveUserToRedis(user);
    }

    @Override
    public Map<String, String> loginByPassword(LoginFormDTO loginForm, HttpSession session) {
        String email = loginForm.getEmail();
        if (RegexUtils.isEmailInvalid(email)) {
            GracefulResponse.raiseException("400", "邮箱格式不正确！");
        }
        // 通过邮箱查询用户
        User user = query().eq("email", email).one();
        if (user == null){
            log.error("密码登录，用户不存在！未使用邮箱注册！");
            GracefulResponse.raiseException("401", "用户不存在！请先使用邮箱注册！");
        }
        String password = user.getPassword();
        // 匹配加密后的密码
        if (!passwordEncoder.matches(loginForm.getPassword(), password)) {
            log.error("密码错误！");
            throw new GracefulResponseDataException("401","密码错误！");
        }
        // 将信息保存到 Redis
        return saveUserToRedis(user);
    }

    private Map<String, String> saveUserToRedis(User user) {
        // 将信息保存到 Redis
        String token = UUID.randomUUID().toString();
        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
        // 将 User 转为 HashMap 存储
        String jsonString = JSON.toJSONString(userDTO);
        Map<String, String> userMap = JSON.parseObject(jsonString, new TypeReference<Map<String, String>>() {
        });
        // 存储
        String tokenKey = Constants.LOGIN_USER_KEY + token;
        stringRedisTemplate.opsForHash().putAll(tokenKey, userMap);
        log.info("存储到 Redis 成功, User: {} token: {}", userDTO, tokenKey);
        // 过期时间
        stringRedisTemplate.expire(tokenKey, Constants.LOGIN_USER_TTL, TimeUnit.MINUTES);

        return Collections.singletonMap("token", token);
    }

    private User createUserWithEmail(String email) {
        User user = User.builder().build()
                .setEmail(email)
                .setNickname(Constants.USER_NICK_NAME_PREFIX + RandomUtil.randomNumbers(6));
        save(user);
        return user;
    }

    @Override
    public Map<String, String> logoutUser(HttpServletRequest request) {
        String token = request.getHeader("authorization");
        if (token == null) {
            log.error("退出登录失败, 未登录");
            throw new GracefulResponseDataException("401", "未登录");
        }
        stringRedisTemplate.delete(Constants.LOGIN_USER_KEY + token);
        log.info("退出登录成功, token: {}", token);
        return Collections.singletonMap("logout", "已退出登录");
    }

    @Override
    public UserDTO getCurrentUser() {
        // 获取当前登录的用户并返回
        UserDTO userDTO = UserHolder.getUser();
        if (userDTO == null) {
            throw new GracefulResponseDataException("用户不存在！");
        }
        log.info("获取当前登录用户成功! User: {}", userDTO);
        return userDTO;
    }

    @Override
    public UserDTO getUserById(Long userId) {
        // 查询详情
        User user = this.getById(userId);
        if (user != null) {
            log.info("通过ID查询用户成功! User: {}", user);
            return BeanUtil.copyProperties(user, UserDTO.class);
        }
        throw new GracefulResponseDataException("用户不存在！");
    }

    @Override
    public Boolean updatePassword(UpdateFormDTO request) {
        Long userId = request.getUserId();
        String newPassword = request.getNewPassword();
        // 查询用户
        User user = this.getById(userId);
        if (user == null) {
            log.error("用户不存在！");
            throw new GracefulResponseDataException("用户不存在！");
        }
        // 加密密码
        newPassword = passwordEncoder.encode(newPassword);
        // 更新密码
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", userId).set("password", newPassword);
        boolean updateResult = this.update(updateWrapper);

        if (!updateResult) {
            log.error("修改密码失败! User: {}", user);
            return false;
        }
        log.info("修改密码成功! User: {}", user);
        return true;
    }
}
