package com.sepfort.sheet.controller;

import com.sepfort.sheet.Application;
import com.sepfort.sheet.domain.RouteSheet;
import com.sepfort.sheet.repo.AddressesRepo;
import com.sepfort.sheet.repo.RouteSheetRepo;
import com.sepfort.sheet.repo.UserRepo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@ComponentScan(basePackages = "com.sepfort.sheet")
@SpringBootTest(classes = {Application.class,})
public class PrimaryInputTest {
    @Autowired
    private MainController mainController;

    @MockBean
    private RouteSheetRepo routeSheetRepo;
    @MockBean
    private AddressesRepo addressesRepo;
    @MockBean
    private UserRepo userRepo;

    @Before
    public void before() {
//        doNothing().when(routeSheetRepo).save(new RouteSheet());

    }

    @Test
    public void testPrimaryInput() {
//        String test = mainController.primaryInput()
    }
}
