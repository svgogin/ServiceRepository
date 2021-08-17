package ru.svgogin.service.spark.web;

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
import ru.svgogin.service.spark.service.SparkService;

@RestController
@RequestMapping("/spark/companies")
@Validated
public class SparkController {
  private static final Logger log = LoggerFactory.getLogger(SparkController.class);
  private final SparkService sparkService;

  public SparkController(SparkService sparkService) {
    this.sparkService = sparkService;
  }

  @GetMapping()
  public Iterable<CompanyDto> getCompanies() {
    log.info("getCompanies");
    return sparkService.findAll();
  }

  @GetMapping("/{inn}")
  public ResponseEntity<CompanyDto> getCompanyByInn(@PathVariable("inn")
                                                      @Pattern(regexp = "^(\\d{10}|\\d{12})$",
                                                               message = "Inn should contain "
                                                                         + "10 or 12 digits")
                                                          String inn) {
    log.info("getCompanyByInn " + inn);
    return new ResponseEntity<>(sparkService.findByInn(inn), HttpStatus.OK);
  }

  @PostMapping()
  public ResponseEntity<CompanyDto> saveCompany(@Valid @RequestBody CompanyDto companyDto) {
    log.info("saveCompany with inn " + companyDto.getInn());
    return new ResponseEntity<>(sparkService.save(companyDto), HttpStatus.OK);
  }

  @PutMapping("/{inn}")
  public ResponseEntity<CompanyDto> updateCompany(@PathVariable("inn")
                                                    @Pattern(regexp = "^(\\d{10}|\\d{12})$",
                                                             message = "Inn should contain "
                                                        + "10 or 12 digits") String inn,
                                                  @Valid @RequestBody CompanyDto companyDto) {
    log.info("updateCompany " + companyDto.getInn());
    return new ResponseEntity<>(sparkService.update(inn, companyDto), HttpStatus.OK);
  }

  @DeleteMapping("/{inn}")
  public ResponseEntity<CompanyDto> deleteCompany(@PathVariable("inn")
                                                    @Pattern(regexp = "^(\\d{10}|\\d{12})$",
                                                             message = "Inn should contain "
                                                        + "10 or 12 digits") String inn) {
    log.info("deleteCompany " + inn);
    return new ResponseEntity<>(sparkService.delete(inn), HttpStatus.OK);
  }
}
