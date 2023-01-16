package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.User;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public interface UserDao {

    List<User> findAll();

    List<String> listAllUsernames();

    User findByUsername(String username);

    int findIdByUsername(String username);

    boolean create(String username, String password);
}
