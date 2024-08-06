package com.example.talent_man.services;

import com.example.talent_man.models.succession.SuccessionDrivers;

import java.util.List;

public interface SuccessionDriverService {
    List<SuccessionDrivers> getAllSuccessionDrivers();
    SuccessionDrivers getSuccessionDriverById(int id);
    SuccessionDrivers createSuccessionDriver(SuccessionDrivers successionDriver);
    SuccessionDrivers updateSuccessionDriver(int id, SuccessionDrivers successionDriver);
    void deleteSuccessionDriver(int id);
}
