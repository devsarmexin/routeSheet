package com.sepfort.sheet.service.impl;

import com.sepfort.sheet.domain.Addresses;
import com.sepfort.sheet.domain.RouteSheet;
import com.sepfort.sheet.domain.User;
import com.sepfort.sheet.repo.RouteSheetRepo;
import org.apache.commons.math3.util.Precision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class RouteSheetServiceImpl {
    @Autowired
    private RouteSheetRepo routeSheetRepo;

    // Добавление первого путевого листа
    public void savePrimaryInput(
            String dateToString,
            Long number,
            Double fuelStart,
            Double fuelFinish,
            Long mileageStart,
            Long mileageFinish,
            Long fueling,
            Double consumptionNorm,
            Double consumptionFact,
            Double saving
    ) {
        LocalDate date = LocalDate.parse(dateToString);
        RouteSheet routeSheet = new RouteSheet(
                date,
                number,
                fuelStart,
                fuelFinish,
                mileageStart,
                mileageFinish,
                fueling,
                consumptionNorm,
                consumptionFact,
                saving
        );
        routeSheetRepo.save(routeSheet);
    }

    public void addRouteSheet(
            User user,
            String dateToString,
            Long number,
            RouteSheet lastRouteShee,
            Long fueling,
            Long distance,
            List<Addresses> addressesList
    ) {
        //Расчёт данных путевого листа
        LocalDate date = LocalDate.parse(dateToString);
        System.out.println(">>> LocalDate = " + date);
        Double consumptionNorm = Precision.round(12D * distance / 100D, 2);
        System.out.println(">>>>>>> consumptionNorm = " + consumptionNorm);

        // Переделать, что бы проьежуток выбирать от даты до даты
        LocalDate winterStartDay = LocalDate.parse("2019-11-01");
        LocalDate winterEndDay = LocalDate.parse("2020-06-30");
        if (date.isAfter(winterStartDay) && date.isBefore(winterEndDay)) {
            consumptionNorm = Precision.round(consumptionNorm * 1.1, 2);
            System.out.println(">>> в зиме ");
        }
        System.out.println(">>> consumptionNorm = " + consumptionNorm);
        Double consumptionFact = 0D;
        if (consumptionNorm < 10) {
            consumptionFact = Double.parseDouble(String.valueOf(consumptionNorm).substring(0, 3));
        }
        if (consumptionNorm >= 10 && consumptionNorm < 100) {
            consumptionFact = Double.parseDouble(String.valueOf(consumptionNorm).substring(0, 4));
        }
        if (consumptionNorm >= 100) {
            consumptionFact = Double.parseDouble(String.valueOf(consumptionNorm).substring(0, 5));
        }
        System.out.println(">>> consumptionFact = " + consumptionFact);

        Double fuelStart = lastRouteShee.getFuelFinish();
        Double fuelFinish = Precision.round(fuelStart + fueling - consumptionFact, 2);
        Long mileageStart = lastRouteShee.getMileageFinish();
        Long mileageFinish = mileageStart + distance;
        Double saving = Precision.round(consumptionNorm - consumptionFact, 3);

        RouteSheet routeSheet = new RouteSheet(
                date,
                number,
                fuelStart,
                fuelFinish,
                mileageStart,
                mileageFinish,
                fueling,
                consumptionNorm,
                consumptionFact,
                saving,
                distance,
                addressesList
        );
        System.out.println(">>> Создали новый RouteSheet");

        RouteSheet routeSheetInDB = routeSheetRepo.save(routeSheet);
        System.out.println(">>> Положили RouteSheet в БД");
        //  System.out.println(">>> Проверка на количество маршрутов = " + routeSheetRepo.findById(routeSheetInDB.getId()).get().getAddress().size());
    }
}
