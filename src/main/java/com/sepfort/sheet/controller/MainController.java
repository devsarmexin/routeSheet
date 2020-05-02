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
    private Addresses addressesForList = new Addresses();
    private String filledDate;
    private boolean isAddRouteSheet = true;
    private RouteSheet lastRouteSheet;
    private Long sumDistance = 0L;
    private LocalDate localDateFromAddRoutes;

    // Вход
    @GetMapping
    public String menuEntry() {
        System.out.println(">>> ВХОД");
        return "menu";
    }

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

    @GetMapping("/filling")
    public String fillingOutTheWaybill() {
        if (IterableUtils.size(routeSheetRepo.findAll()) == 0) {
            return "first";
        }

        return "filling";
    }

    @GetMapping("/edit")
    public String edit(@RequestParam String date) {
        System.out.println(">>> edit");
        localDateFromAddRoutes = LocalDate.parse(date);
        return "addingRoutes";
    }

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
    @PostMapping("add")
    public String addRouteSheet(
            @AuthenticationPrincipal User user,
            @RequestParam String dateToString,
            @RequestParam Long fueling,
            @RequestParam String address1,
            @RequestParam String address2,
            @RequestParam Long distance,
            @RequestParam String flag,
            Model model) {
        sumDistance = sumDistance + distance;

        //Проверка на существование уже путевоо листа на эту дату
        LocalDate localDate = LocalDate.parse(dateToString);
        Iterable<RouteSheet> routeSheetList = routeSheetRepo.findAll();
        Iterator<RouteSheet> iterator = routeSheetList.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getData() == localDate) {
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
            Long idMax = routeSheetRepo.findMaxId();
            lastRouteSheet = routeSheetRepo.findById(idMax).get();
            isAddRouteSheet = true;
        }
        System.out.println(">>> Добавление путевого листа");
        System.out.println(">>> Дата оформляемого путевого листа : " + dateToString);
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
            //id путевого листа dateToString
            //сам предыдущего путевого листа lastRouteSheet
            routeSheetService.addRouteSheet(user, dateToString, number, lastRouteSheet, fueling, sumDistance, addressesList);
            sumDistance = 0L;
            addressesList = new ArrayList<>();

            System.out.println(">>> Переход на страницу statistics");
            return "redirect:/";
        }

        model.addAttribute("lastRouteSheet", lastRouteSheet);
        return "filing";
    }

    @GetMapping("/inputroutesheet")
    public String inputRouteSheet(Model model) {
        return "inputroutesheet";
    }

    @GetMapping("/output")  //Вывод маршрутного листа по дате
    public String output(@RequestParam String date, Model model) {
        LocalDate data = LocalDate.parse(date);
        RouteSheet routeSheet = routeSheetRepo.findByData(data);
        model.addAttribute("routeSheet", routeSheet);
        return "output";
    }

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

//    @GetMapping("/addingRoutes")
//    public String addingRoutes(
////            @RequestParam Long distance,
////            @RequestParam String address1,
////            @RequestParam String address2,
//            Model model
//    ) {
//        System.out.println(">>>  " + distance + " " + address1 + " " + address2);
//        return "addingRoutes";
//    }

}


