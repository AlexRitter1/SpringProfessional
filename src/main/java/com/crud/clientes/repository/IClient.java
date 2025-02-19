package com.crud.clientes.repository;

import com.crud.clientes.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IClient extends JpaRepository<Client, Long> {
}
