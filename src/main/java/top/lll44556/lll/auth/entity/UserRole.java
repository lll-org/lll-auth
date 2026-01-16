package top.lll44556.lll.auth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * @TableName user_role
 */
@TableName(value ="user_role")
@Data
public class UserRole implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long userId;

    private Long roleId;

    private static final long serialVersionUID = 1L;
}