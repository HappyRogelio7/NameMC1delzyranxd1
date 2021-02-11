package me.zyran.namemc.utils;

public interface Callback<Reply>{

    void done(Reply reply);

    void exception(Exception exception);
}
