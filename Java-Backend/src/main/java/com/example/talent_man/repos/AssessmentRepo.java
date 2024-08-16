package com.example.talent_man.repos;

import com.example.talent_man.models.Assessment;
import com.example.talent_man.models.PotentialAttribute;
import com.example.talent_man.models.UserQuestionAnswer;
import com.example.talent_man.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Repository
public interface AssessmentRepo extends JpaRepository<Assessment, Integer> {
    Set<Assessment> findByUsers(User user);

    List<Assessment> findByPotentialAttributes(PotentialAttribute potentialAttribute);

    @Query(value = "SELECT * FROM assessments a WHERE a.end_date > :now", nativeQuery = true)
    List<Assessment> findActiveAssessments(@Param("now") LocalDate now);
}
