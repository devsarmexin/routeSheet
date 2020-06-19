package com.sepfort.sheet.service;

import org.springframework.ui.Model;

import java.io.IOException;

public interface CreateWaybill {
    /** @noinspection checkstyle:MissingJavadocMethod*/
    String createWaybill(String data, Model model) throws IOException;
}
