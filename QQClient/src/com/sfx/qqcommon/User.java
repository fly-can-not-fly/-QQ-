package com.sfx.qqcommon;

import java.io.Serializable;

/**
 * 通讯的客户端
 */
public class User implements Serializable {
    //进行序列化时一定加上这句话
    private static final long serialVersionUID = 1L;
    private String userId;//用户的ID
    private String password;//用户密码
    public User(){}

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User(String useId, String password) {
        this.userId = useId;
        this.password = password;
    }
}
