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
