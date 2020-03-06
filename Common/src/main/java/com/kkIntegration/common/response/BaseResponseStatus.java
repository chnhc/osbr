package com.kkIntegration.common.response;

public enum BaseResponseStatus {

    SUCCESS(20000,"成功"),

    TEST_FAIL(44044,"测试失败"),

    TEST_T_FAIL(44024,"测试时真实失败"),

    Running_Failed(40000,"运行时出现异常"),

    //Token_Validation_Failed(40100,"Token验证失败"),
    Role_Failed(40101,"权限问题"),
    Token_Failed(40102,"Token验证时出现问题"),


    Verify_Failed(40202,"参数验证错误"),

    BasicAuth_Failed(40401,"请检查Basic Auth"),
    //Check_Failed(40402,"验证失败！"),
    GetToken_Failed(40404,"请检查用户名，密码，Basic Auth，grant_type");


    public int status;
    public String message;

    BaseResponseStatus(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public static String getMessageByStatus(int status) {
        BaseResponseStatus[] values = BaseResponseStatus.values();
        for (BaseResponseStatus type : values) {
            if (type.status == status) {
                return type.message;
            }
        }
        return "";
    }

    public static int getStatusByMessage(String message) {
        BaseResponseStatus[] values = BaseResponseStatus.values();
        for (BaseResponseStatus type : values) {
            if (type.message.equalsIgnoreCase(message)) {
                return type.status;
            }
        }
        return 0;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
