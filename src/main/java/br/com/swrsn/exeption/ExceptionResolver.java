package br.com.swrsn.exeption;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.exception.ExceptionUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionResolver {

    public static String getRootException(Throwable ex) {
        return String.format("%s in class: %s Line: %s", ExceptionUtils.getRootCauseMessage(ex),
                ExceptionUtils.getRootCause(ex).getStackTrace()[0].getClassName(),
                ExceptionUtils.getRootCause(ex).getStackTrace()[0].getLineNumber());
    }
}
