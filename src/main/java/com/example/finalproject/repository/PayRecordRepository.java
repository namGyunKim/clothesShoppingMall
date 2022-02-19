package com.example.finalproject.repository;

import com.example.finalproject.entity.ClothesB;
import com.example.finalproject.entity.Payrecord;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PayRecordRepository extends CrudRepository<Payrecord,Long> {

    @Override
    List<Payrecord> findAll();


    List<Payrecord> findAll(Sort id);

    @Query(value = "select * from PAYRECORD where userid= :userid",
            nativeQuery = true)
    List<Payrecord> orderUser(@Param("userid") String userid);
}
