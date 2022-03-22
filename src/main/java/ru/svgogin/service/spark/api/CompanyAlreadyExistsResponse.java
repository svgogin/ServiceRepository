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
@ApiResponse(responseCode = "409",
             description = "Company already exists",
             content = {
                 @Content(
                     examples = @ExampleObject(
                         value = """
                             [
                                 {
                                     "code": "ERROR001",
                                     "message": "EntityAlreadyExists (7729355618)"
                                 }
                             ]"""
                     ),
                     mediaType = "application/json",
                     array = @ArraySchema(schema = @Schema(implementation = ErrorDto.class)))
             })
public @interface CompanyAlreadyExistsResponse {
}
