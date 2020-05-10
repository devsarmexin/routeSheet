package com.sepfort.sheet.service;

import org.springframework.ui.Model;

import java.io.IOException;

public interface CreateWaybill {
    String createWaybill(String data, Model model) throws IOException;
}
