package com.example.springbasics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import javax.validation.Valid;

@Controller
public class PatientController {

    @Autowired
    private PatientRepository patientRepository;

    @GetMapping("/overview")
    public String overview (Model model) {
        Patient elke = new Patient();
        elke.setName("Elke");
        elke.setEmail("elke.steegmans@ucll.be");
        elke.setAge(44);
        Patient miyo = new Patient();
        miyo.setName("Miyo");
        miyo.setEmail("miyo.vandenende@ucll.be");
        patientRepository.save(elke);
        patientRepository.save(miyo);
        // CrudRepository (which is part of JpaRepository)
         model.addAttribute("patients", patientRepository.findAll());
        // PagingAndSortingRepository (which is part of JpaRepository)
        //model.addAttribute("patients", patientRepository.findAll(Sort.by("name").ascending()));
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
            Patient patient = patientRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Patient not found with id" + id));
            model.addAttribute(patient);
        }
        catch (IllegalArgumentException exc) {
            model.addAttribute("error", exc.getMessage());
        }
        return "update-patient";
    }

    @PostMapping("/update/{id}")
    public String update (@PathVariable("id") long id, @Valid Patient patient, BindingResult result, Model model) {
        if (result.hasErrors()) {
            System.out.println("ERRORS UPDATING");
            patient.setId(id);
            model.addAttribute("patient", patient);
            return "update-patient";
        }
        patientRepository.save(patient);
        return "redirect:/overview";
    }
}
