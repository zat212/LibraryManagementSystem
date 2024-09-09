package com.inetlibrarymasoe.Service;


import com.inetlibrarymasoe.Entity.User;
import com.inetlibrarymasoe.Repository.RoleRepository;
import com.inetlibrarymasoe.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@Service
public class UserService {
    @Autowired
    UserRepository userRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
    }

    public List<User> findAll() {
        return userRepo.findAll();
    }

    public User findByEmail(String email) {
        return userRepo.findByEmail(email).get();
    }


    public List<User> getUnverifiedUsers() {
        return userRepo.findByVerifyStatusIsNull();
    }


    public void verifyMemberProcess(Long id) {
        Optional<User> userOpt = userRepo.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setVerifyStatus("Verified");
            userRepo.save(user);
        }
    }


    public List<User> findByKeyword(String keyword) {
        return userRepo.findByKeyword(keyword);
    }


    public void updateUser(long id, User updatedUser) {

        User existingUser = userRepo.findById(id).orElseThrow(() -> new NoSuchElementException("User not found"));
        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setPhone(updatedUser.getPhone());
        existingUser.setAddress(updatedUser.getAddress());
        userRepo.save(existingUser);
    }

    public Optional<User> findById(long id) {
        return userRepo.findById(id);
    }


}