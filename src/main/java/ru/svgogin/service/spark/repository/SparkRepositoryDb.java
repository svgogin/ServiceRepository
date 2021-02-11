package ru.svgogin.service.spark.repository;

import java.math.BigInteger;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.svgogin.service.spark.entity.Company;

@Repository
public interface SparkRepositoryDb extends CrudRepository<Company, BigInteger> {
  Optional<Company> findByInn(String inn);

  boolean existsByInn(String inn);

  void deleteByInn(String inn);
}
