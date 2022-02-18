package com.example.finalproject.repository;

import com.example.finalproject.entity.Clothes;
import com.example.finalproject.entity.ClothesG;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ClothesGRepository extends CrudRepository<ClothesG,Long> {

    @Override
    List<ClothesG> findAll();


    List<ClothesG> findAll(Sort id);
}
