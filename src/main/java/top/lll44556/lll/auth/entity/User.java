package top.lll44556.lll.auth.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    private Long id;

    private String username;

    private String password;

    private Integer orgId;

    private Integer enabled;

    private String phone;

    private String email;

    private Date createTime;

    private static final long serialVersionUID = 1L;
}