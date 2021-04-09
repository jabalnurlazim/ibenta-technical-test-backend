package au.com.ibenta.test.response;

public class ResponseBase {

    private int code;
    private String msg;
    private Object data;

    public static final ResponseBase OK = new ResponseBase(200, "Ok!");
    public static final ResponseBase SUCCESS = new ResponseBase(201, "Success!");
    public static final ResponseBase EMPTY = new ResponseBase(204, "No Content!");
    public static final ResponseBase INVALID_USER = new ResponseBase(404, "User not found");
    public static final ResponseBase INVALID_FIRST_NAME = new ResponseBase(400, "firstName is required");
    public static final ResponseBase INVALID_LAST_NAME = new ResponseBase(400, "lastName is required");
    public static final ResponseBase INVALID_EMAIL = new ResponseBase(400, "email is required and should be a proper email format");
    public static final ResponseBase INVALID_PASSWORD = new ResponseBase(400, "password is required");

    public ResponseBase(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static ResponseBase newSuccessResponse(Object data) {
        ResponseBase response = new ResponseBase(SUCCESS.code, SUCCESS.msg);
        response.data = data;
        return response;
    }

    public static ResponseBase newSuccessResponse(ResponseBase response, Object data) {
        response.data = data;
        return response;
    }

    public static ResponseBase newErrorResponse(ResponseBase responseBase, Object data) {
        ResponseBase base = new ResponseBase(responseBase.code, responseBase.msg);
        base.data = data;
        return base;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public Object getData() {
        return data;
    }

}

