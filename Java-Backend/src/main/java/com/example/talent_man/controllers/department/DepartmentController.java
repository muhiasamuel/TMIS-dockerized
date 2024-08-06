package com.example.talent_man.controllers.department;

import com.example.talent_man.models.Department;
import com.example.talent_man.models.Position;
import com.example.talent_man.repos.DepartmentRepo;
import com.example.talent_man.service_imp.DepServiceImp;
import com.example.talent_man.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController()
@RequestMapping("/v1/api/departments")
public class DepartmentController {
    @Autowired
    private DepServiceImp dService;

    @PostMapping("/addDepartment")
    public ApiResponse<Department> addDepartment(@RequestBody DepDto dep){
        try{
            if(dep.getDepName().isEmpty()){
                return new ApiResponse<>(301, "department should have a name");
            }else if(dep.getPositionList().isEmpty()){
                return new ApiResponse<>(301, "department should have a positions");
            }else{
                Department department = new Department();
                department.setDepName(dep.getDepName());
                Set<Position> positions = new HashSet<>();

                for (PositionDto p: dep.getPositionList()){
                    if(p.getPName() == null){
                        return new ApiResponse<>(301, "department position should have a name");
                    }
                    Position position = new Position();
                    position.setPositionName(p.getPName());
                    positions.add(position);
                }
                department.setDepartmentPositions(positions);
                Department dbDep = dService.addDepartment(department);
                ApiResponse<Department> response = new ApiResponse<>(200, "successful");
                response.setItem(dbDep);
                return response;
            }
        }catch(Exception e){
            return new ApiResponse<>(500, e.getMessage());
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
            Set<Position> positionList = new HashSet<>();

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
    public ApiResponse<Set<Position>> getDepartmentPositions(@RequestParam(value = "depId") Integer depId){
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
                    Set<Position> positionList = department.getDepartmentPositions();
                    ApiResponse<Set<Position>> response = new ApiResponse<>(200, "successful");
                    response.setItem(positionList);
                    return response;
                }

            }
        }catch(Exception e){
            return new ApiResponse<>(500, e.getMessage());
        }
    }

}
