package com.example.talent_man.repos.succession;

import com.example.talent_man.models.succession.ReadyUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReadyUsersRepository extends JpaRepository<ReadyUsers, Integer> {
}