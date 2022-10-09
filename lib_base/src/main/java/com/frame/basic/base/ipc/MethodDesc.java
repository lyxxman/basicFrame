package com.frame.basic.base.ipc;

import java.io.Serializable;
import java.util.List;

/**
 * @Description:
 * @Author: fanj
 * @CreateDate: 2022/8/2 13:52
 * @Version:
 */
public class MethodDesc implements Serializable {
    private String name;
    private List<ParamsDesc> params;
    private Class<?> result;

    public MethodDesc(String name, List<ParamsDesc> params, Class<?> result) {
        super();
        this.name = name;
        this.params = params;
        this.result = result;
    }

    public MethodDesc() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ParamsDesc> getParams() {
        return params;
    }

    public void setParams(List<ParamsDesc> params) {
        this.params = params;
    }

    public Class<?> getResult() {
        return result;
    }

    public void setResult(Class<?> result) {
        this.result = result;
    }
}
