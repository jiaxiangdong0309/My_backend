package com.peanut.trace.common.result;

/**
 * 统一错误码枚举
 */
public enum ResultCode {

    SUCCESS(200, "操作成功"),
    FAIL(500, "操作失败"),
    UNAUTHORIZED(401, "未登录或Token已过期"),
    FORBIDDEN(403, "权限不足"),
    NOT_FOUND(404, "资源不存在"),
    PARAM_ERROR(400, "请求参数错误"),

    // 用户相关
    USER_NOT_EXIST(1001, "用户不存在"),
    USER_PASSWORD_ERROR(1002, "用户名或密码错误"),
    USER_DISABLED(1003, "账号已被禁用"),
    USER_ALREADY_EXIST(1004, "用户名已存在"),

    // 产品相关
    PRODUCT_NOT_EXIST(2001, "产品不存在"),
    BATCH_NO_DUPLICATE(2002, "批次号已存在"),
    IMPORT_FAIL(2003, "数据导入失败"),

    // 溯源相关
    TRACE_NOT_FOUND(3001, "溯源信息不存在"),
    HASH_VERIFY_FAIL(3002, "数据完整性校验失败，数据可能已被篡改"),
    CHAIN_UPLOAD_FAIL(3003, "区块链上链失败"),

    // 统计相关
    STATS_ERROR(4001, "统计数据查询失败"),
    PDF_EXPORT_FAIL(4002, "PDF报告生成失败");

    private final Integer code;
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() { return code; }
    public String getMessage() { return message; }
}
