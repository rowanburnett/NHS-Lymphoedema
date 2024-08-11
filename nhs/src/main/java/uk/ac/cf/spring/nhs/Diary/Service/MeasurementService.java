package uk.ac.cf.spring.nhs.Diary.Service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import uk.ac.cf.spring.nhs.Diary.DTO.MeasurementDTO;
import uk.ac.cf.spring.nhs.Diary.Entity.Measurement;
import uk.ac.cf.spring.nhs.Diary.Repository.MeasurementRepository;

@Service
public class MeasurementService {
    private final MeasurementRepository measurementRepository;

    public MeasurementService(MeasurementRepository measurementRepository) {
        this.measurementRepository = measurementRepository;
    }

    @Transactional
    public Measurement saveMeasurement(MeasurementDTO measurementDTO, long userId) {
        Measurement measurement = new Measurement();
        measurement.setUserId(userId);
        measurement.setType(measurementDTO.getType());
        measurement.setValue(measurementDTO.getValue());
        measurement.setUnit(measurementDTO.getUnit());

        return measurementRepository.save(measurement);
    }
}
