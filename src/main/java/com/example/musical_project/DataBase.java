package com.example.musical_project;

import java.sql.*;
import java.util.ArrayList;
import java.util.stream.Stream;

public class DataBase {
    public static final String MY_NAME = "root";
    public static final String PASSWORD = "viktorM03";
    public static final String URI = "jdbc:mysql://localhost:3306/musicbase";
    public static Statement statement;
    public static Connection connection;
    public static String loginUser;

    public void setLoginUser (String loginUser){
        this.loginUser = loginUser;
    }

    static {
        try {
            connection = DriverManager.getConnection(URI, MY_NAME, PASSWORD);
        } catch (SQLException throwables) {
            throwables.getStackTrace();
            throw new RuntimeException();
        }
    }
    static {
        try{
            statement = connection.createStatement();
        } catch (SQLException throwables){
            throwables.getStackTrace();
            throw new RuntimeException();
        }
    }


    public void writeDownRegistration(UserData userData){
        try {
            statement.executeUpdate("INSERT INTO " + ConstRegistr.USER_TABLE + "(" + ConstRegistr.USER_NAME + "," + ConstRegistr.USER_SECOND_NAME
                    + "," + ConstRegistr.USER_LOGIN + "," + ConstRegistr.USER_PASSWORD + "," + ConstRegistr.USER_EMAIL + ")" +
                    "VALUES('" + userData.getName() + "','" + userData.getSecondName() + "','" + userData.getLogin() + "','" + userData.getPassword() + "','" + userData.getEmail() + "')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Thats ok");
    }

    public ResultSet getUser(UserData user){
        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery("SELECT * FROM " + ConstRegistr.USER_TABLE + " WHERE "
                    + ConstRegistr.USER_LOGIN + "='" + user.getLogin() + "' AND "
                    + ConstRegistr.USER_PASSWORD + "='" + user.getPassword() + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public void createrAlbumFunction(ArrayList<String> albumMusic, String nameAlbum){
        try {
            statement.execute("INSERT INTO " + ConstRegistr.ALBUMNAME_TABLE + "(" + ConstRegistr.ALBUMNAME_ID_USER + "," + ConstRegistr.ALBUMNAME_NAME_PLAYLIST
                    + ")" + "VALUES('" + loginUser + "','" + nameAlbum + "')");
            for(String nameSong : albumMusic){
                statement.execute("INSERT INTO " + ConstRegistr.SONGLIST_TABLE + "(" + ConstRegistr.SONGLIST_ID_PLAYLIST + "," + ConstRegistr.SONGLIST_MUSICLIST
                        + ")" + "VALUES('" + nameAlbum + "','" + nameSong + "')");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> arrayListAlbum(){
        ArrayList<String> stringArrayListAlbum = new ArrayList<String>();
        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery("SELECT * FROM " + ConstRegistr.ALBUMNAME_TABLE + " WHERE "
                    + ConstRegistr.ALBUMNAME_ID_USER + "='" + loginUser + "'");
            while(resultSet.next()){
                stringArrayListAlbum.add(resultSet.getString(2));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stringArrayListAlbum;
    }

    public ArrayList<String> arrayMusicAlbum(String albumSelected){
        ArrayList<String> stringArrayListMusic = new ArrayList<String>();
        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery("SELECT * FROM " + ConstRegistr.SONGLIST_TABLE + " WHERE "
                    + ConstRegistr.SONGLIST_ID_PLAYLIST + "='" + albumSelected + "'");
            while(resultSet.next()){
                stringArrayListMusic.add(resultSet.getString(3));
                System.out.println("thats ok");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stringArrayListMusic;
    }
    public void deleteWhat(String nameTable, String nameRow, String nameDeleted){
        if(nameTable == ConstRegistr.ALBUMNAME_TABLE) {
            try {
                statement.execute("DELETE FROM " + ConstRegistr.SONGLIST_TABLE + " WHERE " + ConstRegistr.SONGLIST_ID_PLAYLIST + "='" + nameDeleted + "'");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        try {
            statement.execute("DELETE FROM " + nameTable + " WHERE " + nameRow + "='" + nameDeleted + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}
