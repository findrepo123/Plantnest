package com.project.api.services.impl;

import com.project.api.dtos.AddressDTO;
import com.project.api.entities.Address;
import com.project.api.repositories.AddressRepository;
import com.project.api.services.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Override
    public List<AddressDTO> findByAccountId(Integer accountId) {
        try {
            return this.addressRepository.findAddressesByAccountId(accountId).stream()
                    .map(AddressDTO::new).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public Address save(Address address) {
        return addressRepository.save(address);
    }

}
