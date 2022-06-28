package ru.svgogin.service.spark.web;

import io.swagger.v3.oas.annotations.Operation;
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
import ru.svgogin.service.spark.api.AllCompaniesFoundResponse;
import ru.svgogin.service.spark.api.CompanyAlreadyExistsResponse;
import ru.svgogin.service.spark.api.CompanyDeletedResponse;
import ru.svgogin.service.spark.api.CompanyNotFoundResponse;
import ru.svgogin.service.spark.api.CompanyRequest;
import ru.svgogin.service.spark.api.InvalidOrMissingBodyParamsResponse;
import ru.svgogin.service.spark.api.InvalidPathAndBodyParamsResponse;
import ru.svgogin.service.spark.api.InvalidPathParamResponse;
import ru.svgogin.service.spark.api.SuccessfulResponse;
import ru.svgogin.service.spark.dto.CompanyDto;
import ru.svgogin.service.spark.security.AdminOnly;
import ru.svgogin.service.spark.security.AllAuthorizedUsers;
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
  @AllCompaniesFoundResponse
  @GetMapping()
  @AllAuthorizedUsers
  public Iterable<CompanyDto> getCompanies() {
    log.info("getCompanies method called");
    return sparkService.findAll();
  }

  @Operation(summary = "Gets a company by inn", tags = "Spark-service")
  @SuccessfulResponse
  @InvalidPathParamResponse
  @CompanyNotFoundResponse
  @GetMapping("/{inn}")
  @AllAuthorizedUsers
  public ResponseEntity<CompanyDto> getCompanyByInn(
      @PathVariable("inn")
      @Pattern(regexp = "^(\\d{10}|\\d{12})$",
               message = "Inn should contain 10 or 12 digits") String inn) {
    log.info("getCompanyByInn method called with inn {}", inn);
    return new ResponseEntity<>(sparkService.findByInn(inn), HttpStatus.OK);
  }

  @Operation(summary = "Creates a new company", tags = "Spark-service")
  @CompanyRequest
  @SuccessfulResponse
  @InvalidOrMissingBodyParamsResponse
  @CompanyAlreadyExistsResponse
  @PostMapping()
  @AllAuthorizedUsers
  public ResponseEntity<CompanyDto> saveCompany(@Valid @RequestBody CompanyDto companyDto) {
    log.info("saveCompany method called with inn {}", companyDto.getInn());
    return new ResponseEntity<>(sparkService.save(companyDto), HttpStatus.OK);
  }

  @Operation(summary = "Updates a new company by inn (path parameter)", tags = "Spark-service")
  @CompanyRequest
  @SuccessfulResponse
  @InvalidPathAndBodyParamsResponse
  @CompanyNotFoundResponse
  @PutMapping("/{inn}")
  @AdminOnly
  public ResponseEntity<CompanyDto> updateCompany(
      @PathVariable("inn")
      @Pattern(regexp = "^(\\d{10}|\\d{12})$",
               message = "Inn should contain 10 or 12 digits") String inn,
      @Valid @RequestBody CompanyDto companyDto) {
    log.info("updateCompany method called with inn {}", companyDto.getInn());
    return new ResponseEntity<>(sparkService.update(inn, companyDto), HttpStatus.OK);
  }

  @Operation(summary = "Deletes a company by inn", tags = "Spark-service")
  @CompanyDeletedResponse
  @InvalidPathParamResponse
  @CompanyNotFoundResponse
  @DeleteMapping("/{inn}")
  @AdminOnly
  public ResponseEntity<CompanyDto> deleteCompany(
      @PathVariable("inn")
      @Pattern(regexp = "^(\\d{10}|\\d{12})$",
               message = "Inn should contain 10 or 12 digits") String inn) {
    log.info("deleteCompany method called with inn {}", inn);
    return new ResponseEntity<>(sparkService.delete(inn), HttpStatus.OK);
  }
}
