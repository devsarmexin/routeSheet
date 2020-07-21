package com.sepfort.sheet.repo;

import com.sepfort.sheet.domain.RouteSheet;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RouteSheetRepo extends CrudRepository<RouteSheet, Integer> {
    Optional<RouteSheet> findById(Integer integer);

    @Query(value = "SELECT MAX (id) AS id FROM RouteSheet")
    Integer findMaxId();

    RouteSheet findByTripDate(LocalDate tripDate);

    @Query(value = "SELECT MAX (tripDate) AS tripDate FROM RouteSheet")
    LocalDate findMaxDate();

    List<RouteSheet> findAllByTripDateIsNotNull();

    RouteSheet findRouteSheetByWaybillNumber(Integer waybillNumber);

    RouteSheet findByWaybillNumber(Integer waybillNumber);

    @Query(value = "SELECT MIN(waybillNumber) FROM RouteSheet")
    Integer findMinWaybillNumber();

    @Query(value = "SELECT MAX(waybillNumber) FROM RouteSheet ")
    Integer findMaxNumberOfWaybills();

}
