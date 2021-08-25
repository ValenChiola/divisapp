package com.divisapp.repositories;

import com.divisapp.entities.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    
    @Query("SELECT u FROM User u WHERE u.email LIKE :email")
    public User getByEmail(@Param("email") String email);

    @Query(value="SELECT * FROM User u "
            + "INNER JOIN user_roles ur ON ur.user_id = u.id WHERE ur.roles LIKE :role", nativeQuery = true)
    public List<User> getAllWithRole(@Param("role") String role);

    @Query(value="SELECT * FROM User u "
            + "INNER JOIN user_roles ur ON ur.user_id = u.id WHERE ur.roles LIKE :role AND (u.name LIKE :q OR u.surname LIKE :q OR u.email LIKE :q) ", nativeQuery = true)
    public List<User> getAllWithRole(@Param("role") String role, @Param("q") String q);
    
}
