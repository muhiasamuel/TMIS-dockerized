package com.example.talent_man.service_imp;

import com.example.talent_man.controllers.department.DepDto;
import com.example.talent_man.controllers.department.PositionDto;
import com.example.talent_man.models.Department;
import com.example.talent_man.models.Position;
import com.example.talent_man.repos.DepartmentRepo;
import com.example.talent_man.services.DepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class DepServiceImp implements DepService {
    @Autowired
    private DepartmentRepo repo;

    @Override
    public Department addDepartment(Department dep) {
        return repo.save(dep);
    }

    @Override
    public Department findById(int depId) {
        return repo.getReferenceById(depId);
    }

    @Override
    public List<Department> getAllDepartments() {
        return repo.findAll();
    }

    @Transactional
    @Override
    public Department createDepartment(DepDto depDto) {
        Department department = new Department();
        department.setDepName(depDto.getDepName());

        List<Position> positions = new ArrayList<>();
        for (PositionDto positionDto : depDto.getPositionList()) {
            Position position = new Position();
            position.setPositionName(positionDto.getPName());
            position.setDepartment(department);  // Set the back reference
            positions.add(position);
        }
        department.setDepartmentPositions(positions); // Convert List to Set

        // Log positions and department before saving
        System.out.println("Positions to be saved: " + positions);
        System.out.println("Department to be saved: " + department);

        // Save department and positions together
        Department savedDepartment = repo.save(department);

        // Log the saved department to ensure it was saved correctly
        System.out.println("Saved Department: " + savedDepartment);

        return savedDepartment;
    }

}
