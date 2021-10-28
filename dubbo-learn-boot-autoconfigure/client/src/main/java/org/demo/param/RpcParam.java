package org.demo.param;

import lombok.Data;

import java.io.Serializable;

/**
 * @author luwt
 * @date 2021/10/28
 */
@Data
public class RpcParam implements Serializable {

    private static final long serialVersionUID = 1940390221864919513L;

    private int id;

    private String name;

    private String message;

}
