package com.project.api.services;

import com.project.api.dtos.AddressDTO;
import com.project.api.entities.Address;

import java.util.List;

public interface AddressService {
    List<AddressDTO> findByAccountId(Integer accountId);

    Address save(Address address);


}
