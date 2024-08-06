package com.example.talent_man.repos;

import com.example.talent_man.models.Assessment;
import com.example.talent_man.models.UserQuestionAnswer;
import com.example.talent_man.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface AssessmentRepo extends JpaRepository<Assessment, Integer> {
    Set<Assessment> findByUsers(User user);
}
