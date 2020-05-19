package com.sepfort.sheet.service.impl;

import com.sepfort.sheet.domain.Route;
import com.sepfort.sheet.domain.RouteSheet;
import com.sepfort.sheet.repo.RouteSheetRepo;
import com.sepfort.sheet.service.RouteSheetService;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.math3.util.Precision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class RouteSheetServiceImpl implements RouteSheetService {
    private boolean pointFlag = true;
    private String newPoint;
    private Integer sumDistance = 0;
    private List<Route> routeList = new ArrayList<>();
    private LocalDate localDateFromAddRoutes; // дата первого ПЛ

    @Autowired
    private RouteSheetRepo routeSheetRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override  // Добавление в БД первого ПЛ
    public String addingFirstRouteSheetToDatabase(String dateToString, Short number, Double fuelStart, Double fuelFinish, Integer mileageStart, Integer mileageFinish, Short fueling, Double consumptionNorm, Double consumptionFact, Model model) {
        localDateFromAddRoutes = LocalDate.parse(dateToString);
        Double saving = consumptionFact - consumptionNorm;
        routeSheetRepo.save(new RouteSheet(localDateFromAddRoutes, number, fuelStart, fuelFinish, mileageStart, mileageFinish, fueling, consumptionNorm, consumptionFact, saving));
        RouteSheet lastRouteSheet = routeSheetRepo.findById(routeSheetRepo.findMaxId()).get();
        model.addAttribute("lastRouteSheet", lastRouteSheet);
        model.addAttribute("errorMessage", "Первый маршрутный лист заполен и помещён в базу данных.");
        return "menu";
    }

    @Override  // Добавление в БД нового ПЛ
    public String addRouteSheetToDatabase(Short fuel, String data, String isEdit, Model model) {
        if (routeSheetRepo.findByTripDate(LocalDate.parse(data)) != null && isEdit.equals("no")) {
            model.addAttribute("errorMessage", "На " + data + " есть путевой лист");
            return "menu";
        }
        Short number = 0;
        if (isEdit.equals("yes")) {
            number = routeSheetRepo.findByTripDate(routeSheetRepo.findMaxDate()).getWaybillNumber();
            Integer id = routeSheetRepo.findByTripDate(LocalDate.parse(data)).getId();
            routeSheetRepo.deleteById(id);
        } else {
            number = (routeSheetRepo.findByTripDate(routeSheetRepo.findMaxDate())).getWaybillNumber();
            number++;
        }
        RouteSheet lastRouteSheet = routeSheetRepo.findById(routeSheetRepo.findMaxId()).get();
        Integer distance = 0;
        RouteSheet routeSheet = addNewRouteSheet(LocalDate.parse(data), number, lastRouteSheet, fuel, distance, null);
        routeSheetRepo.save(routeSheet);
        model.addAttribute("errorMessage", "Новый маршрутный лист на " + data + " создан.");
        return "menu";
    }

    @Override // Добавление и редактирование маршрутов
    public String addingRoutesToRoutSheet(String date, String isEdit, Model model) {
        if (routeSheetRepo.findByTripDate(LocalDate.parse(date)) == null) {
            model.addAttribute("errorMessage", "На " + date + " нет маршрутного листа!");
            return "menu";
        }
        if (!routeSheetRepo.findByTripDate(LocalDate.parse(date)).getRoutes().isEmpty() && isEdit.equals("no")) {
            model.addAttribute("errorMessage", "Нельзя добавить маршруты, они уже заполнены.");
            return "menu";
        }
        if (isEdit.equals("yes")) {
            routeSheetRepo.findByTripDate(LocalDate.parse(date)).setRoutes(new ArrayList<>());
        }
        localDateFromAddRoutes = LocalDate.parse(date);
        model.addAttribute("firstPoint", "Маршала Говорова");
        return "addingRoutes";
    }

    @Override  // Вывод информации
    public String generalInformation(Model model) {
        if (IterableUtils.size(routeSheetRepo.findAll()) == 0) {
            model.addAttribute("errorMessage", "База данных пуста");
            return "menu";
        }
        RouteSheet routeSheet = routeSheetRepo.findById(routeSheetRepo.findMaxId()).get();
        LocalDate lastNumber = routeSheet.getTripDate();
        model.addAttribute("lastNumber", lastNumber);

        List<RouteSheet> routeSheetList = routeSheetRepo.findAllByTripDateIsNotNull();
        model.addAttribute("routeSheetList", routeSheetList);
        return "information";
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

    @Override  // Добавление маршрутов
    public String editingRoutesToRoutSheet(Short distance, String address2, String flag, Model model) {
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
        model.addAttribute("firstPoint", newPoint);
        if (flag.equals("no")) {
            localDateFromAddRoutes = routeSheetRepo.findMaxDate();
            RouteSheet routeSheet = routeSheetRepo.findByTripDate(localDateFromAddRoutes);
            RouteSheet routeSheetForSave = addNewRouteSheet(routeSheet.getTripDate(), routeSheet.getWaybillNumber(), routeSheet, routeSheet.getFueling(), sumDistance, routeList);
            routeSheetRepo.deleteById(routeSheet.getId());
            routeSheetRepo.save(routeSheetForSave);

            routeList = new ArrayList<>();
            localDateFromAddRoutes = null;
            sumDistance = 0;
            pointFlag = true;
            routeList = new ArrayList<>();
            model.addAttribute("errorMessage", "Закончили ввод маршрутов");
            return "menu";
        }
        return "addingRoutes";
    }

    // ПОДСЧЕТ значений полей на новый путевой лист
    public RouteSheet addNewRouteSheet(LocalDate date, Short waybillNumber, RouteSheet lastRouteSheet, Short fueling, Integer distance, List<Route> addressesList) {

        Double consumptionNorm = Precision.round(12D * distance / 100D, 2);
        // Переделать, что бы проьежуток выбирать от даты до даты
        LocalDate winterStartDay = LocalDate.parse("2019-11-01");
        LocalDate winterEndDay = LocalDate.parse("2020-06-30");
        if (date.isAfter(winterStartDay) && date.isBefore(winterEndDay)) {
            consumptionNorm = Precision.round(consumptionNorm * 1.1, 2);
        }
        Double consumptionFact = 0D;
        if (consumptionNorm < 10) {
            consumptionFact = Double.parseDouble(String.valueOf(consumptionNorm).substring(0, 3));
        }
        if (consumptionNorm >= 10 && consumptionNorm < 100) {
            consumptionFact = Double.parseDouble(String.valueOf(consumptionNorm).substring(0, 4));
        }
        if (consumptionNorm >= 100) {
            consumptionFact = Double.parseDouble(String.valueOf(consumptionNorm).substring(0, 5));
        }

        Double fuelStart = lastRouteSheet.getFuelFinish();
        Double fuelFinish = Precision.round(fuelStart + fueling - consumptionFact, 2);
        Integer mileageStart = lastRouteSheet.getMileageFinish();
        Integer mileageFinish = mileageStart + distance;
        Double saving = Precision.round(consumptionNorm - consumptionFact, 3);

        return new RouteSheet(date, waybillNumber, fuelStart, fuelFinish, mileageStart, mileageFinish, fueling, consumptionNorm, consumptionFact, saving, distance, addressesList);
    }
}
