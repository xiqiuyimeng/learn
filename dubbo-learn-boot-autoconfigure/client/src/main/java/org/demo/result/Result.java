package org.demo.result;

import lombok.Data;

import java.io.Serializable;

/**
 * @author luwt
 * @date 2021/10/28
 */
@Data
public class Result implements Serializable {

    private static final long serialVersionUID = 2650040553169115565L;

    private int id;

    private String name;

    private String message;
}
