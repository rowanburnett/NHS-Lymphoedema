package uk.ac.cf.spring.nhs.Provider.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uk.ac.cf.spring.nhs.Measurement.Entity.Measurement;
import uk.ac.cf.spring.nhs.Measurement.Service.MeasurementService;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/patientprofile/statistics")
public class PatientStatisticsController {

    private long userId = 2L;
    @Autowired
    MeasurementService measurementService;




}
