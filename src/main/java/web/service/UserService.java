package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import web.model.Role;
import web.model.User;
import web.model.VerificationToken;
import web.repository.ApartmentRepository;
import web.repository.RoleRepository;
import web.repository.UserRepository;
import web.repository.VerificationTokenRepository;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service("userService")
public class UserService implements CrudService<User>{

    private VerificationTokenRepository verificationTokenRepository;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder,
                       VerificationTokenRepository verificationTokenRepository) {
        this.userRepository = userRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public Page<User> findAllPaginated(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setPassword(user.getPassword());
        Role userRole = roleRepository.findByRole("GLOBAL ADMIN");
        user.setRoles(new HashSet<>(Collections.singletonList(userRole)));
        return userRepository.save(user);
    }

    public User saveOccupantUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setPassword(user.getPassword());
        Role userRole = roleRepository.findByRole("OCCUPANT");
        user.setRoles(new HashSet<>(Collections.singletonList(userRole)));
        return userRepository.save(user);
    }

    public User saveEmployeeUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setPassword(user.getPassword());
        Role userRole = roleRepository.findByRole("EMPLOYEE");
        user.setRoles(new HashSet<>(Collections.singletonList(userRole)));
        return userRepository.save(user);
    }

    @Override
    public void add(User entity) {
        userRepository.save(entity);
    }

    @Override
    public void update(User entity) {
        userRepository.save(entity);
    }

    @Override
    public User update(int id, User entity) {
        return null;
    }

    @Override
    public void delete(User entity) {
        userRepository.delete(entity);
    }

    @Override
    public void deleteById(int id) {

    }

    @Override
    public User getById(int id) {
        return userRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Invalid type Id:" + id));
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    public void createVerificationToken(User user, String token){
        VerificationToken myToken = new VerificationToken(token, user);
        verificationTokenRepository.save(myToken);
    }

    public VerificationToken getVerificationToken(String verificationToken){
        VerificationToken myVerificationToken = new VerificationToken();
        Optional<VerificationToken> token = verificationTokenRepository.findByToken(verificationToken);
        if(token.isPresent()){
            myVerificationToken = token.get();
        }
        return myVerificationToken;
    }
}
