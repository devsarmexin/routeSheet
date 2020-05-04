package com.sepfort.sheet.service;

import com.sepfort.sheet.domain.Addresses;
import com.sepfort.sheet.domain.RouteSheet;
import com.sepfort.sheet.domain.User;
import org.springframework.ui.Model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface RouteSheetService {
    void savePrimaryInput(
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
    );

    void addRouteSheet(
            User user,
            String dateToString,
            Long number,
            RouteSheet lastRouteShee,
            Long fueling,
            Long distance,
            List<Addresses> addressesList
    );

    String addRoute(
            Long distance,
            String address1,
            String address2,
            String flag
    );

    String generalInformation(Model model);

    String goToAddRoute(Long fuel, String data);

    String edit(String date);

    String addRouteSheet(
            User user,
            String address1,
            String address2,
            Long distance,
            String flag,
            Model model
    );

    String primaryInput(
            String dateToString,
            Long number,
            Double fuelStart,
            Double fuelFinish,
            Long mileageStart,
            Long mileageFinish,
            Long fueling,
            Double consumptionNorm,
            Double consumptionFact,
            Model model
    );

    String output(String date, Model model);

    String fillingOutTheWaybill();

    String createWaybill(String data) throws IOException;
}
