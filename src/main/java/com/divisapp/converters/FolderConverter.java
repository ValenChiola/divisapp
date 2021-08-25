package com.divisapp.converters;

import com.divisapp.entities.Folder;
import com.divisapp.entities.User;
import com.divisapp.errors.WebException;
import com.divisapp.models.FolderModel;
import com.divisapp.repositories.FolderRepository;
import com.divisapp.services.UserService;
import java.util.ArrayList;
import org.springframework.beans.BeanUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("FolderConverter")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FolderConverter extends Converter<FolderModel, Folder> {

    private final FolderRepository folderRepository;
    private final UserService userService;

    @Override
    public Folder modelToEntity(FolderModel model) throws WebException {

        Folder entity = new Folder();

        try {

            if (model.getId() != null && !model.getId().isEmpty()) {
                entity = folderRepository.getById(model.getId());
            }

            BeanUtils.copyProperties(model, entity);

            User user = userService.getById(model.getUserId());
            entity.setUser(user);

            if (model.getParent() != null) {
                if (!model.getParent().getId().equals(model.getId())) {
                    Folder parent = modelToEntity(model.getParent());
                    entity.setParent(parent);
                }
            }

        } catch (BeansException e) {
            e.printStackTrace();
            throw new WebException("No se pudo convertir el modelo: " + model + " a entidad.");
        }

        return entity;
    }

    @Override
    public FolderModel entityToModel(Folder entity) throws WebException {
        FolderModel model = new FolderModel();

        try {

            BeanUtils.copyProperties(entity, model);

            model.setUserId(entity.getUser().getId());

            if (entity.getParent() != null) {
                if (!entity.getParent().getId().equals(entity.getId())) {
                    FolderModel parent = entityToModel(entity.getParent());
                    model.setParent(parent);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new WebException("No se pudo convertir la entidad: " + entity + " a modelo.");
        }

        return model;
    }

    @Override
    public List<Folder> modelsToEntities(List<FolderModel> models) throws WebException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<FolderModel> entitiesToModels(List<Folder> entities) throws WebException {
        List<FolderModel> models = new ArrayList<>();

        for (Folder entity : entities) {
            models.add(entityToModel(entity));
        }

        return models;
    }

}
