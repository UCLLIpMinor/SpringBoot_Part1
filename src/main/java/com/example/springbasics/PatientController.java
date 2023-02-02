package com.example.springbasics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;

import java.util.List;

@Controller
public class PatientController {

    private static Logger LOGGER = LoggerFactory.getLogger(PatientController.class);
    @Autowired
    private PatientRepository patientRepository;

    @GetMapping("/overview")
    public String overview (Model model) {
        // PagingAndSortingRepository (which is part of JpaRepository)
        // model.addAttribute("patients", patientRepository.findAll(Sort.by("name").ascending()));
        List<Patient> allPatients = patientRepository.findAll();

        if (allPatients.isEmpty()) {
            Patient elke = new Patient();
            elke.setName("Elke");
            elke.setEmail("elke.steegmans@ucll.be");
            elke.setAge(44);
            Patient miyo = new Patient();
            miyo.setName("Miyo");
            miyo.setEmail("miyo.vandenende@ucll.be");

            // CrudRepository (which is part of JpaRepository)
            patientRepository.save(elke);
            patientRepository.save(miyo);

            allPatients = patientRepository.findAll();
        }

        model.addAttribute("patients", allPatients);
        return "overview-patient";
    }

    @GetMapping("/add")
    public String add(Patient patient) {
        return "add-patient";
    }

    @PostMapping("/add")
    public String add(@Valid Patient patient, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "add-patient";
        }

        patientRepository.save(patient);
        return "redirect:/overview";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") long id, Model model) {
        try {
            Patient patient = patientRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Patient not found with id " + id));
            model.addAttribute(patient);
        } catch (IllegalArgumentException exc) {
            model.addAttribute("error", exc.getMessage());
        }

        return "update-patient";
    }

    @PostMapping("/update/{id}")
    public String update (@PathVariable("id") long id, @Valid Patient patient, BindingResult result, Model model) {
        if (result.hasErrors()) {
            LOGGER.error("ERRORS UPDATING");

            patient.setId(id);
            model.addAttribute("patient", patient);
            return "update-patient";
        }

        patientRepository.save(patient);
        return "redirect:/overview";
    }
}
