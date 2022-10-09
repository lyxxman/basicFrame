package com.frame.basic.base.ipc;

/**
 * @Description:
 * @Author: fanj
 * @CreateDate: 2022/8/2 17:10
 * @Version:
 */
public abstract class CallBlock<T> {
    public abstract void success(T data);
    public void error(String error){}
}
