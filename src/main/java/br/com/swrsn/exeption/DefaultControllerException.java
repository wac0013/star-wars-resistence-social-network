package br.com.swrsn.exeption;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper=false)
public class DefaultControllerException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    @JsonIgnore
    private final HttpStatus httpStatusCode;

    private final String code;

    private final String message;

    private final String description;

    private final MultiValueMap<String, String> headers;

    public DefaultControllerExceptionBody getOnlyBody() {
        return DefaultControllerExceptionBody.builder()
                .code(this.code)
                .message(this.message)
                .description(this.description)
                .build();
    }

    public ResponseEntity<Object> getResponse() {
        return ResponseEntity
                .status(this.httpStatusCode)
                .headers(new HttpHeaders(this.headers))
                .body(DefaultControllerExceptionBody.builder()
                        .code(this.code)
                        .message(this.message)
                        .description(this.description)
                        .build());
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class DefaultControllerExceptionBody {
        private String code;

        private String message;

        private String description;

    }
}
