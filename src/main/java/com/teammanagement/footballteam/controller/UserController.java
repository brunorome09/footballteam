package com.teammanagement.footballteam.controller;
import com.teammanagement.footballteam.model.User;
import com.teammanagement.footballteam.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/equipos")
public class UserController {

    @Autowired(required = false)
    private UserRepo userRepo;

    @PostMapping()
    public ResponseEntity<User> addUser(@RequestBody User user) {
        try {
            User userSave = userRepo.save(user);
            return new ResponseEntity<>(userSave, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
