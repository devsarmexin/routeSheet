package com.sepfort.sheet.controller;

import com.sepfort.sheet.domain.RouteSheet;
import com.sepfort.sheet.dto.RouteSheetDto;
import com.sepfort.sheet.mapper.RouteSheetMapper;
import com.sepfort.sheet.service.RouteSheetService;
import com.sepfort.sheet.service.impl.CreateWaybillImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
public class MainController {
    @Autowired
    private RouteSheetService routeSheetService;
    @Autowired
    private CreateWaybillImpl createWaybillImpl;
    @Autowired
    private RouteSheetMapper routeSheetMapper;

    @GetMapping  // Вход //
    public String menuEntry(Model model) {
        model.addAttribute("errorMessage", "");
        return "menu";
    }

    @PostMapping("/primary_input") //Первичное заполнение первого путевого листа //
    public String primaryInput(@ModelAttribute RouteSheetDto routeSheetDto, Model model) {
        Map<String, String> answerToMenu = routeSheetService.addingFirstRouteSheetToDatabase(routeSheetDto);
        model.addAttribute("errorMessage", answerToMenu.get("errorMessage"));
        return "menu";
    }

    @GetMapping("/routeSheet") // Вывод приветственной информации //
    public String intro() {
        return "routeSheet";
    }

    @GetMapping("/information")  // Вывод информации  //
    public String generalInformation(Model model) {
        List<RouteSheet> routeSheetList  = routeSheetService.generalInformation();
        if (routeSheetList == null) {
            model.addAttribute("errorMessage", "База данных пуста");
            return "menu";
        }
        model.addAttribute("routeSheetList", routeSheetList);
        return "information";
    }

    @GetMapping("/filling")
    public String fillingOutTheWaybill(Model model) {
        if (routeSheetService.queryDatabaseIsEmpty()) { // Запрос на пустоту БД RouteSheet
            model.addAttribute("hello", "user");
            return "first";
        }
        return "fueling"; // Запрос данных (дата, запрвка) для добавления ПЛ
    }

    @GetMapping("/createNewRouteSheet") // Ввод нового путевого листа  //
    public String goToAddRoute(@ModelAttribute RouteSheetDto routeSheetDto, Model model) {
        Map<String, String> answerToMenu = routeSheetService.addRouteSheetToDatabase(routeSheetDto);
        model.addAttribute("errorMessage", answerToMenu.get("errorMessage"));
        return "menu";
    }

    @GetMapping("/editRoute") // Получаем дату (из date.ftlh) и идём заполнять маршруты
    public  String edit(@RequestParam String date, Model model) {
        boolean thereAreRoutes = routeSheetService.thereAreRoutes(date);
        if (thereAreRoutes) {
            model.addAttribute("errorMessage", "На " + date + " заполнены маршруты");
            return "menu";
        }
        model.addAttribute("firstPoint", "Маршала Говорова");
        return "addingRoutes";
    }

    @GetMapping("/editingRoutes") // Приходим из addingRoutes.ftlh в цикле заполняем все маршруты
    public String edit2(
            @RequestParam Short distance,
            @RequestParam String address2,
            @RequestParam String flag,
            Model model
    ) {
        if (flag.equals("yes")) {
            String firstPoint = routeSheetService.editingRoutesToRoutSheet2(distance, address2);
            model.addAttribute("firstPoint", firstPoint);
            return "addingRoutes";
        }
        routeSheetService.editingRoutesToRoutSheetEnd(distance, address2);
        model.addAttribute("errorMessage", "Маршруты добавллены");
        return "menu";
    }

    @GetMapping("/addingRoutes") // Запрос даты для добавления маршрутов (далее /edit)
    public String date() {
        return "date";
    }

    @GetMapping("/viewRouteSheetByDate") // Запрос даты для просмотра ПЛ
    public String dateForViewRouteSheetByDate() {
        return "dateForViewRouteSheetByDate";
    }

    @GetMapping("/formExcel") // Запрос даты для формирования ПЛ в Excel
    public String date3() {
        return "dataForCreateWaybillAdd";
    }

    @GetMapping("/output")  //Вывод маршрутного листа по дате
    public String output(@RequestParam String date, Model model) {
        return routeSheetService.output(date, model);
    }

    @GetMapping("/createWaybill")
    public String createWaybill(@RequestParam String data, Model model) throws IOException {
        return createWaybillImpl.createWaybill(data, model);
    }

    @GetMapping("/waybillEditing")
    public String waybillEditing() {
        return "fuelingForEdit";
    }
}


