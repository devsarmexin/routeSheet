package com.sepfort.sheet.service.impl;

import com.sepfort.sheet.domain.Route;
import com.sepfort.sheet.domain.RouteSheet;
import com.sepfort.sheet.dto.RouteSheetDto;
import com.sepfort.sheet.repo.RouteSheetRepo;
import com.sepfort.sheet.service.RouteSheetService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RouteSheetServiceImpl implements RouteSheetService {
    private boolean pointFlag = true;
    private String newPoint;
    private Integer sumDistance = 0;
    private List<Route> routeList = new ArrayList<>();
    private LocalDate localDateFromAddRoutes; // дата первого ПЛ
    private LocalDate dateForAddRoutes; //Дата для занесения маршрутов в ПЛ

    @Autowired
    private RouteSheetRepo routeSheetRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Map<String, String> editingRoutesToRoutSheet(Short distance, String address2, String flag, Model model) {
        return null;
    }

    @Override  // Добавление в БД первого ПЛ
    public Map<String, String> addingFirstRouteSheetToDatabase(RouteSheetDto routeSheetDto) {
        localDateFromAddRoutes = LocalDate.parse(routeSheetDto.getTripDate());
        Double saving = routeSheetDto.getConsumptionFact() - routeSheetDto.getConsumptionNorm();
        routeSheetRepo.save(new RouteSheet(localDateFromAddRoutes, routeSheetDto.getWaybillNumber(), routeSheetDto.getFuelStart(), routeSheetDto.getFuelFinish(), routeSheetDto.getMileageStart(), routeSheetDto.getMileageFinish(), routeSheetDto.getFueling(), routeSheetDto.getConsumptionNorm(), routeSheetDto.getConsumptionFact(), saving));
        Map<String, String> answerToMenu = new HashMap<>();
        answerToMenu.put("errorMessage", "Первый маршрутный лист заполен и помещён в базу данных.");
        return answerToMenu;
    }

    @Override  // Добавление в БД нового ПЛ
    public Map<String, String> addRouteSheetToDatabase(RouteSheetDto routeSheetDto) {
        if (routeSheetRepo.findByTripDate(LocalDate.parse(routeSheetDto.getTripDate())) != null) {
            Map<String, String> answerToMenu = new HashMap<>();
            answerToMenu.put("errorMessage", "На " + routeSheetDto.getTripDate() + " есть путевой лист");
            return answerToMenu;
        }

        RouteSheet lastRouteSheet = routeSheetRepo.findById(routeSheetRepo.findMaxId()).get();
        RouteSheet routeSheet = calculationNewWaybill(routeSheetDto, lastRouteSheet);
        routeSheetRepo.save(routeSheet);
        Map<String, String> answerToMenu = new HashMap<>();
        answerToMenu.put("errorMessage", "Новый маршрутный лист на " + routeSheetDto.getTripDate() + " создан.");
        return answerToMenu;
    }

    @Override
    public boolean thereAreRoutes(String date) {
        dateForAddRoutes = LocalDate.parse(date);
        return routeSheetRepo.findByTripDate(LocalDate.parse(date)).getRoutes() == null;
    }

//    @Override // Добавление и редактирование маршрутов
//    public  void addingRoutesToRoutSheet(String date) {
//        if (!routeSheetRepo.findByTripDate(LocalDate.parse(date)).getRoutes().isEmpty() && isEdit.equals("no")) {
//            model.addAttribute("errorMessage", "Нельзя добавить маршруты, они уже заполнены.");
//            return "menu";
//        }
//        if (isEdit.equals("yes")) {
//            routeSheetRepo.findByTripDate(LocalDate.parse(date)).setRoutes(new ArrayList<>());
//        }
//        localDateFromAddRoutes = LocalDate.parse(date);
//        model.addAttribute("firstPoint", "Маршала Говорова");
//        return "redirect:/";
//    }

    @Override  // Вывод информации
    public List<RouteSheet> generalInformation() {
        if (IterableUtils.size(routeSheetRepo.findAll()) == 0) {
            return null;
        }
        List<RouteSheet> routeSheetList = new ArrayList<>();
        CollectionUtils.addAll(routeSheetList, routeSheetRepo.findAll().iterator());
        return routeSheetList;
    }

    @Override // Вывод маршрутного листа на экран по дате
    public String output(String date, Model model) {
        LocalDate data = LocalDate.parse(date);
        RouteSheet routeSheet = routeSheetRepo.findByTripDate(data);
        if (routeSheet == null) {
            model.addAttribute("errorMessage", "На " + date + " маршрутного листа нет");
            return "menu";
        }
        model.addAttribute("routeSheet", routeSheet);
        model.addAttribute("numberRouteSheet", date);
        return "output";
    }

    @Override // Запрос : пуста ли база RouteSheet
    public boolean queryDatabaseIsEmpty() {
        return IterableUtils.size(routeSheetRepo.findAll()) == 0;
    }

//    @Override  // Добавление маршрутов
//    public  Map<String, String> editingRoutesToRoutSheet(Short distance, String address2, String flag, Model model) {
//        String firstPoint;
//        if (pointFlag) {
//            firstPoint = "Маршала Говорова";
//            pointFlag = false;
//        } else {
//            firstPoint = newPoint;
//        }
//        newPoint = address2;
//        Route route = new Route(firstPoint, address2, distance);
//        sumDistance += distance;
//        routeList.add(route);
//        model.addAttribute("firstPoint", newPoint);
//        if (flag.equals("no")) {
//            localDateFromAddRoutes = routeSheetRepo.findMaxDate();
//            RouteSheet routeSheet = routeSheetRepo.findByTripDate(localDateFromAddRoutes);
//        //    RouteSheet routeSheetForSave = calculationNewWaybillWithRoutes(routeSheet.getTripDate(), routeSheet.getWaybillNumber(), routeSheet, routeSheet.getFueling(), sumDistance, routeList);
//                   RouteSheet routeSheetForSave = calculationNewWaybillWithRoutes();
//            routeSheetRepo.deleteById(routeSheet.getId());
//            routeSheetRepo.save(routeSheetForSave);
//
//            routeList = new ArrayList<>();
//            localDateFromAddRoutes = null;
//            sumDistance = 0;
//            pointFlag = true;
//            routeList = new ArrayList<>();
//            model.addAttribute("errorMessage", "Закончили ввод маршрутов");
//            Map<String, String> answerToMenu = new HashMap<>();
//            answerToMenu.put("errorMessage", "Закончили ввод маршрутов");
//            return answerToMenu;
//        }
//    //    return "addingRoutes";
//        return null;
//    }

    @Override
    public String editingRoutesToRoutSheet2(Short distance, String address2) {
        String firstPoint;
        if (pointFlag) {
            firstPoint = "Маршала Говорова";
            pointFlag = false;
        } else {
            firstPoint = newPoint;
        }
        newPoint = address2;
        Route route = new Route(firstPoint, address2, distance);
        sumDistance += distance;
        routeList.add(route);
        return newPoint;
    }

    @Override
    public void editingRoutesToRoutSheetEnd(Short distance, String address2) {
        RouteSheet routeSheet = routeSheetRepo.findByTripDate(dateForAddRoutes);
        if (routeList.isEmpty()) {
            Route route = new Route("Маршала Говорова", address2, distance);
            sumDistance += distance;
            routeList.add(route);
        }

        Route route = new Route(newPoint, address2, distance);
        sumDistance += distance;
        routeList.add(route);

        RouteSheet routeSheetForSave = calculationNewWaybillWithRoutes(routeSheet);
        routeSheetRepo.save(routeSheetForSave);

        routeList = new ArrayList<>();
        sumDistance = 0;
        pointFlag = true;
        routeList = new ArrayList<>();
    }

    private RouteSheet calculationNewWaybillWithRoutes(RouteSheet routeSheet) {
        routeSheet.setRoutes(routeList);
        routeSheet.setDistance(sumDistance);
        return routeSheet;
    }

    // ПОДСЧЕТ значений полей на новый путевой лист
    private RouteSheet calculationNewWaybill(RouteSheetDto routeSheetDto, RouteSheet lastRouteSheet) {
        Integer WaybillNumber = lastRouteSheet.getWaybillNumber() + 1;
        Double fuelStart = lastRouteSheet.getFuelFinish();
        Integer mileageStart = lastRouteSheet.getMileageFinish();
        Short fueling = routeSheetDto.getFueling();
        Integer mileageFinish = routeSheetDto.getMileageFinish();
        return new RouteSheet(LocalDate.parse(routeSheetDto.getTripDate()), WaybillNumber, fuelStart, mileageStart, mileageFinish, fueling);
    }

}


//        Double saving = Precision.round(consumptionNorm - consumptionFact, 3);

//     Double consumptionNorm = Precision.round(12D * routeSheetDto.getdistance() / 100D, 2);
// Переделать, что бы проьежуток выбирать от даты до даты
//        LocalDate winterStartDay = LocalDate.parse("2019-11-01");
//        LocalDate winterEndDay = LocalDate.parse("2020-06-30");
//        if (date.isAfter(winterStartDay) && date.isBefore(winterEndDay)) {
//            consumptionNorm = Precision.round(consumptionNorm * 1.1, 2);
//        }
//        Double consumptionFact = 0D;
//        if (consumptionNorm < 10) {
//            consumptionFact = Double.parseDouble(String.valueOf(consumptionNorm).substring(0, 3));
//        }
//        if (consumptionNorm >= 10 && consumptionNorm < 100) {
//            consumptionFact = Double.parseDouble(String.valueOf(consumptionNorm).substring(0, 4));
//        }
//        if (consumptionNorm >= 100) {
//            consumptionFact = Double.parseDouble(String.valueOf(consumptionNorm).substring(0, 5));
//        }
