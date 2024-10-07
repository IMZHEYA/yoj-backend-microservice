package com.yupi.yojbackendcommon.exception;


import com.yupi.yojbackendcommon.common.ErrorCode;

/**
 * 自定义异常类
 *
 * @author <a href="https://github.com/liyupi">段元哲</a>
 * @from <a href="https://yupi.icu">dyz</a>
 */
public class BusinessException extends RuntimeException {

    /**
     * 错误码
     */
    private final int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();
    }

    public int getCode() {
        return code;
    }
}
