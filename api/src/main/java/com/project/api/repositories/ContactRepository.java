package com.project.api.repositories;

import com.project.api.entities.Cart;
import com.project.api.entities.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "contacts", path="contacts")
public interface ContactRepository extends JpaRepository<Contact, Integer> {
}
