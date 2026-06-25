package io.github.crewhub.swagger.annotation.document;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.*;

/**
 * Swagger
 * 문서 삭제 문서
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
        summary = "문서 삭제",
        description = """
                작성자 또는 관리자만 문서를 삭제합니다.
                """
)
@ApiResponse(
        responseCode = "200",
        description = "문서가 삭제되었습니다."
)
public @interface DeleteDocumentApi {
}

