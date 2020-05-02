package com.sepfort.sheet.repo;

import com.sepfort.sheet.domain.Addresses;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressesRepo extends JpaRepository<Addresses, Long> {
}
