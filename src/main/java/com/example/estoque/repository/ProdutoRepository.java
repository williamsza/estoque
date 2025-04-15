package com.example.estoque.repository;


import com.example.estoque.entity.ProdutoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface ProdutoRepository extends JpaRepository<ProdutoEntity, Long> {

    public ProdutoEntity findByNome(String nome);

}
