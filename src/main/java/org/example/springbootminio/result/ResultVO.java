package org.example.springbootminio.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultVO<T> {
    /** 结果状态 ,正常响应200，其他状态码都为失败*/
    private int code;
    private String msg;
    private T data;

    // Static methods
    /**
     * 成功时候的调用
     */
    public static <T> ResultVO<T> success(T data) {
        return new ResultVO<T>(data, CodeMsg.SUCCESS);
    }
    public static <T> ResultVO<T> success() {
        return new ResultVO<T>(CodeMsg.SUCCESS);
    }

    /**
     * 失败时候的调用
     */
    public static <T> ResultVO<T> error(Integer code, String msg) {
        return new ResultVO<T>(code, msg);
    }
    public static <T> ResultVO<T> error(CodeMsg codeMsg) {
        return new ResultVO<T>(codeMsg);
    }
    public static <T> ResultVO<T> error(String msg) {
        CodeMsg codeMsg = new CodeMsg(HttpStatus.INTERNAL_SERVER_ERROR.value(), msg);
        return new ResultVO<T>(codeMsg);
    }

    // Constructor
    private ResultVO(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private ResultVO(T data, CodeMsg codeMsg) {
        this.data = data;
        if (codeMsg != null) {
            this.code = codeMsg.getCode();
            this.msg = codeMsg.getMsg();
        }
    }
    private ResultVO(CodeMsg codeMsg) {
        if (codeMsg != null) {
            this.code = codeMsg.getCode();
            this.msg = codeMsg.getMsg();
        }
    }

}