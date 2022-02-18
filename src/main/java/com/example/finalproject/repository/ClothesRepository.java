package com.example.finalproject.repository;

import com.example.finalproject.entity.CMember;
import com.example.finalproject.entity.Clothes;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ClothesRepository extends CrudRepository<Clothes,Long> {

    @Override
    List<Clothes> findAll();


    List<Clothes> findAll(Sort id);
}
