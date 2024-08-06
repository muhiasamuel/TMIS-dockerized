package com.example.talent_man.repos.user;
import com.example.talent_man.models.user.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<Otp, Long> {
    Optional<Otp> findByUsernameAndOtp(String username, String otp);
    void deleteByUsername(String username);
}