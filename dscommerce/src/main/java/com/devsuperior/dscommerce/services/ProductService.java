package com.devsuperior.dscommerce.services;

import com.devsuperior.dscommerce.dto.ProductDTO;
import com.devsuperior.dscommerce.dto.ProductMinDTO;
import com.devsuperior.dscommerce.entities.Product;
import com.devsuperior.dscommerce.repositories.ProductRepository;
import com.devsuperior.dscommerce.services.exceptions.DatabaseException;
import com.devsuperior.dscommerce.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Transactional(readOnly = true)
    public Page<ProductDTO> findByName(String name, Pageable pageable) {
        var result = productRepository.searchByName(name, pageable);
        return result.map(r -> new ProductDTO(r));
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id){
        var product = productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Produto não encontrado"));
        return new ProductDTO(product);
    }

    @Transactional(readOnly = true)
    public Page<ProductMinDTO> findAll(String name, Pageable pageable){
        Page<Product> results = productRepository.searchByName(name, pageable);
        return results.map(p -> new ProductMinDTO(p));
    }

    @Transactional
    public ProductDTO insert(ProductDTO productDTO){
        productRepository.save(new Product(productDTO));
        return productDTO;
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO productDTO){
        try {
            Product product = productRepository.getReferenceById(id);
            dtoToProduct(product, productDTO);
            product = productRepository.save(product);
            return new ProductDTO(product);
        }
        catch (EntityNotFoundException e){
            throw new ResourceNotFoundException("Produto não encontrado");
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Recurso não encontrado");
        }
        try {
            productRepository.deleteById(id);
        }
        catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Falha de integridade referencial");
        }
    }





    private void dtoToProduct(Product product, ProductDTO productDTO) {
            if (productDTO == null || product == null) {
                throw new IllegalArgumentException("Product or ProductDTO cannot be null");
            }
            // Copia os campos do DTO para a entidade Product
            product.setName(productDTO.getName());
            product.setDescription(productDTO.getDescription());
            product.setPrice(productDTO.getPrice());
            product.setImgUrl(productDTO.getImgUrl());
        }

    }