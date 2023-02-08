package com.example.springbasics.patient.domain;

import com.example.springbasics.patient.web.PatientDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    public List<Patient> getPatients() {
        // PagingAndSortingRepository (which is part of JpaRepository)
        // model.addAttribute("patients", patientRepository.findAll(Sort.by("name").ascending()));
        return patientRepository.findAll();
    }

    public Patient getPatient(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient with id " + id + " not found"));
    }

    public Patient createPatient(PatientDto dto) {
        Patient patient = new Patient();

        patient.setName(dto.getName());
        patient.setEmail(dto.getEmail());
        patient.setAge(dto.getAge());

        // CrudRepository (which is part of JpaRepository)
        return patientRepository.save(patient);
    }

    public Patient updatePatient(Long id, PatientDto dto) {
        Patient patient = getPatient(id);

        patient.setName(dto.getName());
        patient.setEmail(dto.getEmail());
        patient.setAge(dto.getAge());

        return patientRepository.save(patient);
    }
}
