package com.absurd.circle.data.cache;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.absurd.circle.data.cache.util.Column;
import com.absurd.circle.data.cache.util.SQLiteTable;
import com.absurd.circle.data.model.User;

/**
 * Created by absurd on 14-3-27.
 */
public class UserDBManager extends BaseDBManager{


    public UserDBManager(Context context) {
        super(context);
    }

    public void insertUser(User user){
        ContentValues value = new ContentValues();
        value.put(UserDBInfo.ID, user.getId());
        value.put(UserDBInfo.USER_ID, user.getUserId());
        value.put(UserDBInfo.NAME, user.getName());
        value.put(UserDBInfo.LOGIN_NAME, user.getLoginName());
        value.put(UserDBInfo.PASSWORD, user.getPassword());
        value.put(UserDBInfo.SEX,user.getSex());
        value.put(UserDBInfo.AGE,user.getAge().getTime());
        value.put(UserDBInfo.PERMISSION, user.getPermission());
        value.put(UserDBInfo.EMAIL, user.getEmail());
        value.put(UserDBInfo.QQ, user.getQq());
        value.put(UserDBInfo.PHONE, user.getPhone());
        value.put(UserDBInfo.LOCATION, user.getLocation());
        value.put(UserDBInfo.DATE, user.getDate().getTime());
        value.put(UserDBInfo.AVATAR, user.getAvatar());
        value.put(UserDBInfo.DESCRIPTION, user.getDescription());
        value.put(UserDBInfo.TOKEN, user.getToken());
        value.put(UserDBInfo.CHANNEL_URI, user.getChannelUri());
        value.put(UserDBInfo.OS_NAME, user.getOsName());
        value.put(UserDBInfo.LOGIN_TYPE,user.getLoginType());
        value.put(UserDBInfo.HOBBY, user.getHobby());
        value.put(UserDBInfo.PROFESSION, user.getProfession());
        value.put(UserDBInfo.BACKGROUND_IMAGE, user.getBackgroundImage());
        value.put(UserDBInfo.CITY, user.getCity());
        value.put(UserDBInfo.LAST_LOGIN_DATE,user.getLastLoginDate().getTime());
        value.put(UserDBInfo.CORPSE,user.getCorpse());
        value.put(UserDBInfo.APP_VER,user.getAppVer());
        value.put(UserDBInfo.IS_ANNOYMITY, user.getIsAnnoymity());
        mDatabase.insert(UserDBInfo.TABLE_NAME,null,value);
    }

    public void updateUser(User user){
        deleteUser();
        insertUser(user);
    }

    public void deleteUser(){
        String sql = "delete from " + UserDBInfo.TABLE_NAME;
        mDatabase.execSQL(sql);
    }

    public User getUser(){
        Cursor cursor = mDatabase.query(UserDBInfo.TABLE_NAME,null,null,null,null,null,null);
        User user = new User();
        if(cursor.moveToFirst()){
            cursor.move(0);
            user.setId(cursor.getInt(1));
            user.setUserId(cursor.getString(2));
            user.setName(cursor.getString(3));
            user.setLoginName(cursor.getString(4));
            user.setPassword(cursor.getString(5));
            user.setSex(cursor.getString(6));
            user.setAge(new java.sql.Date(cursor.getLong(7)));
            user.setPermission(cursor.getInt(8));
            user.setEmail(cursor.getString(9));
            user.setQq(cursor.getString(10));
            user.setPhone(cursor.getString(11));
            user.setLocation(cursor.getString(12));
            user.setDate(new java.sql.Date(cursor.getLong(13)));
            user.setAvatar(cursor.getString(14));
            user.setDescription(cursor.getString(15));
            user.setToken(cursor.getString(16));
            user.setChannelUri(cursor.getString(17));
            user.setOsName(cursor.getString(18));
            user.setLoginType(cursor.getInt(19));
            user.setHobby(cursor.getString(20));
            user.setProfession(cursor.getString(21));
            user.setBackgroundImage(cursor.getString(22));
            user.setCity(cursor.getString(23));
            user.setLastLoginDate(new java.sql.Date(cursor.getLong(24)));
            user.setCorpse(cursor.getInt(25));
            user.setAppVer(cursor.getString(26));
            user.setIsAnnoymity(cursor.getInt(27));
            return user;
        }
        return null;
    }




    public static class UserDBInfo {

        public UserDBInfo() {

        }

        public static final String TABLE_NAME = "user";

        public static final String ID = "id";

        public static final String USER_ID = "userid";

        public static final String NAME = "name";

        public static final String LOGIN_NAME = "loginname";

        public static final String PASSWORD = "password";

        public static final String SEX = "sex";

        public static final String AGE = "age";

        public static final String PERMISSION = "permission";

        public static final String EMAIL = "email";

        public static final String QQ = "qq";

        public static final String PHONE = "phone";

        public static final String LOCATION = "location";

        public static final String DATE = "date";

        public static final String AVATAR = "avatar";

        public static final String DESCRIPTION = "description";

        public static final String TOKEN = "token";

        public static final String CHANNEL_URI = "channeluri";

        public static final String OS_NAME = "osname";

        public static final String LOGIN_TYPE = "logintype";

        public static final String HOBBY = "hobby";

        public static final String PROFESSION = "profession";

        public static final String BACKGROUND_IMAGE = "backgroundimage";

        public static final String CITY = "city";

        public static final String LAST_LOGIN_DATE = "lastlogindate";

        public static final String CORPSE = "corpse";

        public static final String APP_VER = "appver";

        public static final String IS_ANNOYMITY = "isannoymity";


        public static final SQLiteTable TABLE = new SQLiteTable(TABLE_NAME)
                .addColumn(ID, Column.DataType.INTEGER)
                .addColumn(USER_ID, Column.DataType.TEXT)
                .addColumn(NAME, Column.DataType.TEXT)
                .addColumn(LOGIN_NAME, Column.DataType.TEXT)
                .addColumn(PASSWORD, Column.DataType.TEXT)
                .addColumn(SEX, Column.DataType.TEXT)
                .addColumn(AGE, Column.DataType.INTEGER)
                .addColumn(PERMISSION, Column.DataType.INTEGER)
                .addColumn(EMAIL, Column.DataType.TEXT)
                .addColumn(QQ, Column.DataType.TEXT)
                .addColumn(PHONE, Column.DataType.TEXT)
                .addColumn(LOCATION, Column.DataType.TEXT)
                .addColumn(DATE, Column.DataType.INTEGER)
                .addColumn(AVATAR, Column.DataType.TEXT)
                .addColumn(DESCRIPTION, Column.DataType.TEXT)
                .addColumn(TOKEN, Column.DataType.TEXT)
                .addColumn(CHANNEL_URI, Column.DataType.TEXT)
                .addColumn(OS_NAME, Column.DataType.TEXT)
                .addColumn(LOGIN_TYPE, Column.DataType.INTEGER)
                .addColumn(HOBBY, Column.DataType.TEXT)
                .addColumn(PROFESSION, Column.DataType.TEXT)
                .addColumn(BACKGROUND_IMAGE, Column.DataType.TEXT)
                .addColumn(CITY, Column.DataType.TEXT)
                .addColumn(LAST_LOGIN_DATE, Column.DataType.INTEGER)
                .addColumn(CORPSE, Column.DataType.INTEGER)
                .addColumn(APP_VER, Column.DataType.TEXT)
                .addColumn(IS_ANNOYMITY, Column.DataType.INTEGER);

    }
}
