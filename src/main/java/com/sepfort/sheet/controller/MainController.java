package com.sepfort.sheet.controller;

import com.sepfort.sheet.domain.User;
import com.sepfort.sheet.service.impl.RouteSheetServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

@Slf4j
@Controller
public class MainController {
    @Autowired
    private RouteSheetServiceImpl routeSheetService;

    @GetMapping  // Вход
    public String menuEntry() {
        System.out.println(">>> ВХОД");
        return "menu";
    }

    @GetMapping("/information")  // Вывод информации
    public String generalInformation(Model model) {
        return routeSheetService.generalInformation(model);
    }

    @GetMapping("/filling") // Ввод заправки
    public String fillingOutTheWaybill() {
        return routeSheetService.fillingOutTheWaybill();
    }

    @GetMapping("/fueling") // Ввод новых мапшрутов
    public String goToAddRoute(@RequestParam Long fuel, @RequestParam String data) {
        return routeSheetService.goToAddRoute(fuel, data);
    }

    @GetMapping("/edit") // Получаем дату (из date.ftlh) и идём заполнять маршруты
    public String edit(@RequestParam String date) {
        System.out.println(">>> edit");
        return routeSheetService.edit(date);
    }

    @GetMapping("/edit2") // Приходим из addingRoutes.ftlh в цикле заполняем все маршруты
    public String edit2(
            @RequestParam Long distance,
            @RequestParam String address1,
            @RequestParam String address2,
            @RequestParam String flag
    ) {
        return routeSheetService.addRoute(distance, address1, address2, flag);
    }

    @GetMapping("/date") // Запрос даты для добавления маршрутов (далее /edit)
    public String date() {
        return "date";
    }

    @GetMapping("/date2")
    public String date2() {
        return "date2";
    }

    @GetMapping("/date3")
    public String date3() {
        return "date3";
    }

    @PostMapping("/add")  // Добавление путевого листа
    public String addRouteSheet(
            @AuthenticationPrincipal User user,
            @RequestParam String address1,
            @RequestParam String address2,
            @RequestParam Long distance,
            @RequestParam String flag,
            Model model) {
        System.out.println(">>> Начали процесс добавления новых маршрутов");
        return routeSheetService.addRouteSheet(user, address1, address2, distance, flag, model);
    }

    @GetMapping("/output")  //Вывод маршрутного листа по дате
    public String output(@RequestParam String date, Model model) {
        return routeSheetService.output(date, model);
    }

    @GetMapping("/statistics") //Вывод главного экрана со статистикой и действиями
    public String statistics() {
        return "statistics";
    }

    @Transactional   //Первичное заполнение первого путевого листа
    @PostMapping("/primary_input")
    public String primaryInput(
            @RequestParam String dateToString,
            @RequestParam Long number,
            @RequestParam Double fuelStart,
            @RequestParam Double fuelFinish,
            @RequestParam Long mileageStart,
            @RequestParam Long mileageFinish,
            @RequestParam Long fueling,
            @RequestParam Double consumptionNorm,
            @RequestParam Double consumptionFact,
            Model model
    ) {
        return routeSheetService.primaryInput(dateToString, number, fuelStart, fuelFinish, mileageStart, mileageFinish, fueling, consumptionNorm, consumptionFact, model);
    }

    @GetMapping("/createWaybill")
    public String createWaybill(@RequestParam String data) throws IOException {
//        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
//        Sheet sheet = xssfWorkbook.createSheet("first");
//        Row row = sheet.createRow(0);
//        Cell cell = row.createCell(0);
//        File file = new File("C://exp", "new.xls");
//        FileOutputStream fos = new FileOutputStream("C:/exp.new.xls");
//        xssfWorkbook.write(fos);
//        fos.close();

        // прочитать данные из xlsx
//        FileInputStream fileIS = new FileInputStream(new File("C:/exp/tmp/new.xlsx"));
//        XSSFWorkbook workbook = new XSSFWorkbook (fileIS);
//        XSSFSheet xssfSheet = workbook.getSheetAt(0);
//        var result = xssfSheet.getRow(1).getCell(0);
//        System.out.println(">>> " + result);
//        return "menu";

        // записать данные в xlsx

        return routeSheetService.createWaybill(data);
    }
}


