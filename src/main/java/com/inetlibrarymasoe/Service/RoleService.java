package com.inetlibrarymasoe.Service;



import com.inetlibrarymasoe.Entity.Role;
import com.inetlibrarymasoe.Repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;
    public Role findByName(String roleName) {
        return roleRepository.findByName(roleName);
    }
    public List<Role> findAll() {
        return roleRepository.findAll();
    }


}
