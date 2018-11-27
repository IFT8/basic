package me.ift8.basic.exception;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by zhuye on 12/02/2017.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SystemException extends RuntimeException {
    private static final long serialVersionUID = 818647833910214022L;
    private final String errorCode;
    private final String errorMessage;

    public SystemException() {
        super();
        this.errorCode = "DEFAULT";
        this.errorMessage = "";
    }

    public SystemException(String errorCode, String errorMessage) {
        super(errorMessage);

        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @JsonCreator
    public SystemException(@JsonProperty("errorCode") String errorCode, @JsonProperty("errorMessage") String errorMessage, @JsonProperty("cause") Throwable cause) {
        super(errorMessage, cause);

        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}

