package top.lll44556.lll.auth.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @TableName external_account
 */
@TableName(value ="external_account")
@Data
public class ExternalAccount implements Serializable {
    private Long id;

    private Long userId;

    private String provider;

    private Object extraJson;

    private static final long serialVersionUID = 1L;
}