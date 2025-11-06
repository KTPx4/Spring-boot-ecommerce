package ecommerce.infra.client.user.repository;

import ecommerce.infra.client.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserJpaRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String userName);
    boolean existsByUserName(String userName);
    
    @Query("SELECT u FROM User u WHERE u.userName = :username")
    Optional<User> findByUsername(@Param("username") String username);
}
