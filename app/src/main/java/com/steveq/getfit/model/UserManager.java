package com.steveq.getfit.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.steveq.getfit.BuildConfig;
import com.steveq.getfit.R;
import com.steveq.getfit.controller.LoginActivity;
import com.steveq.getfit.controller.MainActivity;

import org.apache.commons.lang.RandomStringUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserManager {

    private static UserManager instance = null;
    private Context mContext;
    private static Set<String> mUserNames;
    private User mCurrentUser;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private Gson gson;
    public static final String KEY_USERS_SET = "key_users_set";


    protected UserManager(Context ctx, SharedPreferences sPref, SharedPreferences.Editor sEdit){
        mContext = ctx;
        mUserNames = new HashSet<>();
        mSharedPreferences = sPref;
        mEditor = sEdit;
        gson = new Gson();
    }

    public User getCurrentUser() {
        return mCurrentUser;
    }

    public static UserManager getInstance(Context ctx, SharedPreferences sharedPreferences, SharedPreferences.Editor editor){
        if(instance == null){
            instance = new UserManager(ctx, sharedPreferences, editor);
        }
        return instance;
    }

    public static UserManager getInstance(){
        if(instance != null){
            return instance;
        }
        return null;
    }

    //******GETTERS SETTERS******//
    public Set<String> getUserNames() {
        return mUserNames;
    }

    public void setUserName(HashSet<String> input) {
        if(input != null) {
            mUserNames.addAll(input);
        }

    }
    //******GETTERS SETTERS******//

    //******USERS SERVICES*****//
    public void changePassword(Context ctx, String password, String newPassword){
        if(     validate(password)              &&
                validate(newPassword)           &&
                newPassword.equals(password)){
            if(!passwordMatches(mCurrentUser, password)) {
                User user = loadUser(mCurrentUser.getUserName());
                user.setPassword(newPassword);
                saveUser(mCurrentUser.getUserName(), user);
                Toast.makeText(ctx, "Password Changed", Toast.LENGTH_LONG).show();
                logOut(ctx);
            } else {
                Toast.makeText(ctx, "Password duplicate", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(ctx, "Insert valid credentials", Toast.LENGTH_LONG).show();
        }
    }

    public boolean changeUsername(Context ctx, String newUsername, String password){
        if(credentialValidation(newUsername, password)){
            if(passwordMatches(loadUser(mCurrentUser.getUserName()), password)) {
                User user = loadUser(mCurrentUser.getUserName());
                user.setUserName(newUsername);
                saveUser(newUsername, user);
                registerUser(newUsername);
                deleteUser(mCurrentUser.getUserName());
                logOut(ctx);
                return true;
            } else {
                Toast.makeText(ctx, "Insert correct password", Toast.LENGTH_LONG).show();
                return false;
            }
        }else {
            Toast.makeText(ctx, "Insert valid credentials", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public boolean createNewUser(Context ctx, String username, String password){
        if(credentialValidation(username, password) && !mUserNames.contains(username)){
            saveUser(username, new User(username, password));
            registerUser(username);
            return true;
        } else {
            Toast.makeText(ctx, "User already exists", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public boolean logIn(Context ctx, String username, String password){
        if(credentialValidation(username, password) && mUserNames.contains(username)){
            if(passwordMatches(loadUser(username), password)){
                mCurrentUser = loadUser(username);
                Intent intent = new Intent(mContext, MainActivity.class);
                mContext.startActivity(intent);
                Toast.makeText(ctx, "Hello!"+ RandomStringUtils.random(10, mContext.getResources().getString(R.string.alpha_num).toCharArray()), Toast.LENGTH_SHORT).show();
                return true;
            } else {
                Toast.makeText(ctx, "Wrong Password", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(ctx, "No such user", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public boolean logOut(Context ctx){
        Intent intent = new Intent(ctx, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ctx.startActivity(intent);
        return true;
    }

    public boolean removeUser(Context ctx, String password){
        if(credentialValidation(mCurrentUser.getUserName(), password) && mUserNames.contains(mCurrentUser.getUserName())){
            if(passwordMatches(loadUser(mCurrentUser.getUserName()), password)){
                deleteUser(mCurrentUser.getUserName());
                Toast.makeText(ctx, "User Removed", Toast.LENGTH_LONG).show();
                logOut(ctx);
                return true;
            } else {
                Toast.makeText(ctx, "Wrong Password", Toast.LENGTH_LONG).show();
                return false;
            }
        } else {
            Toast.makeText(ctx, "Wrong Credentials", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    //******USERS SERVICES*****//

    //******HELPER METHODS*****//
    public void initializeUsersList(){
        setUserName((HashSet<String>) mSharedPreferences.getStringSet(KEY_USERS_SET, null));
    }

    private boolean registerUser(String user){
        mUserNames.add(user);
        mEditor.putStringSet(KEY_USERS_SET, mUserNames);
        mEditor.commit();
        return true;
    }

    private boolean passwordMatches(User user, String password){
        return user.getPassword().equals(password);
    }


    private boolean validate(String input){
        Pattern pattern = Pattern.compile("[a-zA-Z\\d]*");
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    private boolean credentialValidation(String username, String password){
        if(!validate(username) || username.equals("")) {
            Toast.makeText(mContext, "Insert valid User Name", Toast.LENGTH_SHORT).show();
        } else if(!validate(password) || password.equals("")){
            Toast.makeText(mContext, "Insert valid Password", Toast.LENGTH_SHORT).show();
        } else {
            return true;
        }
        return false;
    }

    private User loadUser(String username){
        String jsonUser = mSharedPreferences.getString(username, "");
        return gson.fromJson(jsonUser, User.class);
    }

    public boolean saveUser(String username, User user){

        String jsonUser = gson.toJson(user);
        mEditor.putString(username, jsonUser);
        mEditor.commit();

        return true;
    }

    private boolean deleteUser(String username){
        mEditor.remove(username);
        mUserNames.remove(username);
        mEditor.putStringSet(KEY_USERS_SET, mUserNames);
        mEditor.commit();
        return true;
    }
    //******HELPER METHODS*****//
}
