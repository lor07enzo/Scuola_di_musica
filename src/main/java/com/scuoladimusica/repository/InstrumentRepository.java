package com.scuoladimusica.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.scuoladimusica.model.entity.Instrument;
import com.scuoladimusica.model.entity.TipoStrumento;


@Repository
public interface InstrumentRepository extends JpaRepository<Instrument, Long> {
    
    Optional<Instrument> findByCodiceStrumento(String codiceStrumento);

    boolean existsByCodiceStrumento(String codiceStrumento);

    List<Instrument> findByTipoStrumento(TipoStrumento tipoStrumento);

    @Query("SELECT i FROM Instrument i WHERE NOT EXISTS " +
           "(SELECT l FROM Loan l WHERE l.instrument = i AND l.dataFine IS NULL)")
    List<Instrument> findAvailableInstruments();

}
