package org.demo.learn.exception;

/**
 * 异常枚举类，实现了基础异常类接口，定义标准错误码
 * 实现了业务异常断言类接口，获得断言能力
 * @author luwt
 * @date 2020/5/13.
 */
public enum ExceptionEnum implements BaseExceptionInter, BusinessExceptionAssert {

    SUCCESS(1000, "ok"),
    HANDLER_NOT_FOUND(404, "未找到合适的处理器"),
    ILLEGAL_PARAM(-1, "参数非法"),
    NPE(1, "空指针异常"),
    UNDEFINED(2, "未知异常");

    private int code;

    private String message;

    ExceptionEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode(){
        return code;
    }

    @Override
    public String getMessage(){
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
