package com.sepfort.sheet.service;

import com.sepfort.sheet.domain.RouteSheet;
import com.sepfort.sheet.dto.RouteSheetDto;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;

public interface RouteSheetService {
    Map<String, String> addingFirstRouteSheetToDatabase(RouteSheetDto routeSheetDto);

    Map<String, String> addRouteSheetToDatabase(RouteSheetDto routeSheetDto, String isEdit);

    List<RouteSheet> generalInformation();

    String output(String date, Model model);

    boolean queryDatabaseIsEmpty();

    boolean thereAreRoutes(String date);

    String editingRoutesToRoutSheet2(Short distance, String address2);

    void editingRoutesToRoutSheetEnd(Short distance, String address2);

    void delete();
}
