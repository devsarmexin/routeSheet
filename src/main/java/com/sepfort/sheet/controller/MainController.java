package com.sepfort.sheet.controller;

import com.sepfort.sheet.repo.RouteSheetRepo;
import com.sepfort.sheet.service.RouteSheetService;
import com.sepfort.sheet.service.impl.CreateWaybillImpl;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
public class MainController {
    @Autowired
    private RouteSheetService routeSheetService;
    @Autowired
    private CreateWaybillImpl createWaybillImpl;
    @Autowired
    private RouteSheetRepo routeSheetRepo;

    @GetMapping  // Вход
    public String menuEntry(Model model) {
        model.addAttribute("errorMessage", "");
        return "menu";
    }

    @PostMapping("/primary_input") //Первичное заполнение первого путевого листа
    public String primaryInput(
            @RequestParam String dateToString,
            @RequestParam Short waybillNumber,
            @RequestParam Double fuelStart,
            @RequestParam Double fuelFinish,
            @RequestParam Integer mileageStart,
            @RequestParam Integer mileageFinish,
            @RequestParam Short fueling,
            @RequestParam Double consumptionNorm,
            @RequestParam Double consumptionFact,
            Model model
    ) {
        return routeSheetService.addingFirstRouteSheetToDatabase(dateToString, waybillNumber, fuelStart, fuelFinish, mileageStart, mileageFinish, fueling, consumptionNorm, consumptionFact, model);
    }

    @GetMapping("/routeSheet") // Вывод приветственной информации
    public String intro() {
        return "routeSheet";
    }

    @GetMapping("/information")  // Вывод информации
    public String generalInformation(Model model) {
        return routeSheetService.generalInformation(model);
    }

    @GetMapping("/filling")
    public String fillingOutTheWaybill() {
        if (IterableUtils.size(routeSheetRepo.findAll()) == 0) {
            return "first";
        }
        return "fueling"; // Запрос данных (дата, запрвка) для добавления ПЛ
    }
    
    @GetMapping("/createNewRouteSheet") // Ввод нового путевого листа
    public String goToAddRoute(@RequestParam Short fuel, @RequestParam String data, @RequestParam(defaultValue = "no") String isEdit, Model model) {
        return routeSheetService.addRouteSheetToDatabase(fuel, data, isEdit, model);
    }

    @GetMapping("/editRoute") // Получаем дату (из date.ftlh) и идём заполнять маршруты
    public String edit(@RequestParam String date, @RequestParam(defaultValue = "no") String isEdit, Model model) {
        return routeSheetService.addingRoutesToRoutSheet(date, isEdit, model);
    }

    @GetMapping("/editingRoutes") // Приходим из addingRoutes.ftlh в цикле заполняем все маршруты
    public String edit2(
            @RequestParam Short distance,
            @RequestParam String address2,
            @RequestParam String flag,
            Model model
    ) {
        return routeSheetService.editingRoutesToRoutSheet(distance, address2, flag, model);
    }

    @GetMapping("/addingRoutes") // Запрос даты для добавления маршрутов (далее /edit)
    public String date() {
        return "date";
    }

    @GetMapping("/viewRouteSheetByDate") // Запрос даты для просмотра ПЛ
    public String date2() {
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


