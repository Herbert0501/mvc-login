package top.kangyaocoding.training.dto;

import lombok.Data;

/**
 * 描述: 登录表单数据传输对象
 *
 * @author K·Herbert
 * @since 2024-06-22 15:38
 */

@Data
public class LoginFormDTO {
    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 验证码
     */
    private String code;

    /**
     * 用户密码
     */
    private String password;
}

