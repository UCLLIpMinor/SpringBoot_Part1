package com.example.springbasics.patient.web;

import com.example.springbasics.patient.domain.Patient;
import com.example.springbasics.patient.domain.PatientService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class PatientController {

    private static Logger LOGGER = LoggerFactory.getLogger(PatientController.class);

    private final PatientService patientService;

    @Autowired
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping("/overview")
    public String overview (Model model) {
        List<Patient> allPatients = patientService.getPatients();

        if (allPatients.isEmpty()) {
            createSampleData();
            allPatients = patientService.getPatients();
        }

        model.addAttribute("patients", allPatients);
        return "overview-patient";
    }

    private void createSampleData() {
        PatientDto bram = new PatientDto();
        bram.setName("Bram");
        bram.setEmail("bram.vanimpe@ucll.be");
        bram.setAge(32);

        PatientDto elke = new PatientDto();
        elke.setName("Greetje");
        elke.setEmail("greetje.jongen@ucll.be");

        patientService.createPatient(bram);
        patientService.createPatient(elke);
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("patientDto", new PatientDto());
        return "add-patient";
    }

    @PostMapping("/add")
    public String add(@Valid PatientDto patient, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("patientDto", patient);
            return "add-patient";
        }

        patientService.createPatient(patient);
        return "redirect:/overview";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") long id, Model model) {
        try {
            Patient patient = patientService.getPatient(id);
            model.addAttribute("patientDto", toDto(patient));
        } catch (RuntimeException exc) {
            model.addAttribute("error", exc.getMessage());
        }

        return "update-patient";
    }

    private PatientDto toDto(Patient patient) {
        PatientDto dto = new PatientDto();

        dto.setId(patient.getId());
        dto.setName(patient.getName());
        dto.setEmail(patient.getEmail());
        dto.setAge(patient.getAge());

        return dto;
    }

    @PostMapping("/update/{id}")
    public String update (@PathVariable("id") long id, @Valid PatientDto patient, BindingResult result, Model model) {
        if (result.hasErrors()) {
            LOGGER.error("ERRORS UPDATING");

            patient.setId(id);
            model.addAttribute("patientDto", patient);
            return "update-patient";
        }

        patientService.updatePatient(id, patient);
        return "redirect:/overview";
    }
}
