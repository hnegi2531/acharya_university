package com.au.dto;

import java.io.Serializable;

public class JwtDetails implements Serializable {

private static final long serialVersionUID = -6090152806392445419L;
private Integer userId;
private String userName;
private String userType;
private String sub;
private long exp;
private long iat;

public Integer getUserId() {
return userId;
}

public void setUserId(Integer userId) {
this.userId = userId;
}

public String getUserName() {
return userName;
}

public void setUserName(String userName) {
this.userName = userName;
}

public String getUserType() {
return userType;
}

public void setUserType(String userType) {
this.userType = userType;
}

public String getSub() {
return sub;
}

public void setSub(String sub) {
this.sub = sub;
}

public long getExp() {
return exp;
}

public void setExp(long exp) {
this.exp = exp;
}

public long getIat() {
return iat;
}

public void setIat(long iat) {
this.iat = iat;
}

@Override
public String toString() {
return "JwtDetails [userId=" + userId + ", userName=" + userName + ", sub=" + sub + ", exp=" + exp + ", iat="
+ iat + "]";
}

}