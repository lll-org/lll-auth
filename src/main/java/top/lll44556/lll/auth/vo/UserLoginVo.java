package top.lll44556.lll.auth.vo;

import top.lll44556.lll.auth.entity.User;

import java.util.Date;

public class UserLoginVo extends User {
    @Override
    public Long getId() {
        return super.getId();
    }

    @Override
    public String getUsername() {
        return super.getUsername();
    }

    @Override
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public Integer getOrgId() {
        return super.getOrgId();
    }

    @Override
    public Integer getEnabled() {
        return super.getEnabled();
    }

    @Override
    public String getPhone() {
        return super.getPhone();
    }

    @Override
    public String getEmail() {
        return super.getEmail();
    }

    @Override
    public Date getCreateTime() {
        return super.getCreateTime();
    }

    @Override
    public void setId(Long id) {
        super.setId(id);
    }

    @Override
    public void setUsername(String username) {
        super.setUsername(username);
    }

    @Override
    public void setPassword(String password) {
        super.setPassword(password);
    }

    @Override
    public void setOrgId(Integer orgId) {
        super.setOrgId(orgId);
    }

    @Override
    public void setEnabled(Integer enabled) {
        super.setEnabled(enabled);
    }

    @Override
    public void setPhone(String phone) {
        super.setPhone(phone);
    }

    @Override
    public void setEmail(String email) {
        super.setEmail(email);
    }

    @Override
    public void setCreateTime(Date createTime) {
        super.setCreateTime(createTime);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
