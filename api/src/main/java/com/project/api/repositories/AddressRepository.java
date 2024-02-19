package com.project.api.repositories;

import com.project.api.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "addresses", path="addresses")
@CrossOrigin({"http://localhost:4200", "http://localhost:4300"})
public interface AddressRepository extends JpaRepository<Address, Integer> {

    @Query("SELECT a FROM Address a JOIN a.accounts acc WHERE acc.id = :accountId")
    List<Address> findAddressesByAccountId(int accountId);

}
