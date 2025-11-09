package org.startlight.awsome.repository;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.startlight.awsome.bean.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    // 이름으로 필터링
    Page<User> findByNameContaining(String name, Pageable pageable);

    // 날짜 범위로 필터링
    Page<User> findByJoinDateBetween(Date startDate, Date endDate,
            Pageable pageable);

    // 이름과 날짜 범위로 필터링
    @Query("SELECT u FROM User u WHERE (:name IS NULL OR u.name LIKE %:name%) AND (:startDate IS NULL OR u.joinDate >= :startDate) AND (:endDate IS NULL OR u.joinDate <= :endDate)")
    Page<User> findByNameContainingAndJoinDateBetween(
            @Param("name") String name, @Param("startDate") Date startDate,
            @Param("endDate") Date endDate, Pageable pageable);
}
