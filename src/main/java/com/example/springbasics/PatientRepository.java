package com.example.springbasics;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    List<Patient> findAllByEmail(String email);

    @Query("SELECT p FROM Patient p WHERE p.age>18")
    List<Patient> findAllAdults ();
}
