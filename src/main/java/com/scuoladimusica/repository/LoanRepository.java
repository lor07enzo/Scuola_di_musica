package com.scuoladimusica.repository;

import com.scuoladimusica.model.entity.Loan;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    Optional<Loan> findByInstrumentCodiceStrumentoAndDataFineIsNull(String codiceStrumento);
    boolean existsByInstrumentCodiceStrumentoAndDataFineIsNull(String codiceStrumento);
}
