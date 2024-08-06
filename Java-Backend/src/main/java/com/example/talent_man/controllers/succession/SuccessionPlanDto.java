package com.example.talent_man.controllers.succession;

import com.example.talent_man.models.Department;
import com.example.talent_man.models.Position;
import com.example.talent_man.models.succession.ReadyUsers;
import com.example.talent_man.models.succession.SuccessionDrivers;
import com.example.talent_man.models.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuccessionPlanDto implements Serializable {
    private Department department;
    private List<SuccessionDrivers> drivers;
    private List<User> managerUsers;
    private List<Position> positions;
    private List<ReadyUsers> readyNow;
    private List<ReadyUsers> readyAfterTwoYears;
    private List<ReadyUsers> readyMoreTwoYears;
}
