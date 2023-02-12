package com.petspace.dev.repository;

import com.petspace.dev.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CategoryRepository extends JpaRepository<Category, Long>{

        List<Category> findAll();

}
