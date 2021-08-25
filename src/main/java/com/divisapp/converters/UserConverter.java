package com.divisapp.converters;

import com.divisapp.entities.User;
import com.divisapp.enums.Role;
import com.divisapp.errors.WebException;
import com.divisapp.models.UserModel;
import com.divisapp.repositories.UserRepository;
import java.util.ArrayList;
import org.springframework.beans.BeanUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("UserConverter")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserConverter extends Converter<UserModel, User> {

    private final UserRepository userRepository;

    @Override
    public User modelToEntity(UserModel model) throws WebException {

        User entity = new User();

        try {

            if (model.getId() != null && !model.getId().isEmpty()) {
                entity = userRepository.getById(model.getId());
            }
            
            List<Role> roles = entity.getRoles();
            BeanUtils.copyProperties(model, entity);
            entity.setRoles(roles);
            
        } catch (BeansException e) {
            e.printStackTrace();
            throw new WebException("No se pudo convertir el modelo: " + model + " a entidad.");
        }

        return entity;
    }

    @Override
    public UserModel entityToModel(User entity) throws WebException {

        UserModel model = new UserModel();

        try {

            BeanUtils.copyProperties(entity, model);

        } catch (BeansException e) {
            e.printStackTrace();
            throw new WebException("No se pudo convertir la entidad: " + entity + " a modelo.");
        }

        return model;
    }

    @Override
    public List<User> modelsToEntities(List<UserModel> models) throws WebException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<UserModel> entitiesToModels(List<User> entities) throws WebException {
        List<UserModel> models = new ArrayList<>();
        
        for (User entity : entities) {
            models.add(entityToModel(entity));
        }
        
        return models;
    }

}
