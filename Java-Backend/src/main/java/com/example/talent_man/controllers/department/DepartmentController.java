package com.example.talent_man.controllers.department;

import com.example.talent_man.models.Department;
import com.example.talent_man.models.Position;
import com.example.talent_man.repos.DepartmentRepo;
import com.example.talent_man.service_imp.DepServiceImp;
import com.example.talent_man.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController()
@RequestMapping("/v1/api/departments")
public class DepartmentController {
    @Autowired
    private DepServiceImp dService;


    @PostMapping
    public ResponseEntity<Department> createDepartment(@RequestBody DepDto depDto) {
        Department createdDepartment = dService.createDepartment(depDto);
        return new ResponseEntity<>(createdDepartment, HttpStatus.CREATED);
    }
    @PostMapping("/addDepartment")
    public ResponseEntity<ApiResponse<Department>> addDepartment(@RequestBody DepDto depDto) {
        // Validate Department Name
        if (depDto.getDepName() == null || depDto.getDepName().isEmpty()) {
            return new ResponseEntity<>(new ApiResponse<>(301, "Department should have a name"), HttpStatus.BAD_REQUEST);
        }

        // Validate Positions List
        if (depDto.getPositionList() == null || depDto.getPositionList().isEmpty()) {
            return new ResponseEntity<>(new ApiResponse<>(301, "Department should have positions"), HttpStatus.BAD_REQUEST);
        }

        // Validate Each Position Name
        for (PositionDto p : depDto.getPositionList()) {
            System.out.println(p);
            if (p.getPName() == null || p.getPName().isEmpty()) {
                return new ResponseEntity<>(new ApiResponse<>(301, "Department position should have a name"), HttpStatus.BAD_REQUEST);
            }
        }

        try {
            // Create the Department with validated data
            Department createdDepartment = dService.createDepartment(depDto);
            ApiResponse<Department> response = new ApiResponse<>(200, "Successful");
            response.setItem(createdDepartment);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            // Handle any exceptions that occur during the creation
            return new ResponseEntity<>(new ApiResponse<>(500, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/addDepartmentPositions")
    public ApiResponse<Department> addDepartmentPositions(@RequestParam("depId") Integer depId, @RequestBody List<PositionDto> positions){
        if(depId == null || depId == 0){
            return new ApiResponse<>(301, "Please enter a valid id");
        }else if(dService.findById(depId) == null){
            return new ApiResponse<>(301, "No department found with the given id");
        }
        else if (positions == null || positions.isEmpty()) {
            return new ApiResponse<>(301, "Please enter valid positions");
        }else{
            List<Position> positionList = new ArrayList<>();

            for (PositionDto p: positions){
                if(p.getPName() == null || p.getPName().equals("")){
                    return new ApiResponse<>(301, "Please enter valid position name");
                }
                Position position = new Position();
                position.setPositionName(p.getPName());
                positionList.add(position);
            }
            Department dep = dService.findById(depId);
            if(!dep.getDepartmentPositions().isEmpty()){
                for (Position p: positionList){
                    dep.getDepartmentPositions().add(p);
                }
            }else{
                dep.setDepartmentPositions(positionList);
            }

            Department dbDep = dService.addDepartment(dep);
            ApiResponse<Department> response = new ApiResponse<>(200, "successful");
            response.setItem(dbDep);
            return response;
        }
    }


    @PutMapping("/{departmentId}/addLeader/{leaderId}")
    public ResponseEntity<ApiResponse<Department>> addLeaderToDepartment(
            @PathVariable int departmentId, @PathVariable int leaderId) {
        try {
            Department updatedDepartment = dService.addLeaderToDepartment(departmentId, leaderId);
            ApiResponse<Department> response = new ApiResponse<>(200, "Leader added successfully");
            response.setItem(updatedDepartment);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse<>(500, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/getAllDepartments")
    public ApiResponse<List<Department>> getAlDepartments(){
        try{
            if(dService.getAllDepartments().isEmpty()){
                return new ApiResponse<>(301, "We currently don't have any departments");

            }else{
                List<Department> departments = dService.getAllDepartments();
                ApiResponse<List<Department>> response = new ApiResponse<>(200, "successful");
                response.setItem(departments);
                return response;
            }
        }catch(Exception e){
            return new ApiResponse<>(500, e.getMessage());
        }
    }

    @GetMapping("/getDepartmentPositions")
    public ApiResponse<List<Position>> getDepartmentPositions(@RequestParam(value = "depId") Integer depId){
        try{
            if(depId == null || depId == 0){
                return new ApiResponse<>(301, "Please enter a valid id");
            } else if (dService.findById(depId) == null ) {
                return new ApiResponse<>(301, "No department found with the given id");
            }else{
                Department department = dService.findById(depId);
                if(department.getDepartmentPositions() == null || department.getDepartmentPositions().isEmpty()){
                    return new ApiResponse<>(301, "No positions found with the given department");
                }else{
                    List<Position> positionList = department.getDepartmentPositions();
                    ApiResponse<List<Position>> response = new ApiResponse<>(200, "successful");
                    response.setItem(positionList);
                    return response;
                }

            }
        }catch(Exception e){
            return new ApiResponse<>(500, e.getMessage());
        }
    }

}
