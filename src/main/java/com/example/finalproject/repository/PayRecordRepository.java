package com.example.finalproject.repository;

import com.example.finalproject.entity.Payrecord;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PayRecordRepository extends CrudRepository<Payrecord,Long> {

    @Override
    List<Payrecord> findAll();


    List<Payrecord> findAll(Sort id);
}
