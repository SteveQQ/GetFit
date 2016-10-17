package com.steveq.getfit.model;

public class User {
    private String mUserName;
    private String mPassword;

    public User(String userName, String password) {
        mUserName = userName;
        mPassword = password;
    }

    public String getUserName() {
        return mUserName;
    }

    public String getPassword() {
        return mPassword;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (mUserName != null ? !mUserName.equals(user.mUserName) : user.mUserName != null)
            return false;
        return mPassword != null ? mPassword.equals(user.mPassword) : user.mPassword == null;

    }

    @Override
    public int hashCode() {
        int result = mUserName != null ? mUserName.hashCode() : 0;
        result = 31 * result + (mPassword != null ? mPassword.hashCode() : 0);
        return result;
    }
}
