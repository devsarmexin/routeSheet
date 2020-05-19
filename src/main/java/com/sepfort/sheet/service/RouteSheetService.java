package com.sepfort.sheet.service;

import org.springframework.ui.Model;

public interface RouteSheetService {
    String addingFirstRouteSheetToDatabase(String dateToString, Short number, Double fuelStart, Double fuelFinish, Integer mileageStart, Integer mileageFinish, Short fueling, Double consumptionNorm, Double consumptionFact, Model model);

    String addRouteSheetToDatabase(Short fuel, String data, String isEdit, Model model);

    String addingRoutesToRoutSheet(String date, String isEdit, Model model);

    String editingRoutesToRoutSheet(Short distance, String address2, String flag, Model model);

    String generalInformation(Model model);

    String output(String date, Model model);
}
