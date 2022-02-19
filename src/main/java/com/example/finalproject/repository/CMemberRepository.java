package com.example.finalproject.repository;

import com.example.finalproject.entity.CMember;
import com.example.finalproject.entity.ClothesB;
import com.example.finalproject.entity.Payrecord;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CMemberRepository extends CrudRepository<CMember,String> {

    @Override
    List<CMember> findAll();
    List<CMember> findAll(Sort id);
}
