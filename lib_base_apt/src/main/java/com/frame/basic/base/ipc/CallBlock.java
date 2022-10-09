package com.frame.basic.base.ipc;

/**
 * @Description:
 * @Author: fanj
 * @CreateDate: 2022/8/2 17:10
 * @Version:
 */
public abstract class CallBlock<T> {
    abstract void success(T data);
    void error(String error){}
}
