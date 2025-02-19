package com.crud.clientes.service;

import com.crud.clientes.DTO.ClientDTO;
import com.crud.clientes.models.Client;
import com.crud.clientes.repository.IClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;

@Service
public class ClientService {

    @Autowired
    private IClient repository;

    @Transactional(readOnly = true)
    public ClientDTO findById(Long id){
        var client = repository.findById(id);
        var client2 = client.get();
        return new ClientDTO(client2);
    }

    @Transactional(readOnly = true)
    public Page<ClientDTO> findAll(Pageable pageable){
        Page<Client> results = repository.findAll(pageable);
        return results.map(ClientDTO::new);
    }

    @Transactional
    public ClientDTO insert(ClientDTO clientDTO){
        var c = repository.save(new Client(clientDTO));
        return new ClientDTO(c);
    }

    @Transactional
    public ClientDTO update(Long id, ClientDTO clientDTO){
            Client c = repository.getReferenceById(id);
            dtoToClient(c, clientDTO);
            c = repository.save(c);
            return new ClientDTO(c);
        }

    private void dtoToClient(Client c, ClientDTO clientDTO) {
        c.setName(clientDTO.getName());
        c.setCpf(clientDTO.getCpf());
        c.setIncome(clientDTO.getIncome());
        c.setBirthDate(clientDTO.getBirthDate());
        c.setChildren(clientDTO.getChildren());
    }


    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        }
    }
}


