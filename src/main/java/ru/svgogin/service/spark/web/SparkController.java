package ru.svgogin.service.spark.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.svgogin.service.spark.dto.CompanyDto;
import ru.svgogin.service.spark.errordto.ErrorDto;
import ru.svgogin.service.spark.service.SparkService;

@Tag(name = "Spark-service", description = "Spark API for companies data management")
@RestController
@RequestMapping("/spark/companies")
@Validated
public class SparkController {
  private static final Logger log = LoggerFactory.getLogger(SparkController.class);
  private final SparkService sparkService;

  public SparkController(SparkService sparkService) {
    this.sparkService = sparkService;
  }

  @Operation(summary = "Gets all companies", tags = "Spark-service")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Found all the companies",
          content = {
              @Content(
                  examples = @ExampleObject(
                      value = "[\n"
                              + "    {\n"
                              + "        \"id\": 1,\n"
                              + "        \"inn\": \"7729355614\",\n"
                              + "        \"ogrn\": \"1027700262270\",\n"
                              + "        \"kpp\": \"997950001\",\n"
                              + "        \"fullNameRus\": null,\n"
                              + "        \"shortNameRus\": \"АО\\\"ДОМ.РФ\\\"\",\n"
                              + "        \"statusName\": \"Действующая\",\n"
                              + "        \"statusDate\": \"2020-11-30\"\n"
                              + "    },\n"
                              + "    {\n"
                              + "        \"id\": 2,\n"
                              + "        \"inn\": \"7725038124\",\n"
                              + "        \"ogrn\": \"1037739527077\",\n"
                              + "        \"kpp\": \"770401001\",\n"
                              + "        \"fullNameRus\": "
                              + "\"АКЦИОНЕРНОЕ ОБЩЕСТВО \\\"БАНК ДОМ.РФ\\\"\",\n"
                              + "        \"shortNameRus\": \"АО\\\"БАНК ДОМ.РФ\\\"\",\n"
                              + "        \"statusName\": \"Действующая\",\n"
                              + "        \"statusDate\": \"2020-11-30\"\n"
                              + "    }\n"
                              + "]"
                  ),
                  mediaType = "application/json",
                  array = @ArraySchema(schema = @Schema(implementation = CompanyDto.class)))
          })
  })
  @GetMapping()
  public Iterable<CompanyDto> getCompanies() {
    log.info("getCompanies");
    return sparkService.findAll();
  }

  @Operation(summary = "Gets a company by inn", tags = "Spark-service")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Found the company by inn",
          content = {
              @Content(
                  examples = @ExampleObject(
                      value = "{\n"
                              + "    \"id\": 3,\n"
                              + "    \"inn\": \"7729355614\",\n"
                              + "    \"ogrn\": \"1027700262270\",\n"
                              + "    \"kpp\": \"997950001\",\n"
                              + "    \"fullNameRus\": null,\n"
                              + "    \"shortNameRus\": \"АО\\\"ДОМ.РФ\\\"\",\n"
                              + "    \"statusName\": \"Действующая\",\n"
                              + "    \"statusDate\": \"2020-11-30\"\n"
                              + "}"
                  ),
                  mediaType = "application/json",
                  schema = @Schema(implementation = CompanyDto.class))
          }),
      @ApiResponse(
          responseCode = "400",
          description = "Invalid inn supplied",
          content = {
              @Content(
                  examples = @ExampleObject(
                      value = "[\n"
                              + "    {\n"
                              + "        \"code\": \"ERROR003\",\n"
                              + "        \"message\": \"Inn should contain 10 or 12 digits (772)\""
                              + "\n"
                              + "    }\n"
                              + "]"
                  ),
                  mediaType = "application/json",
                  array = @ArraySchema(schema = @Schema(implementation = ErrorDto.class)))
          }),
      @ApiResponse(responseCode = "404",
                   description = "Company not found",
                   content = {
                       @Content(
                           examples = @ExampleObject(
                               value = "[\n"
                                       + "    {\n"
                                       + "        \"code\": \"ERROR002\",\n"
                                       + "        \"message\": \"NoSuchEntity (7729355618)\"\n"
                                       + "    }\n"
                                       + "]"
                           ),
                           mediaType = "application/json",
                           array = @ArraySchema(schema = @Schema(implementation = ErrorDto.class)))
                   })
  })

  @GetMapping("/{inn}")
  public ResponseEntity<CompanyDto> getCompanyByInn(
      @PathVariable("inn")
      @Pattern(regexp = "^(\\d{10}|\\d{12})$",
               message = "Inn should contain 10 or 12 digits") String inn) {
    log.info("getCompanyByInn " + inn);
    return new ResponseEntity<>(sparkService.findByInn(inn), HttpStatus.OK);
  }

  @Operation(summary = "Creates a new company", tags = "Spark-service")
  @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "Uses CompanyDto schema without parameter \"id\" "
                    + "If id is passed by a client, ignores it and "
                    + "returns id of created company from DB",
      content = {
          @Content(
              examples = @ExampleObject(
                  value = "{\n"
                          + "    \"inn\": \"7729355614\",\n"
                          + "    \"ogrn\": \"1027700262270\",\n"
                          + "    \"kpp\": \"997950001\",\n"
                          + "    \"fullNameRus\": null,\n"
                          + "    \"shortNameRus\": \"АО\\\"ДОМ.РФ\\\"\",\n"
                          + "    \"statusName\": \"Действующая\",\n"
                          + "    \"statusDate\": \"2020-11-30\"\n"
                          + "}"
              ),
              mediaType = "application/json",
              schema = @Schema(implementation = CompanyDto.class))}
  )
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Company created",
          content = {
              @Content(
                  examples = @ExampleObject(
                      value = "{\n"
                              + "    \"id\": 3,\n"
                              + "    \"inn\": \"7729355614\",\n"
                              + "    \"ogrn\": \"1027700262270\",\n"
                              + "    \"kpp\": \"997950001\",\n"
                              + "    \"fullNameRus\": null,\n"
                              + "    \"shortNameRus\": \"АО\\\"ДОМ.РФ\\\"\",\n"
                              + "    \"statusName\": \"Действующая\",\n"
                              + "    \"statusDate\": \"2020-11-30\"\n"
                              + "}"
                  ),
                  mediaType = "application/json",
                  schema = @Schema(implementation = CompanyDto.class))
          }),
      @ApiResponse(
          responseCode = "400",
          description = "Invalid inn, kpp or ogrn supplied or required parameter is absent. "
                        + "Inn format is checked only in the request body",
          content = {
              @Content(
                  examples = @ExampleObject(
                      value = "[\n"
                              + "    {\n"
                              + "        \"code\": \"ERROR003\",\n"
                              + "        \"message\": \"InvalidRequestFormat (ogrn)\"\n"
                              + "    },\n"
                              + "    {"
                              + "        \"code\": \"ERROR003\",\n"
                              + "        \"message\": \"InvalidRequestFormat (inn)\"\n"
                              + "    },\n"
                              + "    {"
                              + "        \"code\": \"ERROR004\",\n"
                              + "        \"message\": \"MissingParameter (kpp)\"\n"
                              + "    }\n"
                              + "]"
                  ),
                  mediaType = "application/json",
                  array = @ArraySchema(schema = @Schema(implementation = ErrorDto.class)))
          }),
      @ApiResponse(responseCode = "409",
                   description = "Company already exists",
                   content = {
                       @Content(
                           examples = @ExampleObject(
                               value = "[\n"
                                       + "    {\n"
                                       + "        \"code\": \"ERROR001\",\n"
                                       + "        \"message\": \"EntityAlreadyExists (7729355618)\""
                                       + "\n"
                                       + "    }\n"
                                       + "]"
                           ),
                           mediaType = "application/json",
                           array = @ArraySchema(schema = @Schema(implementation = ErrorDto.class)))
                   })
  })
  @PostMapping()
  public ResponseEntity<CompanyDto> saveCompany(@Valid @RequestBody CompanyDto companyDto) {
    log.info("saveCompany with inn " + companyDto.getInn());
    return new ResponseEntity<>(sparkService.save(companyDto), HttpStatus.OK);
  }

  @Operation(summary = "Updates a new company by inn (path parameter)", tags = "Spark-service")
  @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "Uses CompanyDto schema without parameter \"id.\" "
                    + "If id is passed by a client, ignores it and returns "
                    + "id of a created company from DB",
      content = {
          @Content(
              examples = @ExampleObject(
                  value = "{\n"
                          + "    \"inn\": \"7729355614\",\n"
                          + "    \"ogrn\": \"1027700262270\",\n"
                          + "    \"kpp\": \"997950001\",\n"
                          + "    \"fullNameRus\": null,\n"
                          + "    \"shortNameRus\": \"АО\\\"ДОМ.РФ\\\"\",\n"
                          + "    \"statusName\": \"Действующая\",\n"
                          + "    \"statusDate\": \"2020-11-30\"\n"
                          + "}"
              ),
              mediaType = "application/json",
              schema = @Schema(implementation = CompanyDto.class))}
  )
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Company updated",
          content = {
              @Content(
                  examples = @ExampleObject(
                      value = "{\n"
                              + "    \"id\": 3,\n"
                              + "    \"inn\": \"7729355614\",\n"
                              + "    \"ogrn\": \"1027700262270\",\n"
                              + "    \"kpp\": \"997950001\",\n"
                              + "    \"fullNameRus\": null,\n"
                              + "    \"shortNameRus\": \"АО\\\"ДОМ.РФ\\\"\",\n"
                              + "    \"statusName\": \"Действующая\",\n"
                              + "    \"statusDate\": \"2020-11-30\"\n"
                              + "}"
                  ),
                  mediaType = "application/json",
                  schema = @Schema(implementation = CompanyDto.class))
          }),
      @ApiResponse(
          responseCode = "400",
          description = "Invalid inn, kpp or ogrn supplied or required parameter is absent. "
                        + "Inn format is checked in both (path and body) parameters",
          content = {
              @Content(
                  examples = @ExampleObject(
                      value = "[\n"
                              + "    {\n"
                              + "        \"code\": \"ERROR003\",\n"
                              + "        \"message\": \"Inn should contain 10 or 12 digits (772)\""
                              + "\n"
                              + "    },\n"
                              + "    {\n"
                              + "        \"code\": \"ERROR003\",\n"
                              + "        \"message\": \"InvalidRequestFormat (ogrn)\"\n"
                              + "    },\n"
                              + "    {"
                              + "        \"code\": \"ERROR003\",\n"
                              + "        \"message\": \"InvalidRequestFormat (inn)\"\n"
                              + "    },\n"
                              + "    {"
                              + "        \"code\": \"ERROR004\",\n"
                              + "        \"message\": \"MissingParameter (kpp)\"\n"
                              + "    }\n"
                              + "]"
                  ),
                  mediaType = "application/json",
                  array = @ArraySchema(schema = @Schema(implementation = ErrorDto.class)))
          }),
      @ApiResponse(responseCode = "404",
                   description = "Company not found",
                   content = {
                       @Content(
                           examples = @ExampleObject(
                               value = "[\n"
                                       + "    {\n"
                                       + "        \"code\": \"ERROR002\",\n"
                                       + "        \"message\": \"NoSuchEntity (7729355618)\"\n"
                                       + "    }\n"
                                       + "]"
                           ),
                           mediaType = "application/json",
                           array = @ArraySchema(schema = @Schema(implementation = ErrorDto.class)))
                   })
  })
  @PutMapping("/{inn}")
  public ResponseEntity<CompanyDto> updateCompany(
      @PathVariable("inn")
      @Pattern(regexp = "^(\\d{10}|\\d{12})$",
               message = "Inn should contain 10 or 12 digits") String inn,
      @Valid @RequestBody CompanyDto companyDto) {
    log.info("updateCompany " + companyDto.getInn());
    return new ResponseEntity<>(sparkService.update(inn, companyDto), HttpStatus.OK);
  }

  @Operation(summary = "Deletes a company by inn", tags = "Spark-service")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Company deleted",
          content = {
              @Content(
                  examples = @ExampleObject(
                      value = "{\n"
                              + "    \"id\": 3,\n"
                              + "    \"inn\": \"7729355614\",\n"
                              + "    \"ogrn\": \"1027700262270\",\n"
                              + "    \"kpp\": \"997950001\",\n"
                              + "    \"fullNameRus\": null,\n"
                              + "    \"shortNameRus\": \"АО\\\"ДОМ.РФ\\\"\",\n"
                              + "    \"statusName\": \"Действующая\",\n"
                              + "    \"statusDate\": \"2020-11-30\"\n"
                              + "}"
                  ),
                  mediaType = "application/json",
                  schema = @Schema(implementation = CompanyDto.class))
          }),
      @ApiResponse(
          responseCode = "400",
          description = "Invalid inn supplied",
          content = {
              @Content(
                  examples = @ExampleObject(
                      value = "[\n"
                              + "    {\n"
                              + "        \"code\": \"ERROR003\",\n"
                              + "        \"message\": \"Inn should contain 10 or 12 digits (772)\""
                              + "\n"
                              + "    }\n"
                              + "]"
                  ),
                  mediaType = "application/json",
                  array = @ArraySchema(schema = @Schema(implementation = ErrorDto.class)))
          }),
      @ApiResponse(responseCode = "404",
                   description = "Company not found",
                   content = {
                       @Content(
                           examples = @ExampleObject(
                               value = "[\n"
                                       + "    {\n"
                                       + "        \"code\": \"ERROR002\",\n"
                                       + "        \"message\": \"NoSuchEntity (7729355618)\"\n"
                                       + "    }\n"
                                       + "]"
                           ),
                           mediaType = "application/json",
                           array = @ArraySchema(schema = @Schema(implementation = ErrorDto.class)))
                   })
  })
  @DeleteMapping("/{inn}")
  public ResponseEntity<CompanyDto> deleteCompany(
      @PathVariable("inn")
      @Pattern(regexp = "^(\\d{10}|\\d{12})$",
               message = "Inn should contain 10 or 12 digits") String inn) {
    log.info("deleteCompany " + inn);
    return new ResponseEntity<>(sparkService.delete(inn), HttpStatus.OK);
  }
}
