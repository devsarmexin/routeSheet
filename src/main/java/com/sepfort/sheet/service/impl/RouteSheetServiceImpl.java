package com.sepfort.sheet.service.impl;

import com.sepfort.sheet.domain.Addresses;
import com.sepfort.sheet.domain.RouteSheet;
import com.sepfort.sheet.domain.User;
import com.sepfort.sheet.repo.RouteSheetRepo;
import com.sepfort.sheet.service.RouteSheetService;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.math3.util.Precision;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class RouteSheetServiceImpl implements RouteSheetService {
    private Long sumDistance = 0L;
    private List<Addresses> addressesList = new ArrayList<>();
    private LocalDate localDateFromAddRoutes;
    private Long fueling;
    private String dataNow;
    private RouteSheet lastRouteSheet;
    private boolean isAddRouteSheet = true;

    @Autowired
    private RouteSheetRepo routeSheetRepo;
    @Autowired
    private RouteSheetService routeSheetService;


    // Добавление первого путевого листа
    public void savePrimaryInput(
            String dateToString,
            Long number,
            Double fuelStart,
            Double fuelFinish,
            Long mileageStart,
            Long mileageFinish,
            Long fueling,
            Double consumptionNorm,
            Double consumptionFact,
            Double saving
    ) {
        LocalDate date = LocalDate.parse(dateToString);
        RouteSheet routeSheet = new RouteSheet(
                date,
                number,
                fuelStart,
                fuelFinish,
                mileageStart,
                mileageFinish,
                fueling,
                consumptionNorm,
                consumptionFact,
                saving
        );
        routeSheetRepo.save(routeSheet);
    }

    // Добавление маршрутов
    public String addRoute(
            Long distance,
            String address1,
            String address2,
            String flag
    ) {
        //    routeSheetService.addRoute(distance, address1, address2, flag);
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

    @Override
    public String generalInformation(Model model) {
        Long idMax = routeSheetRepo.findMaxId();
        if (idMax == null) {
            model.addAttribute("lastNumber", "База данных пуста");
        } else {
            RouteSheet routeSheet = routeSheetRepo.findById(idMax).get();
            LocalDate lastNumber = routeSheet.getData();
            model.addAttribute("lastNumber", lastNumber);
            //      routeSheetService.generalInformation(model);
        }
        return "information";
    }

    @Override
    public String goToAddRoute(Long fuel, String data) {
        fueling = fuel;
        dataNow = data;
        return "filling";
    }

    @Override
    public String edit(String date) {
        localDateFromAddRoutes = LocalDate.parse(date);
        return "addingRoutes";
    }

    @Override
    public String addRouteSheet(
            User user,
            String address1,
            String address2,
            Long distance,
            String flag,
            Model model) {
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

    @Override
    public String primaryInput(
            String dateToString,
            Long number,
            Double fuelStart,
            Double fuelFinish,
            Long mileageStart,
            Long mileageFinish,
            Long fueling,
            Double consumptionNorm,
            Double consumptionFact,
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

    @Override
    public String output(String date, Model model) {
        LocalDate data = LocalDate.parse(date);
        RouteSheet routeSheet = routeSheetRepo.findByData(data);
        if (routeSheet == null) {
            return "menu";
        }
        model.addAttribute("routeSheet", routeSheet);
        return "output";
    }

    @Override
    public String fillingOutTheWaybill() {
        if (IterableUtils.size(routeSheetRepo.findAll()) == 0) {  // Проверка на присутствие в БД хоть одного путевого листа
            return "first";
        }
        return "fueling";
    }

    @Override
    public void addRouteSheet(
            User user,
            String dateToString,
            Long number,
            RouteSheet lastRouteShee,
            Long fueling,
            Long distance,
            List<Addresses> addressesList
    ) {
        //Расчёт данных путевого листа
        LocalDate date = LocalDate.parse(dateToString);
        System.out.println(">>> LocalDate = " + date);
        Double consumptionNorm = Precision.round(12D * distance / 100D, 2);
        System.out.println(">>>>>>> consumptionNorm = " + consumptionNorm);

        // Переделать, что бы проьежуток выбирать от даты до даты
        LocalDate winterStartDay = LocalDate.parse("2019-11-01");
        LocalDate winterEndDay = LocalDate.parse("2020-06-30");
        if (date.isAfter(winterStartDay) && date.isBefore(winterEndDay)) {
            consumptionNorm = Precision.round(consumptionNorm * 1.1, 2);
            System.out.println(">>> в зиме ");
        }
        System.out.println(">>> consumptionNorm = " + consumptionNorm);
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
        System.out.println(">>> consumptionFact = " + consumptionFact);

        Double fuelStart = lastRouteShee.getFuelFinish();
        Double fuelFinish = Precision.round(fuelStart + fueling - consumptionFact, 2);
        Long mileageStart = lastRouteShee.getMileageFinish();
        Long mileageFinish = mileageStart + distance;
        Double saving = Precision.round(consumptionNorm - consumptionFact, 3);

        RouteSheet routeSheet = new RouteSheet(
                date,
                number,
                fuelStart,
                fuelFinish,
                mileageStart,
                mileageFinish,
                fueling,
                consumptionNorm,
                consumptionFact,
                saving,
                distance,
                addressesList
        );
        System.out.println(">>> Создали новый RouteSheet");
        routeSheetRepo.save(routeSheet);
        System.out.println(">>> Положили RouteSheet в БД");
    }

    @Override
    public String createWaybill(String data) throws IOException {
        List<Integer> integerList = Arrays.asList(4, 10, 6, 3, 6, 7, 21, 11, 30, 4, 35, 4, 38, 11, 39, 11, 40, 11, 41, 11, 42, 11, 49, 11);
        System.out.println(">>> data = " + data);
        List<String> value = enteringWaybillData(data);

        FileInputStream fileIS = new FileInputStream(new File("C:/exp/tmp/new.xlsx"));
        XSSFWorkbook workbook = new XSSFWorkbook(fileIS);
        XSSFSheet xssfSheet = workbook.getSheetAt(0);

        int element = 0;
        for (int i = 0; i < 24; i++) {
            Row row = xssfSheet.getRow(integerList.get(i));
            Cell cell = row.getCell(integerList.get(i + 1));
            cell.setCellValue(value.get(element++));
            System.out.println(">>> element = " + element);
            i++;
        }

        xssfSheet.ungroupRow(58, 66);
        CellRangeAddress cellRangeAddress1 = new CellRangeAddress(58, 60, 0, 0);
        xssfSheet.addMergedRegion(cellRangeAddress1);
        CellRangeAddress cellRangeAddress2 = new CellRangeAddress(58, 60, 1, 1);
        xssfSheet.addMergedRegion(cellRangeAddress2);
        CellRangeAddress cellRangeAddress3 = new CellRangeAddress(58, 60, 2, 2);
        xssfSheet.addMergedRegion(cellRangeAddress3);
        CellRangeAddress cellRangeAddress4 = new CellRangeAddress(58, 60, 3, 3);
        xssfSheet.addMergedRegion(cellRangeAddress4);
        CellRangeAddress cellRangeAddress5 = new CellRangeAddress(58, 60, 4, 4);
        xssfSheet.addMergedRegion(cellRangeAddress5);
        CellRangeAddress cellRangeAddress6 = new CellRangeAddress(58, 60, 5, 5);
        xssfSheet.addMergedRegion(cellRangeAddress6);
        CellRangeAddress cellRangeAddress7 = new CellRangeAddress(58, 60, 6, 7);
        xssfSheet.addMergedRegion(cellRangeAddress7);
        CellRangeAddress cellRangeAddress8 = new CellRangeAddress(58, 60, 8, 11);
        xssfSheet.addMergedRegion(cellRangeAddress8);
        Row row = xssfSheet.getRow(58);
        Cell cell = row.getCell(0);
        cell.setCellValue("111");

        FileOutputStream fos = new FileOutputStream("C:/exp/tmp/new.xlsx");
        workbook.write(fos);
        fos.close();
        return "menu";
    }

    private List<String> enteringWaybillData(String data) {
        List<String> stringList = new ArrayList<>();
        LocalDate localDate = LocalDate.parse(data);
        RouteSheet routeSheet = routeSheetRepo.findByData(localDate);
        stringList.add(0, String.valueOf(routeSheet.getNumber()));
        stringList.add(1, data);
        stringList.add(2, data);
        stringList.add(3, String.valueOf(routeSheet.getMileageStart()));
        stringList.add(4, data);
        stringList.add(5, data);
        stringList.add(6, String.valueOf(routeSheet.getFuelStart()));
        stringList.add(7, String.valueOf(routeSheet.getFuelFinish()));
        stringList.add(8, String.valueOf(routeSheet.getConsumptionNorm()));
        stringList.add(9, String.valueOf(routeSheet.getConsumptionFact()));
        stringList.add(10, String.valueOf(routeSheet.getSaving()));
        stringList.add(11, String.valueOf(routeSheet.getMileageFinish()));
        return stringList;
    }
}
