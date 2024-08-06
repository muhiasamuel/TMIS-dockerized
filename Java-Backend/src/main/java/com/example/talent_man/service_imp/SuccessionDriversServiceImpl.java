package com.example.talent_man.service_imp;

import com.example.talent_man.models.succession.SuccessionDrivers;
import com.example.talent_man.repos.SuccessionDriverRepo;
import com.example.talent_man.services.SuccessionDriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SuccessionDriversServiceImpl implements SuccessionDriverService {

    @Autowired
    private SuccessionDriverRepo successionDriversRepository;

    @Override
    public List<SuccessionDrivers> getAllSuccessionDrivers() {
        return successionDriversRepository.findAll();
    }

    @Override
    public SuccessionDrivers getSuccessionDriverById(int id) {
        return successionDriversRepository.findById(id).orElse(null);
    }

    @Override
    public SuccessionDrivers createSuccessionDriver(SuccessionDrivers successionDriver) {
        return successionDriversRepository.save(successionDriver);
    }

    @Override
    public SuccessionDrivers updateSuccessionDriver(int id, SuccessionDrivers successionDriver) {
        if (successionDriversRepository.existsById(id)) {
            successionDriver.setDriverId(id);
            return successionDriversRepository.save(successionDriver);
        } else {
            return null;
        }
    }

    @Override
    public void deleteSuccessionDriver(int id) {
        successionDriversRepository.deleteById(id);
    }
}
