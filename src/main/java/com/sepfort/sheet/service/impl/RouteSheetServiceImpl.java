package com.sepfort.sheet.service.impl;

import com.sepfort.sheet.domain.Addresses;
import com.sepfort.sheet.domain.RouteSheet;
import com.sepfort.sheet.repo.RouteSheetRepo;
import com.sepfort.sheet.service.RouteSheetService;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.math3.util.Precision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class RouteSheetServiceImpl implements RouteSheetService {
    private boolean pointFlag = true;
    private String newPoint;
    private Long sumDistance = 0L;
    private List<Addresses> addressesList = new ArrayList<>();
    private LocalDate localDateFromAddRoutes; // дата первого ПЛ

    @Autowired
    private RouteSheetRepo routeSheetRepo;

    @Override  // Добавление в БД первого ПЛ
    public String addingFirstRouteSheetToDatabase(String dateToString, Long number, Double fuelStart, Double fuelFinish, Long mileageStart, Long mileageFinish, Long fueling, Double consumptionNorm, Double consumptionFact, Model model) {
        localDateFromAddRoutes = LocalDate.parse(dateToString);
        var saving = consumptionFact - consumptionNorm;
        routeSheetRepo.save(new RouteSheet(localDateFromAddRoutes, number, fuelStart, fuelFinish, mileageStart, mileageFinish, fueling, consumptionNorm, consumptionFact, saving));
        var lastRouteSheet = routeSheetRepo.findById(routeSheetRepo.findMaxId()).get();
        model.addAttribute("lastRouteSheet", lastRouteSheet);
        model.addAttribute("errorMessage", "Первый маршрутный лист заполен и помещён в базу данных.");
        return "menu";
    }

    @Override  // Добавление в БД нового ПЛ
    public String addRouteSheetToDatabase(Long fuel, String data, String isEdit, Model model) {
        if (routeSheetRepo.findByData(LocalDate.parse(data)) != null && isEdit.equals("no")) {
            model.addAttribute("errorMessage", "На " + data + " есть путевой лист");
            return "menu";
        }
        Long number;
        if (isEdit.equals("yes")) {
            number = routeSheetRepo.findByData(routeSheetRepo.findDataMax()).getNumber();
            var id = routeSheetRepo.findByData(LocalDate.parse(data)).getId();
            routeSheetRepo.deleteById(id);
        } else {
            number = routeSheetRepo.findByData(routeSheetRepo.findDataMax()).getNumber() + 1L;
        }
        var lastRouteSheet = routeSheetRepo.findById(routeSheetRepo.findMaxId()).get();
        var routeSheet = addNewRouteSheet(LocalDate.parse(data), number, lastRouteSheet, fuel, 0L, null);
        routeSheetRepo.save(routeSheet);
        model.addAttribute("errorMessage", "Новый маршрутный лист на " + data + " создан.");
        return "menu";
    }

    @Override // Добавление и редактирование маршрутов
    public String addingRoutesToRoutSheet(String date, String isEdit, Model model) {
        if (routeSheetRepo.findByData(LocalDate.parse(date)) == null) {
            model.addAttribute("errorMessage", "На " + date + " нет маршрутного листа!");
            return "menu";
        }
        if (!routeSheetRepo.findByData(LocalDate.parse(date)).getAddress().isEmpty() && isEdit.equals("no")) {
            model.addAttribute("errorMessage", "Нельзя добавить маршруты, они уже заполнены.");
            return "menu";
        }
        if (isEdit.equals("yes")) {
            routeSheetRepo.findByData(LocalDate.parse(date)).setAddress(new ArrayList<>());
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
        var routeSheet = routeSheetRepo.findById(routeSheetRepo.findMaxId()).get();
        var lastNumber = routeSheet.getData();
        model.addAttribute("lastNumber", lastNumber);

        List<RouteSheet> routeSheetList = routeSheetRepo.findAllByDataIsNotNull();
        model.addAttribute("routeSheetList", routeSheetList);
        return "information";
    }

    @Override // Вывод маршрутного листа на экран по дате
    public String output(String date, Model model) {
        var data = LocalDate.parse(date);
        var routeSheet = routeSheetRepo.findByData(data);
        if (routeSheet == null) {
            model.addAttribute("errorMessage", "На " + date + " маршрутного листа нет");
            return "menu";
        }
        model.addAttribute("routeSheet", routeSheet);
        model.addAttribute("numberRouteSheet", date);
        return "output";
    }

    @Override  // Добавление маршрутов
    public String editingRoutesToRoutSheet(Long distance, String address2, String flag, Model model) {
        String firstPoint;
        if (pointFlag) {
            firstPoint = "Маршала Говорова";
            pointFlag = false;
        } else {
            firstPoint = newPoint;
        }
        newPoint = address2;
        var addresses = new Addresses(firstPoint, address2, distance);
        sumDistance += distance;
        addressesList.add(addresses);
        model.addAttribute("firstPoint", newPoint);
        if (flag.equals("no")) {
            localDateFromAddRoutes = routeSheetRepo.findDataMax();
            var routeSheet = routeSheetRepo.findByData(localDateFromAddRoutes);
            var routeSheetForSave = addNewRouteSheet(routeSheet.getData(), routeSheet.getNumber(), routeSheet, routeSheet.getFueling(), sumDistance, addressesList);
            routeSheetRepo.deleteById(routeSheet.getId());
            routeSheetRepo.save(routeSheetForSave);

            addressesList = new ArrayList<>();
            localDateFromAddRoutes = null;
            sumDistance = 0L;
            pointFlag = true;
            addressesList = new ArrayList<>();
            model.addAttribute("errorMessage", "Закончили ввод маршрутов");
            return "menu";
        }
        return "addingRoutes";
    }

    // ПОДСЧЕТ значений полей на новый путевой лист
    public RouteSheet addNewRouteSheet(LocalDate date, Long number, RouteSheet lastRouteSheet, Long fueling, Long distance, List<Addresses> addressesList) {

        Double consumptionNorm = Precision.round(12D * distance / 100D, 2);
        // Переделать, что бы проьежуток выбирать от даты до даты
        var winterStartDay = LocalDate.parse("2019-11-01");
        var winterEndDay = LocalDate.parse("2020-06-30");
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

        var fuelStart = lastRouteSheet.getFuelFinish();
        var fuelFinish = Precision.round(fuelStart + fueling - consumptionFact, 2);
        var mileageStart = lastRouteSheet.getMileageFinish();
        var mileageFinish = mileageStart + distance;
        var saving = Precision.round(consumptionNorm - consumptionFact, 3);

        return new RouteSheet(date, number, fuelStart, fuelFinish, mileageStart, mileageFinish, fueling, consumptionNorm, consumptionFact, saving, distance, addressesList);
    }
}
