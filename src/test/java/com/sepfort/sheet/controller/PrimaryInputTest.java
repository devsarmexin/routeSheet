package com.sepfort.sheet.controller;

import com.sepfort.sheet.Application;
import com.sepfort.sheet.domain.Addresses;
import com.sepfort.sheet.domain.RouteSheet;
import com.sepfort.sheet.repo.RouteSheetRepo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.util.*;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ComponentScan(basePackages = "com.sepfort.sheet")
@SpringBootTest(classes = {Application.class,})
public class PrimaryInputTest {
    @Autowired
    private MainController mainController;
    @Autowired
    private RouteSheetRepo routeSheetRepo;

    @Before
    public void before() {

    }

    @Test
    public void testPrimaryInput() {
        assertEquals("menu", mainController.primaryInput("2020-05-01", 1L, 10D, 40D, 10L, 100L, 50L, 10D, 10D, createModel()));
    }

    @Test
    public void testGeneralInformation() {
        assertEquals("menu", mainController.generalInformation(createModel()));
    }

    @Test
    public void testIntro() {
        assertEquals("routeSheet", mainController.intro());
    }

    @Test
    public void testFillingOutTheWaybill() {
        assertEquals("first", mainController.fillingOutTheWaybill());
    }

    @Test
    public void testGoToAddRoute() {
        assertEquals("menu", mainController.goToAddRoute(10L, "2020-05-01", "no", createModel()));
    }

    @Test
    public void testEdit() {
        assertEquals("menu", mainController.edit("2020-05-01", "no", createModel())); // Нет ПЛ на эту дату
    //    String string = mainController.primaryInput("2020-05-01", 1L, 10D, 40D, 10L, 100L, 50L, 10D, 10D, createModel());
        List<Addresses> address = Collections.singletonList(new Addresses("Мурино", "Девяткино", 10L));
        RouteSheet routeSheet = new RouteSheet(LocalDate.parse("2020-05-01"), 1L, 10D, 40D, 10L, 100L, 50L, 10D, 10D, 0D, 100L, address);

        routeSheetRepo.save(routeSheet);
        assertEquals("menu", mainController.edit("2020-05-01", "no", createModel()));
    }

    @Test
    public void testEdit2() {
        assertEquals("addingRoutes", mainController.edit2(50L, "Мурино", "no", createModel()));
    }

    private Model createModel() {
        return new Model() {
            @Override
            public Model addAttribute(String s, Object o) {
                return null;
            }

            @Override
            public Model addAttribute(Object o) {
                return null;
            }

            @Override
            public Model addAllAttributes(Collection<?> collection) {
                return null;
            }

            @Override
            public Model addAllAttributes(Map<String, ?> map) {
                return null;
            }

            @Override
            public Model mergeAttributes(Map<String, ?> map) {
                return null;
            }

            @Override
            public boolean containsAttribute(String s) {
                return false;
            }

            @Override
            public Object getAttribute(String s) {
                return null;
            }

            @Override
            public Map<String, Object> asMap() {
                return null;
            }
        };
    }
}
