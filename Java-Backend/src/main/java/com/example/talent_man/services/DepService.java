package com.example.talent_man.services;

import com.example.talent_man.models.Department;

import java.util.List;

public interface DepService {
    Department addDepartment(Department dep);
    Department findById(int depId);
    List<Department> getAllDepartments();
}
