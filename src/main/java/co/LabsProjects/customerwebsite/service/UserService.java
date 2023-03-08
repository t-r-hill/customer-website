package co.LabsProjects.customerwebsite.service;

import co.LabsProjects.customerwebsite.model.Customer;
import co.LabsProjects.customerwebsite.model.Role;
import co.LabsProjects.customerwebsite.model.User;
import co.LabsProjects.customerwebsite.repo.RoleRepository;
import co.LabsProjects.customerwebsite.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    CustomerService customerService;

    @Autowired
    PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.getByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException("Could not find user with username " + username)
        );
    }

    public User createNewUser(User user){
        if (user.getCustomer() != null){
            Customer customer = user.getCustomer();
            customerService.saveCustomer(customer);
        }
        return createUser(user);
    }

    // Helper
    public User createUser(User user) {
        checkPassword(user.getPassword());
        Role userRole = roleRepository.getByRole(Role.Roles.USER_ROLE);
        user.setAuthorities(Collections.singletonList(userRole));
        user.setPassword(encoder.encode(user.getPassword()));
        try {
            return userRepository.save(user);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e.getCause());
        }
    }

    private void checkPassword(String password) {
        if (password == null) {
            throw new IllegalStateException("You must set a password");
        }
        if (password.length() < 6) {
            throw new IllegalStateException("Password is too short. Must be longer than 6 characters");
        }
    }
}
