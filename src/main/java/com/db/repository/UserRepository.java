package com.db.repository;

import com.db.POJO.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    public User findByUsernameAndPassword(String username, String password);
    public User findByUsername(String username);
    public User findByUID(String UID);
}
