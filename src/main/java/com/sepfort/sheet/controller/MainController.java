package com.sepfort.sheet.controller;

import com.sepfort.sheet.domain.Addresses;
import com.sepfort.sheet.domain.RouteSheet;
import com.sepfort.sheet.domain.User;
import com.sepfort.sheet.repo.RouteSheetRepo;
import com.sepfort.sheet.service.impl.RouteSheetServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Controller
public class MainController {
    @Autowired
    private RouteSheetRepo routeSheetRepo;
    @Autowired
    private RouteSheetServiceImpl routeSheetService;

    private List<Addresses> addressesList = new ArrayList<>();
    private boolean isAddRouteSheet = true;
    private RouteSheet lastRouteSheet;
    private Long sumDistance = 0L;
    private LocalDate localDateFromAddRoutes;
    private Long fueling;
    private String dataNow;

    // Вход
    @GetMapping
    public String menuEntry() {
        System.out.println(">>> ВХОД");
        return "menu";
    }

    // Вывод информации
    @GetMapping("/information")
    public String generalInformation(Model model) {
        Long idMax = routeSheetRepo.findMaxId();
        if (idMax == null) {
            model.addAttribute("lastNumber", "База данных пуста");
            return "information";
        }
        RouteSheet routeSheet = routeSheetRepo.findById(idMax).get();
        LocalDate lastNumber = routeSheet.getData();
        model.addAttribute("lastNumber", lastNumber);
        return "information";
    }

    // Ввод заправки
    @GetMapping("/filling")
    public String fillingOutTheWaybill() {
        // Проверка на присутствие в БД хоть одного путевого листа
        if (IterableUtils.size(routeSheetRepo.findAll()) == 0) {
            return "first";
        }
        return "fueling";
    }

    // Ввод новых мапшрутов
    @GetMapping("/fueling")
    public String goToAddRoute(@RequestParam Long fuel, @RequestParam String data) {
        fueling = fuel;
        dataNow = data;
        return "filling";
    }

    // Получаем дату (из date.ftlh) и идём заполнять маршруты
    @GetMapping("/edit")
    public String edit(@RequestParam String date, Model model) {
        System.out.println(">>> edit");
        localDateFromAddRoutes = LocalDate.parse(date);
        model.addAttribute("path", "edit2");
        return "addingRoutes";
    }

    // Приходим из addingRoutes.ftlh в цикле заполняем все маршруты
    @GetMapping("/edit2")
    public String edit2(
            @RequestParam Long distance,
            @RequestParam String address1,
            @RequestParam String address2,
            @RequestParam String flag,
            Model model
    ) {
        System.out.println(">>>  edit2");
        Addresses addresses = new Addresses(address1, address2, distance);
        System.out.println(">>> " + sumDistance);
        sumDistance = sumDistance + distance;
        addressesList.add(addresses);
        if (flag.equals("no")) {
            RouteSheet routeSheet = routeSheetRepo.findByData(localDateFromAddRoutes);
            System.out.println(">>> лист с датой " + routeSheet.getData());
            System.out.println(" Длина списка маршрутов " + addressesList.size());
            routeSheet.setDistance(sumDistance);
            routeSheet.setAddress(addressesList);
            Long id = routeSheet.getId();
            routeSheetRepo.deleteById(id);
            routeSheetRepo.save(routeSheet);
            addressesList = new ArrayList<>();
            localDateFromAddRoutes = null;
            sumDistance = 0L;
            return "menu";
        }
        return "addingRoutes";
    }

    // Запрос даты для добавления маршрутов (далее /edit)
    @GetMapping("/date")
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

    // Добавление путевого листа
    @PostMapping("/add")
    public String addRouteSheet(
            @AuthenticationPrincipal User user,
            @RequestParam String address1,
            @RequestParam String address2,
            @RequestParam Long distance,
            @RequestParam String flag,
            Model model) {
        System.out.println(">>> Начали процесс добавления новых маршрутов");
        sumDistance = sumDistance + distance;
        //Проверка на существование уже путевоо листа на эту дату
        var localDate = LocalDate.parse(dataNow);
        for (RouteSheet routeSheet : routeSheetRepo.findAll()) {
            if (routeSheet.getData() == localDate) {
                System.out.println(">>> В итераторею Проверка на совпадение даты при вводе маршорутного листа");
                Long idMax = routeSheetRepo.findMaxId();
                lastRouteSheet = routeSheetRepo.findById(idMax).get();
                model.addAttribute("lastRouteSheet", lastRouteSheet);
                model.addAttribute("isDate", "Маршрктный лист на эту дату уже существует. Данные не записаны.");
                return "main";
            }
        }
        if (isAddRouteSheet) {
            System.out.println(">>> Устовка id предыдущего путевого листа");
            lastRouteSheet = routeSheetRepo.findById(routeSheetRepo.findMaxId()).get();
            isAddRouteSheet = true;
        }
        System.out.println(">>> Добавление путевого листа");
        System.out.println(">>> Дата оформляемого путевого листа : " + dataNow);
        System.out.println(">>> Добавляется ещё один маршрут flag = " + flag);
        if (flag.equals("yes")) {
            Addresses addressesForList = new Addresses(address1, address2, distance);
            addressesList.add(addressesForList);
            System.out.println(">>> Добавили ещё один маршрут. Длина addressList = " + addressesList.size());
        } else {
            isAddRouteSheet = true;
            Addresses addressesForList = new Addresses(address1, address2, distance);
            addressesList.add(addressesForList);
            System.out.println(">>> Добавили последний маршрут. Длина addressList = " + addressesList.size());
            System.out.println(">>> из бд " + routeSheetRepo.findDataMax());
            LocalDate localDate1 = routeSheetRepo.findDataMax();
            System.out.println(">>> " + routeSheetRepo.findByData(localDate1));
            Long number = routeSheetRepo.findByData(localDate1).getNumber() + 1L;
            System.out.println(">>> Сдед. номер = " + number);
            routeSheetService.addRouteSheet(user, dataNow, number, lastRouteSheet, fueling, sumDistance, addressesList);
            sumDistance = 0L;
            addressesList = new ArrayList<>();
            fueling = 0L;
            dataNow = "";
            System.out.println(">>> Переход на страницу statistics");
            return "redirect:/";
        }
        System.out.println(">>> ВЫХОД");
        return "filling";
    }

//    @GetMapping("/inputroutesheet")
//    public String inputRouteSheet(Model model) {
//        return "inputroutesheet";
//    }

    @GetMapping("/output")  //Вывод маршрутного листа по дате
    public String output(@RequestParam String date, Model model) {
        LocalDate data = LocalDate.parse(date);
        RouteSheet routeSheet = routeSheetRepo.findByData(data);
        if (routeSheet == null) {
            return "menu";
        }
        model.addAttribute("routeSheet", routeSheet);
        return "output";
    }

//    @GetMapping("/error")
//    public String error(Model model) {
//        return "error";
//    }

    //Вывод главного экрана со статистикой и действиями
    @GetMapping("/statistics")
    public String statistics() {
        return "statistics";
    }

    //Первичное заполнение первого путевого листа
    @Transactional
    @PostMapping("/primary_input")
    public String PrimaryInput(
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
        localDateFromAddRoutes = LocalDate.parse(dateToString);
        Double saving = consumptionFact - consumptionNorm;
        routeSheetService.savePrimaryInput(dateToString, number, fuelStart, fuelFinish, mileageStart, mileageFinish,
                fueling, consumptionNorm, consumptionFact, saving
        );

        Long idMax = routeSheetRepo.findMaxId();
        RouteSheet lastRouteSheet = routeSheetRepo.findById(idMax).get();
        model.addAttribute("lastRouteSheet", lastRouteSheet);
        return "first";
    }
}


