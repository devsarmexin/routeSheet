package com.sepfort.sheet.mapper;

import com.sepfort.sheet.domain.RouteSheet;
import com.sepfort.sheet.dto.RouteSheetDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RouteSheetMapper {
    @Autowired
    private ModelMapper modelMapper;

    public RouteSheetDto convert(RouteSheet routeSheet) {
        return modelMapper.map(routeSheet, RouteSheetDto.class);
    }

    public RouteSheet revert(RouteSheetDto routeSheetDto) {
        return modelMapper.map(routeSheetDto, RouteSheet.class);
    }
}
