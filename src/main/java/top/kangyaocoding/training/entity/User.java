package top.kangyaocoding.training.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 描述: 用户实体类
 *
 * @author K·Herbert
 * @since 2024-06-22 15:36
 */

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户主键ID
     * 使用@TableId注解指定该字段为表的主键，并设置主键类型为自动增长。
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户头像
     */
    private String icon = "";

    /**
     * 用户创建时间
     */
    private LocalDateTime createTime;

    /**
     * 用户信息更新时间
     */
    private LocalDateTime updateTime;
}
