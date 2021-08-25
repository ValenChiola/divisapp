package com.divisapp.services;

import com.divisapp.converters.UserConverter;
import com.divisapp.entities.User;
import com.divisapp.enums.Role;
import com.divisapp.errors.WebException;
import com.divisapp.models.UserModel;
import com.divisapp.repositories.UserRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserService implements UserDetailsService {

    private final UserConverter userConverter;
    private final UserRepository userRepository;

    @Transactional(rollbackOn = {WebException.class, Exception.class})
    public User save(UserModel model) throws WebException {

        validateModel(model);
        User user = userConverter.modelToEntity(model);
        completeUser(user, model);

        return userRepository.save(user);
    }

    public User getById(String id) {
        return userRepository.getById(id);
    }

    public UserModel getModelById(String id) throws WebException {
        return userConverter.entityToModel(userRepository.getById(id));
    }

    public User getByEmail(String email) {
        return userRepository.getByEmail(email);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }
    
    public Page<User> getAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public List<User> getAllWithRole(Role role) {
        return userRepository.getAllWithRole(role.toString());
    }

    public List<User> getAllWithRole(Role role, String q) {
        return userRepository.getAllWithRole(role.toString(), "%" + q + "%");
    }

    private void completeUser(User user, UserModel model) {

        if (user.getRoles() == null) {
            user.setRoles(new ArrayList<>());
        }

        if (user.getCreatedAt() == null) {
            user.setCreatedAt(new Date());
            user.getRoles().add(Role.STUDENT);
        }

        String encriptedPassword = new BCryptPasswordEncoder().encode(model.getPassword());
        user.setPassword(encriptedPassword);

    }

    private void validateModel(UserModel model) throws WebException {

        if (dataIsWrong(model.getName())) {
            throw new WebException("Tu nombre no puede estar vacío.");
        }
        if (dataIsWrong(model.getSurname())) {
            throw new WebException("Tu apellido no puede estar vacío.");
        }
        if (dataIsWrong(model.getEmail())) {
            throw new WebException("Tu E-mail no puede estar vacío.");
        }
        if (dataIsWrong(model.getPassword())) {
            throw new WebException("Tu contraseña no puede estar vacía.");
        }
        if (dataIsWrong(model.getAdress())) {
            throw new WebException("Tu domicilio no puede estar vacío.");
        }
        if (dataIsWrong(model.getMotherPhone())) {
            throw new WebException("El teléfono de tu mamá no puede estar vacío.");
        }

        User user = userRepository.getByEmail(model.getEmail());

        if (user != null) {
            if (user.getEmail().equals(model.getEmail())) {
                if (!user.getId().equals(model.getId())) {
                    throw new WebException("Ya hay un usuario con el mail: " + user.getEmail());
                }
            }
        }

    }

    private boolean dataIsWrong(String s) {
        return s == null || s.isEmpty();
    }

    @Override
    public UserDetails loadUserByUsername(String email) {

        User user = getByEmail(email);
        System.out.println(user);
        if (user == null) {
            return null;
        }

        List<GrantedAuthority> permisos = new ArrayList<>();

        for (Role role : user.getRoles()) {
            permisos.add(new SimpleGrantedAuthority("ROLE_" + role.toString()));
        }

        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession(true);
        session.setAttribute("usersession", user);

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), permisos);

    }

}
