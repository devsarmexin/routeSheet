package com.sepfort.sheet.service;

import com.sepfort.sheet.domain.RouteSheet;
import com.sepfort.sheet.dto.RouteSheetDto;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;

public interface RouteSheetService {

    /**
     * Adding the first waybill to the database.
     *
     * @param routeSheetDto routeSheetDto.
     * @return Operation Result Information.
     */
    Map<String, String> addingFirstRouteSheetToDatabase(RouteSheetDto routeSheetDto);

    /**
     * Adding a new waybill to the database.
     *
     * @param routeSheetDto routeSheetDto.
     * @param isEdit        Is the waybill editable.
     * @return Operation Result Information.
     */
    Map<String, String> addRouteSheetToDatabase(RouteSheetDto routeSheetDto, String isEdit);

    /**
     * Display information on all existing waybills.
     * @return List of waybills.
     */
    List<RouteSheet> generalInformation();

    /**
     * Display route sheet by date.
     * @param date Searched date.
     * @param model Representation.
     * @return Returns to the main menu if there is no waybill for the desired date or displays the contents of the waybill.
     */
    String output(String date, Model model);

    /**
     * Determines whether the base is empty or not.
     * @return Try or false.
     */
    boolean queryDatabaseIsEmpty();

    /**
     * Check for the existence of a waybill for the desired date.
     *
     * @param date Searched date.
     * @return Try or false.
     */
    boolean thereAreRoutes(String date);

    /**
     * We fill in all directions of the waybill.
     * @param distance Distance between waypoints on a waybill.
     * @param routeEndPointAddress route endpoint address.
     * @return We return to the main menu if there are no more routes, otherwise we go to enter a new waybill route.
     */
    String editingRoutesToRoutSheet(Short distance, String routeEndPointAddress);

    /**
     * Enter the last waybill route.
     * @param distance Distance between waypoints on a waybill.
     * @param routeEndPointAddress route endpoint address.
     */
    void editingRoutesToRoutSheetEnd(Short distance, String routeEndPointAddress);

    /**
     * Deleting a database.
     */
    void delete();

    /**
     * Database recalculation.
     */
    void databaseRecalculation();
}
