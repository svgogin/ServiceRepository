package ru.svgogin.service.spark.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.svgogin.service.spark.service.SparkService;
import ru.svgogin.service.spark.entity.Company;


@RestController
@RequestMapping ("/spark")
public class SparkController {
    private final SparkService sparkService;

  public SparkController(SparkService sparkService) {
    this.sparkService = sparkService;
    }

  @GetMapping("/companies")
  public Iterable<Company> getCompanies() {
    return sparkService.findAll();
  }

  @GetMapping("/companies/{inn}")
  public Company getCompanyByInn(@PathVariable("inn") String inn) {
    return sparkService.findByInn(inn);
  }
}

