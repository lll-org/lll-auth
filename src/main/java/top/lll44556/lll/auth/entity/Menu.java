package top.lll44556.lll.auth.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * @TableName menu
 */
@TableName(value ="menu")
@Data
public class Menu implements Serializable {
    private Long id;

    private Long menuPid;

    private String menuPids;

    private Integer isLeaf;

    private String menuName;

    private String url;

    private String icon;

    private Integer sort;

    private Integer level;

    private Integer status;

    private static final long serialVersionUID = 1L;
}