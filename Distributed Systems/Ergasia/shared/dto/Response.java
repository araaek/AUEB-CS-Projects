package shared.dto;

import java.io.Serializable;
import java.util.List;

public class Response implements Serializable {
    private static final long serialVersionUID = 5L;

    public enum Status { SUCCESS, FAILED, PARTIAL_SUCCESS }

    private Status status;
    private String message;
    private Object data;

    public Response(Status status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public Status getStatus() { return status; }
    public String getMessage() { return message; }
    public Object getData() { return data; }

    @Override
    public String toString() {
        return "Response{" +
               "status=" + status +
               ", message='" + message + '\'' +
               ", data=" + (data instanceof List ? "List[size=" + ((List<?>) data).size() + "]" : data) +
               '}';
    }
}