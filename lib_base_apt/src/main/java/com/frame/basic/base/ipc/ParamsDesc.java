package com.frame.basic.base.ipc;

import java.io.Serializable;

/**
 * @Description:
 * @Author: fanj
 * @CreateDate: 2022/8/2 13:52
 * @Version:
 */
public class ParamsDesc implements Serializable {
    private Class<?> type;
    private Object value;

    public ParamsDesc(Class<?> type, Object value) {
        this.type = type;
        this.value = value;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
