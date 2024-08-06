package com.example.talent_man.controllers.succession;

import com.example.talent_man.models.succession.SuccessionDrivers;
import com.example.talent_man.services.SuccessionDriverService;
import com.example.talent_man.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/successionDrivers")
public class SuccessionDriversController {

    @Autowired
    private SuccessionDriverService successionDriversService;

    @GetMapping
    public ApiResponse<List<SuccessionDrivers>> getAllSuccessionDrivers() {
        List<SuccessionDrivers> drivers = successionDriversService.getAllSuccessionDrivers();
        ApiResponse res = new ApiResponse<>();
        res.setMessage("Succession drivers retrieved successfully");
        res.setStatus(200);
        res.setItem(drivers);
        return res;
    }

    @GetMapping("/{id}")
    public ApiResponse<SuccessionDrivers> getSuccessionDriverById(@PathVariable int id) {
        SuccessionDrivers driver = successionDriversService.getSuccessionDriverById(id);
        ApiResponse res = new ApiResponse<>();
        res.setMessage("Succession driver found successfully");
        res.setStatus(200);
        res.setItem(driver);
        return driver != null ? res : new ApiResponse<>(404, "not found");
    }

    @PostMapping
    public ApiResponse<SuccessionDrivers> createSuccessionDriver(@RequestBody SuccessionDrivers successionDriver) {
        SuccessionDrivers createdDriver = successionDriversService.createSuccessionDriver(successionDriver);
        ApiResponse res = new ApiResponse<>();
        res.setMessage("Succession driver added successfully");
        res.setStatus(200);
        res.setItem(createdDriver);
        return res;
    }

    @PutMapping("/{id}")
    public ApiResponse<SuccessionDrivers> updateSuccessionDriver(@PathVariable int id, @RequestBody SuccessionDrivers successionDriver) {
        SuccessionDrivers updatedDriver = successionDriversService.updateSuccessionDriver(id, successionDriver);
        ApiResponse res = new ApiResponse<>();
        res.setMessage("Succession driver Updated successfully");
        res.setStatus(200);
        res.setItem(updatedDriver);
        return updatedDriver != null ? res : new ApiResponse<>(404, "not found");
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteSuccessionDriver(@PathVariable int id) {
        successionDriversService.deleteSuccessionDriver(id);
        ApiResponse res = new ApiResponse<>();
        res.setMessage("Succession driver deleted successfully");
        res.setStatus(200);
        return res;
    }
}