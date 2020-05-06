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

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Service
public class RouteSheetServiceImpl implements RouteSheetService {
    private Long sumDistance = 0L;
    private List<Addresses> addressesList = new ArrayList<>();
    private LocalDate localDateFromAddRoutes;
    private Long fueling;
    private String dataNow;
    private RouteSheet lastRouteSheet;
    private boolean isAddRouteSheet = true;
    private int lastLine;

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

        LocalDate localDate = LocalDate.parse(data);
        RouteSheet routeSheet = routeSheetRepo.findByData(localDate);
        if ( routeSheet == null) {
            return "menu";
        }

        List<Integer> integerList = Arrays.asList(4, 10, 6, 3, 6, 7, 21, 11, 30, 4, 35, 4, 38, 11, 39, 11, 40, 11, 41, 11, 42, 11, 49, 11);
        System.out.println(">>> data = " + data);
        List<String> value = enteringWaybillData(data);

        System.out.println(">>> Запись путевого листа в память");
        Path p = Paths.get("./src/main/resources/templates/forma/routeSheetTemplate.xlsx");
        Path newFile = Files.copy(p, Paths.get("./src/main/resources/templates/forma/sheet/routeSheetTemplate.xlsx"), REPLACE_EXISTING);
        String newPath = "./src/main/resources/templates/forma/sheet/routeSheet" + routeSheet.getData() + ".xlsx";
        if (Files.isExecutable(newFile)) {
            return "menu";
        }
        Files.move(newFile, newFile.resolveSibling("routeSheet" + routeSheet.getData() + ".xlsx"));

        System.out.println(">>> Записали");
        FileInputStream fileIS = new FileInputStream(new File(newPath));
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

        int numberOfRoutes = routeSheet.getAddress().size() * 3;
        System.out.println(">>> Количество " + numberOfRoutes);
        for (int i = 0; i < numberOfRoutes; i = i + 3) {
            System.out.println(">>> " + i);
            CellRangeAddress cellRangeAddress1 = new CellRangeAddress(58 + i, 60 + i, 0, 0);
            xssfSheet.addMergedRegion(cellRangeAddress1);
            CellRangeAddress cellRangeAddress2 = new CellRangeAddress(58 + i, 60 + i, 1, 1);
            xssfSheet.addMergedRegion(cellRangeAddress2);
            CellRangeAddress cellRangeAddress3 = new CellRangeAddress(58 + i, 60 + i, 2, 2);
            xssfSheet.addMergedRegion(cellRangeAddress3);
            CellRangeAddress cellRangeAddress4 = new CellRangeAddress(58 + i, 60 + i, 3, 3);
            xssfSheet.addMergedRegion(cellRangeAddress4);
            CellRangeAddress cellRangeAddress5 = new CellRangeAddress(58 + i, 60 + i, 4, 5);
            xssfSheet.addMergedRegion(cellRangeAddress5);
            CellRangeAddress cellRangeAddress6 = new CellRangeAddress(58 + i, 60 + i, 6, 6);
            xssfSheet.addMergedRegion(cellRangeAddress6);
            CellRangeAddress cellRangeAddress7 = new CellRangeAddress(58 + i, 60 + i, 7, 8);
            xssfSheet.addMergedRegion(cellRangeAddress7);
            CellRangeAddress cellRangeAddress8 = new CellRangeAddress(58 + i, 60 + i, 9, 12);
            xssfSheet.addMergedRegion(cellRangeAddress8);
            lastLine = 60 + i;
        }

        //Ввод последних строк
        Row row1 = xssfSheet.createRow(lastLine + 1);
        Cell cell1 = row1.createCell(0);
        cell1.setCellValue("пройдено, км");
        Cell cell2 = row1.createCell(6);
        cell2.setCellValue("за часы, руб.коп.");

        Row row2 = xssfSheet.createRow(lastLine + 3);
        Cell cell3 = row2.createCell(6);
        cell3.setCellValue("Итого, руб.коп.");

        Row row3 = xssfSheet.createRow(lastLine + 5);
        Cell cell4 = row3.createCell(0);
        cell4.setCellValue("Расчет произвел");
        Cell cell5 = row3.createCell(1);
        cell5.setCellValue("Генеральный директор");
        Cell cell6 = row3.createCell(6);
        cell6.setCellValue("Ковалев Г.П.");

        Row row4 = xssfSheet.createRow(lastLine + 6);
        Cell cell7 = row4.createCell(1);
        cell7.setCellValue("должность");
        Cell cell8 = row4.createCell(2);
        cell8.setCellValue("подпись");
        Cell cell9 = row4.createCell(6);
        cell9.setCellValue("расшифровка подписи");


        FileOutputStream fos = new FileOutputStream(newPath);
        workbook.write(fos);
        fos.close();

        // После создания в маршрутном листе списка маршрутов и нижней части - заполняем её

        // Ввод пройденных километров за маршрутами
        FileInputStream fileIS2 = new FileInputStream(new File(newPath));
        XSSFWorkbook workbook2 = new XSSFWorkbook(fileIS2);
        XSSFSheet xssfSheet2 = workbook2.getSheetAt(0);

        Row row5 = xssfSheet2.getRow(lastLine + 1);
        Cell cell10 = row5.createCell(2);
        cell10.setCellValue(routeSheet.getDistance());

        //Ввод маршрутов
        Row row6;
        int index = 0;
        List<Integer> list = Arrays.asList(0, 2, 3, 7);
        for (int i = 0; i < numberOfRoutes; i = i + 3) {
            System.out.println(">>> " + i);
            row6 = xssfSheet2.getRow(58 + i);
            Cell cell11 = row6.createCell(list.get(0));
            cell11.setCellValue(routeSheet.getNumber());
            Cell cell12 = row6.createCell(list.get(1));
            cell12.setCellValue(lineBreak(routeSheet.getAddress().get(index).getDeparture_point()));
            Cell cell13 = row6.createCell(list.get(2));
            cell13.setCellValue(lineBreak(routeSheet.getAddress().get(index).getDestination()));
            Cell cell14 = row6.createCell(list.get(3));
            cell14.setCellValue(lineBreak(String.valueOf(routeSheet.getAddress().get(index).getDistance())));
            index++;
        }

        FileOutputStream fos2 = new FileOutputStream(newPath);
        workbook2.write(fos2);
        fos.close();

        Desktop.getDesktop().mail();
    //    Desktop.getDesktop().print(new File("C://Users/SGavrilov/Desktop/blue-butterfly-images-clipart-13.png"));

        lastLine = 0;
        return "menu";
    }

    private String lineBreak(String text) {
        String result;
        String result2;
        String result3 = "";
        String result4;
        String l = String.valueOf(text);
        char[] chars = l.toCharArray();
        System.out.println(">>> Длина строки = " + chars.length);
        int dif = chars.length - 15;
        if (chars.length < 15) {
            result3 = l;
        }
        if (chars.length >= 15 && chars.length < 30) {
            result = new String(chars, 0, 15);
            result2 = new String(chars, 15, chars.length - 15);
            result3 = result + "\n" + result2;
        }
        if (chars.length >= 30) {
            result = new String(chars, 0, 15);
            result2 = new String(chars, 15, 15);
            result4 = new String(chars, 30, chars.length - 30);
            result3 = result + "\n" + result2 + "\n" + result4;
        }
        return result3;
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
