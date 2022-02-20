package com.example.finalproject.repository;

import com.example.finalproject.entity.ClothesB;
import com.example.finalproject.entity.ClothesG;
import com.example.finalproject.entity.Payrecord;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClothesBRepository extends CrudRepository<ClothesB,Long> {

    @Override
    List<ClothesB> findAll();

    List<ClothesB> findAll(Sort id);

    @Query(value = "select * from CLOTHESB where userid= :userid order by ID",
            nativeQuery = true)
    List<ClothesB> orderUser(@Param("userid") String userid);


    @Query(value = "select * from CLOTHESB where userid= :userid and CLOTHESID= :clothesid",
            nativeQuery = true)
    ClothesB existsClothes(@Param("userid") String userid,@Param("clothesid") Long clothesid);
}
