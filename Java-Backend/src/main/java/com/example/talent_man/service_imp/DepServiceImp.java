package com.example.talent_man.service_imp;

import com.example.talent_man.models.Department;
import com.example.talent_man.repos.DepartmentRepo;
import com.example.talent_man.services.DepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
