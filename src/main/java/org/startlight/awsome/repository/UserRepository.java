package org.startlight.awsome.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.startlight.awsome.bean.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {}
