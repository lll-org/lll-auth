package top.lll44556.lll.auth.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * @TableName role
 */
@TableName(value ="role")
@Data
public class Role implements Serializable {
    private Long id;

    private String roleName;

    private String roleDesc;

    private String roleCode;

    private Integer sort;

    private Integer status;

    private static final long serialVersionUID = 1L;
}