package com.gmail.zyran.dev.utils;

public interface Callback<Reply>{

    void done(Reply reply);

    void exception(Exception exception);
}
