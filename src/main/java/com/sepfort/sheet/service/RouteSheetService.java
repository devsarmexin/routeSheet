package com.sepfort.sheet.service;

import org.springframework.ui.Model;

public interface RouteSheetService {
    String addingFirstRouteSheetToDatabase(String dateToString, Long number, Double fuelStart, Double fuelFinish, Long mileageStart, Long mileageFinish, Long fueling, Double consumptionNorm, Double consumptionFact, Model model);

    String addRouteSheetToDatabase(Long fuel, String data, String isEdit, Model model);

    String addingRoutesToRoutSheet(String date, String isEdit, Model model);

    String editingRoutesToRoutSheet(Long distance, String address2, String flag, Model model);

    String generalInformation(Model model);

    String output(String date, Model model);
}
