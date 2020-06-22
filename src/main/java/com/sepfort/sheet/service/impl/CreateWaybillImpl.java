package com.sepfort.sheet.service.impl;

import com.sepfort.sheet.domain.RouteSheet;
import com.sepfort.sheet.repo.RouteSheetRepo;
import com.sepfort.sheet.service.CreateWaybill;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

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

/**
 * Creating a waybill in excel.
 */
@Service
public class CreateWaybillImpl implements CreateWaybill {

    /**
     * Last row in excel sheet.
     */
    private int lastLine;

    private RouteSheetRepo routeSheetRepo;

    @Autowired
    public CreateWaybillImpl(RouteSheetRepo routeSheetRepo) {
        this.routeSheetRepo = routeSheetRepo;
    }

    /**
     * Creating a route sheet in Excel.
     *
     * @param data
     * @param model
     * @return Return to the main menu.
     * @throws IOException
     */
    @Override
    public String createWaybill(String data, Model model) throws IOException {
        LocalDate localDate = LocalDate.parse(data);
        RouteSheet routeSheet = routeSheetRepo.findByTripDate(localDate);
        if (routeSheet == null) {
            model.addAttribute("errorMessage", "На " + data + " маршрутный лист отсутствует");
            return "menu";
        }

        List<Integer> integerList = Arrays.asList(4, 10, 6, 3, 6, 7, 21, 11, 30, 4, 35, 4, 38, 11, 39, 11, 40, 11, 41, 11, 42, 11, 49, 11);
        List<String> value = enteringWaybillData(data);

        String newPath = "./src/main/resources/templates/forma/sheet/routeSheet" + routeSheet.getTripDate() + ".xlsx";
        if (Files.isExecutable(Paths.get(newPath))) {
            model.addAttribute("errorMessage", "На " + data + " маршрутный лист уже создан");
            return "menu";
        }
        Path p = Paths.get("./src/main/resources/templates/forma/routeSheetTemplate.xlsx");
        Path newFile = Files.copy(p, Paths.get("./src/main/resources/templates/forma/sheet/routeSheetTemplate.xlsx"), REPLACE_EXISTING);
        Files.move(newFile, newFile.resolveSibling("routeSheet" + routeSheet.getTripDate() + ".xlsx"));

        FileInputStream fileIS = new FileInputStream(new File(newPath));
        XSSFWorkbook workbook = new XSSFWorkbook(fileIS);
        XSSFSheet xssfSheet = workbook.getSheetAt(0);

        int element = 0;
        for (int i = 0; i < 24; i++) {
            Row row = xssfSheet.getRow(integerList.get(i));
            Cell cell = row.getCell(integerList.get(i + 1));
            cell.setCellValue(value.get(element++));
            i++;
        }

        int numberOfRoutes = routeSheet.getRoutes().size() * 3;
        for (int i = 0; i < numberOfRoutes; i = i + 3) {
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
            row6 = xssfSheet2.getRow(58 + i);
            Cell cell11 = row6.createCell(list.get(0));
            cell11.setCellValue(routeSheet.getWaybillNumber());
            Cell cell12 = row6.createCell(list.get(1));
            cell12.setCellValue(lineBreak(routeSheet.getRoutes().get(index).getDeparture_point()));
            Cell cell13 = row6.createCell(list.get(2));
            cell13.setCellValue(lineBreak(routeSheet.getRoutes().get(index).getDestination()));
            Cell cell14 = row6.createCell(list.get(3));
            cell14.setCellValue(lineBreak(String.valueOf(routeSheet.getRoutes().get(index).getDistance())));
            index++;
        }

        FileOutputStream fos2 = new FileOutputStream(newPath);
        workbook2.write(fos2);
        fos.close();

        // Вывод на принтер
        //    Desktop.getDesktop().mail();
        //    Desktop.getDesktop().print(new File("C://Users/SGavrilov/Desktop/blue-butterfly-images-clipart-13.png"));

        lastLine = 0;
        model.addAttribute("errorMessage", "Маршрутный лист на " + data + " успешно сформирован");
        return "menu";
    }

    /**
     * Formation of a line with hyphens.
     * @param text Text
     * @return
     */
    private String lineBreak(String text) {
        String resultLine1;
        String resultLine2;
        String resultLineEnd = "";
        String resultLine3;
        String l = String.valueOf(text);
        char[] chars = l.toCharArray();
        if (chars.length < 15) {
            resultLineEnd = l;
        }
        if (chars.length >= 15 && chars.length < 30) {
            resultLine1 = new String(chars, 0, 15);
            resultLine2 = new String(chars, 15, chars.length - 15);
            resultLineEnd = resultLine1 + "\n" + resultLine2;
        }
        if (chars.length >= 30) {
            resultLine1 = new String(chars, 0, 15);
            resultLine2 = new String(chars, 15, 15);
            resultLine3 = new String(chars, 30, chars.length - 30);
            resultLineEnd = resultLine1 + "\n" + resultLine2 + "\n" + resultLine3;
        }
        return resultLineEnd;
    }

    /**
     * Entering waybill data.
     * @param data Date.
     * @return List of waybill.
     */
    private List<String> enteringWaybillData(String data) {
        List<String> stringList = new ArrayList<>();
        var localDate = LocalDate.parse(data);
        var routeSheet = routeSheetRepo.findByTripDate(localDate);
        stringList.add(0, String.valueOf(routeSheet.getWaybillNumber()));
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
