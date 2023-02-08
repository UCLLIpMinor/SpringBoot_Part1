package com.example.springbasics.patient.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface PatientRepository extends JpaRepository<Patient, Long> {}
