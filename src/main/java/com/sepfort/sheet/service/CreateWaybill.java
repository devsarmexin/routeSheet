package com.sepfort.sheet.service;

import org.springframework.ui.Model;

import java.io.IOException;

public interface CreateWaybill {

    /**
     * Creating a route sheet in Excel.
     *
     * @param data Date.
     * @param model Representation.
     * @return Return to the main menu.
     * @throws IOException
     */
    String createWaybill(String data, Model model) throws IOException;
}
