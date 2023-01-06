package ru.netology.patient.medical;


import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoFileRepository;
import ru.netology.patient.repository.PatientInfoRepository;
import ru.netology.patient.service.alert.SendAlertService;
import ru.netology.patient.service.medical.MedicalServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MedicalServiceImplTest {
    MedicalServiceImpl medicalService;
    PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoFileRepository.class);
    SendAlertService sendAlertService = Mockito.mock(SendAlertService.class);

    @BeforeEach
    public void init() {
        System.out.println("Test started");
    }

    @BeforeAll
    public static void started() {
        System.out.println("Tests started");
    }

    @AfterEach
    public void finished() {
        System.out.println("Test completed");
    }


    @AfterAll
    public static void finishedAll() {
        System.out.println("Tests completed");
    }

    @ParameterizedTest
    @CsvSource({"156,80", "120,79"})
    public void checkBloodPressureTest(String high , String low){
        Mockito.when(patientInfoRepository.getById("id1")).thenReturn(new PatientInfo("Иван", "Петров", LocalDate.of(1980, 11, 26),
                new HealthInfo(new BigDecimal("36.65"), new BloodPressure(120, 80))));

        medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);
        medicalService.checkBloodPressure("id1", new BloodPressure(Integer.parseInt(high), Integer.parseInt(low)));
        Mockito.verify(sendAlertService).send(Mockito.any());
    }

    @ParameterizedTest
    @ValueSource(strings = {"35.14", "38.7"})
    public void checkTemperatureTest(BigDecimal temperature){
        Mockito.when(patientInfoRepository.getById("id1")).thenReturn(new PatientInfo("Иван", "Петров", LocalDate.of(1980, 11, 26),
                new HealthInfo(new BigDecimal("36.65"), new BloodPressure(120, 80))));
        medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);
        medicalService.checkTemperature("id1",temperature);
        Mockito.verify(sendAlertService).send(Mockito.any());
    }

    @Test
    public void checkTemperatureTest(){
        Mockito.when(patientInfoRepository.getById("id1")).thenReturn(new PatientInfo("Иван", "Петров", LocalDate.of(1980, 11, 26),
                new HealthInfo(new BigDecimal("36.65"), new BloodPressure(120, 80))));
        medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);
        medicalService.checkTemperature("id1",new BigDecimal(35.16));
        Mockito.verify(sendAlertService, Mockito.never()).send(Mockito.any());
    }
}
