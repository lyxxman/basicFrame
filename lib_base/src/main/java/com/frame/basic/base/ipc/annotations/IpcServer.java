package com.frame.basic.base.ipc.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description: 使用该注解的方法会自动生成IPC代理类
 * @Author: fanj
 * @CreateDate: 2022/8/2 11:07
 * @Version:
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface IpcServer {
}
