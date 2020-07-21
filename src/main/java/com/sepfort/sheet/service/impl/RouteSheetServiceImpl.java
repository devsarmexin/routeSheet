package com.sepfort.sheet.service.impl;

import com.sepfort.sheet.domain.Route;
import com.sepfort.sheet.domain.RouteSheet;
import com.sepfort.sheet.dto.RouteSheetDto;
import com.sepfort.sheet.repo.RouteRepo;
import com.sepfort.sheet.repo.RouteSheetRepo;
import com.sepfort.sheet.service.RouteSheetService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RouteSheetServiceImpl implements RouteSheetService {

    /**
     * Flag of the starting point of the first route of the waybill.
     */
    private boolean pointFlag = true;

    /**
     * Starting point of the route of the waybill.
     */
    private String newPoint;

    /**
     * The sum of the distance traveled.
     */
    private Integer sumDistance = 0;

    /**
     * List of directions of the waybill.
     */
    private List<Route> routeList = new ArrayList<>();

    /**
     * Date of first waybill.
     */
    private LocalDate localDateFromAddRoutes;

    /**
     * Date for entering routes on the waybill.
     */
    private LocalDate dateForAddRoutes;

    /**
     * Modified waybill.
     */
    private Integer numberModifiedWAyBill;

    private int step;

    private RouteSheetRepo routeSheetRepo;
    private RouteRepo routeRepo;

    @Autowired
    public RouteSheetServiceImpl(final RouteSheetRepo routeSheetRepo, final RouteRepo routeRepo) {
        this.routeSheetRepo = routeSheetRepo;
        this.routeRepo = routeRepo;
    }

    /**
     * Adding the first waybill to the database.
     *
     * @param routeSheetDto routeSheetDto.
     * @return Operation Result Information.
     */
    @Override
    public Map<String, String> addingFirstRouteSheetToDatabase(final RouteSheetDto routeSheetDto) {
        localDateFromAddRoutes = LocalDate.parse(routeSheetDto.getTripDate());
        Double saving = routeSheetDto.getConsumptionFact() - routeSheetDto.getConsumptionNorm();
        routeSheetRepo.save(new RouteSheet(localDateFromAddRoutes, routeSheetDto.getWaybillNumber(), routeSheetDto.getFuelStart(), routeSheetDto.getFuelFinish(), routeSheetDto.getMileageStart(), routeSheetDto.getMileageFinish(), routeSheetDto.getFueling(), routeSheetDto.getConsumptionNorm(), routeSheetDto.getConsumptionFact(), saving));
        Map<String, String> answerToMenu = new HashMap<>();
        answerToMenu.put("errorMessage", "Первый маршрутный лист заполен и помещён в базу данных.");
        return answerToMenu;
    }

    /**
     * Adding a new waybill to the database.
     *
     * @param routeSheetDto routeSheetDto.
     * @param isEdit        Is the waybill editable.
     * @return Operation Result Information.
     */
    @Override
    public Map<String, String> addRouteSheetToDatabase(final RouteSheetDto routeSheetDto, final String isEdit) {
        Map<String, String> answerToMenu = new HashMap<>();
        System.out.println("Дата изменяемого ПЛ " + routeSheetDto.getTripDate());
//        if (routeSheetRepo.findByTripDate(LocalDate.parse(routeSheetDto.getTripDate())) != null && isEdit.equals("no")) {
//            answerToMenu.put("errorMessage", "На " + routeSheetDto.getTripDate() + " есть путевой лист");
//            return answerToMenu;
//        }
        RouteSheet lastRouteSheet = routeSheetRepo.findById(routeSheetRepo.findMaxId()).get();
//        if (lastRouteSheet.getMileageFinish() == null) {
//            answerToMenu.put("errorMessage", "Предыдущий маршрутный лист без пробега (нет маршрутов)");
//            return answerToMenu;
//        }
        if (isEdit.equals("yes")) {
            //Вставить проверку на то, что номер ПЛ больше 1. Иначе надо заносить данные через ввод первого ПЛ.
            LocalDate date = LocalDate.parse(routeSheetDto.getTripDate());
            Integer id = routeSheetRepo.findByTripDate(date).getId();
            RouteSheet routesheet = changeWaybill(routeSheetDto);
            routeSheetRepo.save(routesheet);
            routeSheetRepo.deleteById(id);
            answerToMenu.put("errorMessage", "Изменённый маршрутный лист на " + routeSheetDto.getTripDate() + " создан. ТЕПЕРЬ ОБЯЗАТЕЛЬНО ВВЕДИТЕ МАРШРУТЫ НА ЭТУ ДАТУ!!!");
            return answerToMenu;
        }
        RouteSheet routeSheet = calculationNewWaybill(routeSheetDto, lastRouteSheet);
        routeSheetRepo.save(routeSheet);
        answerToMenu.put("errorMessage", "Новый маршрутный лист на " + routeSheetDto.getTripDate() + " создан.");
        return answerToMenu;
    }

    /**
     * Change waybill.
     * @param routeSheetDto routeSheetDto.
     * @return
     */
    private RouteSheet changeWaybill(final RouteSheetDto routeSheetDto) {
        RouteSheet routeSheet = routeSheetRepo.findByTripDate(LocalDate.parse(routeSheetDto.getTripDate()));
        Integer waybillNumber = routeSheet.getWaybillNumber();
        RouteSheet previousRouteSheet = routeSheetRepo.findByWaybillNumber(waybillNumber);
        RouteSheet modifiedRouteSheet = calculationChangeNewWaybill(routeSheetDto, previousRouteSheet);
        return modifiedRouteSheet;
    }

    /**
     * Пересчёт изменяемого ПЛ
     * @param routeSheetDto
     * @param previousRouteSheet
     * @return
     */

    private RouteSheet calculationChangeNewWaybill(final RouteSheetDto routeSheetDto, final RouteSheet previousRouteSheet) {
//сделать проверку на то, что возможен изменен первый ПЛ и предыдущего не будет путевого листа.
        Integer waybillNumber = previousRouteSheet.getWaybillNumber();
        LocalDate date = LocalDate.parse(routeSheetDto.getTripDate());
        Double fuelStart = previousRouteSheet.getFuelStart();
        Integer mileageStart = previousRouteSheet.getMileageStart();
        Short fueling = routeSheetDto.getFueling();
        return new RouteSheet(date, waybillNumber, fuelStart, mileageStart, fueling);
    }

    /**
     * Check for the existence of a waybill for the desired date.
     *
     * @param date Searched date.
     * @return Try or false.
     */
    @Override
    public boolean thereAreRoutes(final String date) {
        dateForAddRoutes = LocalDate.parse(date);
        return routeSheetRepo.findByTripDate(LocalDate.parse(date)).getRoutes() == null;
    }

    /**
     * Display information on all existing waybills.
     * @return List of waybills.
     */
    @Override
    public List<RouteSheet> generalInformation() {
        if (IterableUtils.size(routeSheetRepo.findAll()) == 0) {
            return null;
        }
        List<RouteSheet> routeSheetList = new ArrayList<>();
        CollectionUtils.addAll(routeSheetList, routeSheetRepo.findAll().iterator());
        return routeSheetList;
    }

    /**
     * Display route sheet by date.
     * @param date Searched date.
     * @param model Representation.
     * @return Returns to the main menu if there is no waybill for the desired date or displays the contents of the waybill.
     */
    @Override
    public String output(final String date, final Model model) {
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

    /**
     * Determines whether the base is empty or not.
     * @return Try or false.
     */
    @Override // Запрос : пуста ли база RouteSheet
    public boolean queryDatabaseIsEmpty() {
        return IterableUtils.size(routeSheetRepo.findAll()) == 0;
    }

    /**
     * We fill in all directions of the waybill.
     * @param distance Distance between waypoints on a waybill.
     * @param routeEndPointAddress route endpoint address.
     * @return We return to the main menu if there are no more routes, otherwise we go to enter a new waybill route.
     */
    @Override
    public String editingRoutesToRoutSheet(final Short distance, final String routeEndPointAddress) {
        String firstPoint;
        if (pointFlag) {
            firstPoint = "Маршала Говорова";
            pointFlag = false;
        } else {
            firstPoint = newPoint;
        }
        newPoint = routeEndPointAddress;
        Route route = new Route(firstPoint, routeEndPointAddress, distance);
        sumDistance += distance;
        routeList.add(route);
        return newPoint;
    }

    /**
     * Enter the last waybill route.
     * @param distance Distance between waypoints on a waybill.
     * @param routeEndPointAddress route endpoint address.
     */
    @Override
    public void editingRoutesToRoutSheetEnd(final Short distance, final String routeEndPointAddress) {
        RouteSheet routeSheet = routeSheetRepo.findByTripDate(dateForAddRoutes);
        if (routeList.isEmpty()) {
            Route route = new Route("Маршала Говорова", routeEndPointAddress, distance);
            sumDistance += distance;
            routeList.add(route);
        } else {
            Route route = new Route(newPoint, routeEndPointAddress, distance);
            sumDistance += distance;
            routeList.add(route);
        }

        RouteSheet routeSheetForSave = calculationNewWaybillWithRoutes(routeSheet);
        routeSheetRepo.save(routeSheetForSave);

        routeList = new ArrayList<>();
        sumDistance = 0;
        pointFlag = true;
        routeList = new ArrayList<>();
    }

    /**
     * Deleting a database.
     */
    @Override
    public void delete() {
        routeSheetRepo.deleteAll();
        routeRepo.deleteAll();
    }

    // пересчёт всех маршрутных листов
    /**
     * Database recalculation.
     */
    @Override
    public void databaseRecalculation() {
        step = 1;
        Integer maximumNumberOfWaybills = routeSheetRepo.findMaxNumberOfWaybills();
        while (routeSheetRepo.findByWaybillNumber(step) != null) {
            RouteSheet modifiedWayBill = routeSheetRepo.findRouteSheetByWaybillNumber(step);
            if (step == maximumNumberOfWaybills) {
                return;
            }
            RouteSheet previousWaybill = routeSheetRepo.findByWaybillNumber(step);
            RouteSheet waybill = routeSheetRepo.findByWaybillNumber(step + 1);

            Double fuelStart = previousWaybill.getFuelFinish();
            Integer mileageStart = previousWaybill.getMileageFinish();
            Integer mileageFinish = mileageStart + waybill.getDistance();
            Double fuelFinish = previousWaybill.getFuelFinish() + waybill.getFueling() - waybill.getConsumptionFact();

            waybill.setFuelStart(fuelStart);
            waybill.setMileageStart(mileageStart);
            waybill.setMileageFinish(mileageFinish);
            waybill.setFuelFinish(fuelFinish);

            routeSheetRepo.deleteById(waybill.getId());
            routeSheetRepo.save(waybill);
            RouteSheet x = routeSheetRepo.findByWaybillNumber(2);

            step++;
        }
    }

    /**
     * calculation New Waybill With Routes.
     * @param routeSheet routeSheet.
     * @return routeSheet.
     */
    private RouteSheet calculationNewWaybillWithRoutes(final RouteSheet routeSheet) {
        RouteSheet lastRouteSheet = routeSheetRepo.findRouteSheetByWaybillNumber(routeSheet.getWaybillNumber() - 1);

        routeSheet.setMileageFinish(routeSheet.getMileageStart() + sumDistance);
        Double consumptionNorm = sumDistance * 12D / 100D;
        routeSheet.setConsumptionNorm(consumptionNorm);
        Double consumptionFact = (Double) Math.floor(consumptionNorm * 10) / 10.0;
        routeSheet.setConsumptionFact(consumptionFact);
        routeSheet.setSaving(consumptionNorm - consumptionFact);
        routeSheet.setFuelFinish(routeSheet.getFuelStart() + routeSheet.getFueling() - consumptionFact);
        routeSheet.setRoutes(routeList);
        routeSheet.setDistance(sumDistance);
        return routeSheet;
    }

    /**
     * Calculation new waybill.
     * @param routeSheetDto routeSheetDto.
     * @param previousRouteSheet Previous RouteSheet.
     * @return Modified waybill.
     */
    private RouteSheet calculationNewWaybill(final RouteSheetDto routeSheetDto, final RouteSheet previousRouteSheet) {
        Integer waybillNumber = previousRouteSheet.getWaybillNumber() + 1;
        Double fuelStart = previousRouteSheet.getFuelFinish();
        Integer mileageStart = previousRouteSheet.getMileageFinish();
        Short fueling = routeSheetDto.getFueling();
        Integer mileageFinish = routeSheetDto.getMileageFinish();
        return new RouteSheet(LocalDate.parse(routeSheetDto.getTripDate()), waybillNumber, fuelStart, mileageStart, mileageFinish, fueling);
    }
}
