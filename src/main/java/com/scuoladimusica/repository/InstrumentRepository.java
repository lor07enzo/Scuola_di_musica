package com.scuoladimusica.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scuoladimusica.model.entity.Instrument;


@Repository
public interface InstrumentRepository extends JpaRepository<Instrument, Long> {
    
    Optional<Instrument> findByCodiceStrumento(String codice);
    boolean existsByCodiceStrumento(String codice);

}
