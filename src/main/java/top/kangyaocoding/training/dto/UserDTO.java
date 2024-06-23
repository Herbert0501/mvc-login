package top.kangyaocoding.training.dto;

import lombok.Data;

/**
 * 描述: 用户数据传输对象类
 *
 * @author K·Herbert
 * @since 2024-06-22 15:37
 */

@Data
public class UserDTO {
    /**
     * 用户唯一标识ID
     */
    private Long id;
    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * 用户头像
     */
    private String icon;
}

