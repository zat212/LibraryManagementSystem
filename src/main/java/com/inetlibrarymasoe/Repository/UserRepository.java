package com.inetlibrarymasoe.Repository;

import com.inetlibrarymasoe.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findById(Long id);

    List<User> findByVerifyStatusIsNull();
    @Query("SELECT u FROM User u WHERE u.fullName LIKE %?1% OR u.role.name LIKE %?1% OR u.email LIKE %?1% OR u.phone LIKE %?1% OR str(u.id) LIKE %?1%")
    List<User> findByKeyword(String keyword);
}
