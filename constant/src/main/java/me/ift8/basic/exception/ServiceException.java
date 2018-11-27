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
public class ServiceException extends Exception {
    private static final long serialVersionUID = 6056337532100279861L;
    private final String errorCode;
    private final String errorMessage;

    public ServiceException() {
        super();
        this.errorCode = "DEFAULT";
        this.errorMessage = "";
    }

    public ServiceException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @JsonCreator
    public ServiceException(@JsonProperty("errorCode") String errorCode, @JsonProperty("errorMessage") String errorMessage, @JsonProperty("cause") Throwable cause) {
        super(errorMessage, cause);

        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}

