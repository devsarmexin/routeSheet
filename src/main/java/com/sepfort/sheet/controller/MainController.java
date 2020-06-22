package com.sepfort.sheet.controller;

import com.sepfort.sheet.domain.RouteSheet;
import com.sepfort.sheet.dto.RouteSheetDto;
import com.sepfort.sheet.mapper.RouteSheetMapper;
import com.sepfort.sheet.service.RouteSheetService;
import com.sepfort.sheet.service.impl.CreateWaybillImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
public class MainController {
    private RouteSheetService routeSheetService;
    private CreateWaybillImpl createWaybillImpl;
    private RouteSheetMapper routeSheetMapper;

    @Autowired
    public MainController(RouteSheetService routeSheetService, CreateWaybillImpl createWaybill, RouteSheetMapper routeSheetMapper) {
        this.routeSheetService = routeSheetService;
        this.createWaybillImpl = createWaybill;
        this.routeSheetMapper = routeSheetMapper;
    }

    /**
     * Entrance to the main menu.
     * @param model Representation.
     * @return Return to main menu.
     */
    @GetMapping
    public String menuEntry(Model model) {
        model.addAttribute("errorMessage", "");
        return "menu";
    }

    /**
     * Initial filling of the first waybill.
     * @param routeSheetDto routeSheetDto.
     * @param model Representation.
     * @return Return to main menu.
     */
    @PostMapping("/primary_input")
    public String primaryInput(@ModelAttribute RouteSheetDto routeSheetDto, Model model) {
        Map<String, String> answerToMenu = routeSheetService.addingFirstRouteSheetToDatabase(routeSheetDto);
        model.addAttribute("errorMessage", answerToMenu.get("errorMessage"));
        return "menu";
    }

    /**
     * Conclusion of welcome information.
     * @return Message.
     */
    @GetMapping("/routeSheet")
    public String intro() {
        return "routeSheet";
    }

    /**
     * @param model Representation.
     * @return Return to main menu or Information output.
     */
    @GetMapping("/information")
    public String generalInformation(Model model) {
        List<RouteSheet> routeSheetList = routeSheetService.generalInformation();
        if (routeSheetList == null) {
            model.addAttribute("errorMessage", "База данных пуста");
            return "menu";
        }
        model.addAttribute("routeSheetList", routeSheetList);
        return "information";
    }

    /**
     * Data request (date, refueling) for adding a waybill.
     * @param model Representation.
     * @return Transition.
     */
    @GetMapping("/filling")
    public String fillingOutTheWaybill(Model model) {
        if (routeSheetService.queryDatabaseIsEmpty()) {
            model.addAttribute("hello", "user");
            return "first";
        }
        return "fueling";
    }

    /**
     * Enter a new waybill.
     * @param routeSheetDto routeSheetDto.
     * @param isEdit Is editable.
     * @param model Representation.
     * @return Return to main menu.
     */
    @GetMapping("/createNewRouteSheet")
    public String goToAddRoute(@ModelAttribute RouteSheetDto routeSheetDto, @RequestParam(defaultValue = "no") String isEdit, Model model) {
        Map<String, String> answerToMenu = routeSheetService.addRouteSheetToDatabase(routeSheetDto, isEdit);
        model.addAttribute("errorMessage", answerToMenu.get("errorMessage"));
        return "menu";
    }

    /**
     * Having received the date and having completed the checks, we proceed to filling out the routes.
     * @param date Date.
     * @param model Representation.
     * @return Go to filling out the directions of the waybill or to the main menu.
     */
    @GetMapping("/editRoute")
    public String edit(@RequestParam String date, Model model) {
        boolean isDatabaseIsEmpty = routeSheetService.queryDatabaseIsEmpty();
        if (isDatabaseIsEmpty) {
            model.addAttribute("errorMessage", " БД пуста");
            return "menu";
        }
        boolean thereAreRoutes = routeSheetService.thereAreRoutes(date);
        if (thereAreRoutes) {
            model.addAttribute("errorMessage", "На " + date + " заполнены маршруты");
            return "menu";
        }
        model.addAttribute("firstPoint", "Маршала Говорова");
        return "addingRoutes";
    }

    /**
     * We fill in all directions of the waybill.
     * @param distance Distance between waypoints on a waybill.
     * @param routeEndPointAddress route endpoint address.
     * @param isThereFollowingRoute Flag for the next route in the waybill
     * @param model Representation.
     * @return We return to the main menu if there are no more routes, otherwise we go to enter a new waybill route.
     */
    @GetMapping("/editingRoutes")
    public String edit2(
            @RequestParam Short distance,
            @RequestParam String routeEndPointAddress,
            @RequestParam String isThereFollowingRoute,
            Model model
    ) {
        if (isThereFollowingRoute.equals("yes")) {
            String firstPoint = routeSheetService.editingRoutesToRoutSheet(distance, routeEndPointAddress);
            model.addAttribute("firstPoint", firstPoint);
            return "addingRoutes";
        }
        routeSheetService.editingRoutesToRoutSheetEnd(distance, routeEndPointAddress);
        model.addAttribute("errorMessage", "Маршруты добавллены");
        return "menu";
    }

    /**
     * Request a waybill date for adding routes.
     * @return Go to date entry.
     */
    @GetMapping("/addingRoutes")
    public String date() {
        return "date";
    }

    /**
     *  Request a waybill date for viewing.
     * @return Transition.
     */
    @GetMapping("/viewRouteSheetByDate") // Запрос даты для просмотра ПЛ
    public String dateForViewRouteSheetByDate() {
        return "dateForViewRouteSheetByDate";
    }

    /**
     * Requesting the date of the waybill for generating it in Excel.
     * @return Transition.
     */
    @GetMapping("/formExcel")
    public String date3() {
        return "dataForCreateWaybillAdd";
    }

    /**
     * Display route sheet by date.
     * @param date Date.
     * @param model Representation.
     * @return Transition.
     */
    @GetMapping("/output")
    public String output(@RequestParam String date, Model model) {
        return routeSheetService.output(date, model);
    }

    /**
     * Creating a route sheet in Excel.
     * @param data Date.
     * @param model Representation.
     * @return Route sheet in Excel.
     * @throws IOException
     */
    @GetMapping("/createWaybill")
    public String createWaybill(@RequestParam String data, Model model) throws IOException {
        return createWaybillImpl.createWaybill(data, model);
    }

    /**
     * Navigation to enter the parameters of a new waybill.
     * @return Transition.
     */
    @GetMapping("/waybillEditing")
    public String waybillEditing() {
        return "fuelingForEdit";
    }

    /**
     * Request to delete a database.
     * @return Transition.
     */
    @GetMapping("/deleteDataBase")
    public String deleteDataBase() {
        return "deleteDataBase";
    }

    /**
     * Request to delete a database.
     * @param isDelete Approval to delete the database.
     * @param model Representation.
     * @return Return to main menu.
     */
    @GetMapping("/delete")
    public String delete(@RequestParam String isDelete, Model model) {
        if (isDelete.equals("yes")) {
            routeSheetService.delete();
            model.addAttribute("errorMessage", "Очистили БД");
            return "menu";
        }

        model.addAttribute("errorMessage", "Не стали очищать БД");
        return "menu";
    }

    // Доделать
    /**
     *
     * @param string
     * @return
     */
    @GetMapping("/download")
    public ResponseEntity<String> download(@RequestBody String string) {
        String responceObject = "Hello";
        return new ResponseEntity<String>(responceObject, HttpStatus.OK);
    }
}


