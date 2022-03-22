package ru.svgogin.service.spark.api;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import ru.svgogin.service.spark.errordto.ErrorDto;

@Target({PARAMETER, METHOD, FIELD, ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ApiResponse(
    responseCode = "400",
    description = """
        Invalid inn (path or body parameter), kpp or ogrn supplied
        or required body parameter is absent.""",
    content = {
        @Content(
            examples = @ExampleObject(
                value = """
                    [
                        {
                            "code": "ERROR003",
                            "message": "Inn should contain 10 or 12 digits (772)"
                        },
                        {
                            "code": "ERROR003",
                            "message": "InvalidRequestBodyFormat (ogrn)"
                        },
                        {
                            "code": "ERROR003",
                            "message": "InvalidRequestBodyFormat (inn)"
                        },
                        {
                            "code": "ERROR004",
                            "message": "MissingParameter (kpp)"
                        }
                    ]"""
            ),
            mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = ErrorDto.class)))
    })
public @interface InvalidPathAndBodyParamsResponse {
}
