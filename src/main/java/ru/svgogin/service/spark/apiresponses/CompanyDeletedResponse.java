package ru.svgogin.service.spark.apiresponses;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import ru.svgogin.service.spark.dto.CompanyDto;

@Target({PARAMETER, METHOD, FIELD, ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ApiResponse(
    responseCode = "200",
    description = "Company deleted",
    content = {
        @Content(
            examples = @ExampleObject(
                value = """
                    {
                        "id": 3,
                        "inn": "7729355614",
                        "ogrn": "1027700262270",
                        "kpp": "997950001",
                        "fullNameRus": null,
                        "shortNameRus": "АО\\"ДОМ.РФ\\"",
                        "statusName": "Действующая",
                        "statusDate": "2020-11-30"
                    }"""
            ),
            mediaType = "application/json",
            schema = @Schema(implementation = CompanyDto.class))
    })
public @interface CompanyDeletedResponse {
}
