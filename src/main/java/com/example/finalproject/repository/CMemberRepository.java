package com.example.finalproject.repository;

import com.example.finalproject.entity.CMember;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CMemberRepository extends CrudRepository<CMember,String> {

    @Override
    List<CMember> findAll();


    List<CMember> findAll(Sort id);
}
