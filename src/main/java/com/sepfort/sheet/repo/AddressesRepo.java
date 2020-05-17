package com.sepfort.sheet.repo;

import com.sepfort.sheet.domain.Route;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressesRepo extends JpaRepository<Route, Long> {
}
