package com.testproject.catalog.repositories;

import com.testproject.catalog.entities.User;
import com.testproject.catalog.projections.UserNameProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    @Query(nativeQuery = true, value = "SELECT email "
            + "FROM tb_user "
            + "WHERE id = :userId")
    UserNameProjection search1(int userId);
}
