package com.frame.basic.base.ipc.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description: 使用该注解的方法会自动生成到IPC代理类中
 * @Author: fanj
 * @CreateDate: 2022/8/2 11:08
 * @Version:
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface IpcApi {
}
