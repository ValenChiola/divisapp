package com.divisapp.converters;

import com.divisapp.entities.File;
import com.divisapp.errors.WebException;
import com.divisapp.models.FileModel;
import com.divisapp.repositories.FileRepository;
import java.io.IOException;
import java.util.ArrayList;
import org.springframework.beans.BeanUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component("FileConverter")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FileConverter extends Converter<FileModel, File> {

    private final FileRepository fileRepository;

    @Override
    public File modelToEntity(FileModel model) throws WebException {

        File entity = new File();
        
        try {
        
            if (model.getId() != null && !model.getId().isEmpty()) {
                entity = fileRepository.getById(model.getId());
            }

            BeanUtils.copyProperties(model, entity);

        } catch (BeansException e) {
            e.printStackTrace();
            throw new WebException("No se pudo convertir el modelo: " + model + " a entidad.");
        }

        return entity;
    }

    @Override
    public FileModel entityToModel(File entity) throws WebException {
        FileModel model = new FileModel();
        
        try {
            
            BeanUtils.copyProperties(entity, model);
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new WebException("No se pudo convertir la entidad: " + entity + " a modelo.");
        }
        
        return model;
    }

    @Override
    public List<File> modelsToEntities(List<FileModel> models) throws WebException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<FileModel> entitiesToModels(List<File> entities) throws WebException {
        List<FileModel> models = new ArrayList<>();
        
        for (File entity : entities) {
            models.add(entityToModel(entity));
        }
        
        return models;
    }
    
    public File multipartFileToFile(MultipartFile multipartFile) throws IOException {
        
        File file = new File();
        
        file.setContent(multipartFile.getBytes());
        file.setName(multipartFile.getOriginalFilename());
        file.setMime(multipartFile.getContentType());
        
        return file;
    }
    
    public List<File> multipartFilesToFiles(List<MultipartFile> multipartFiles) throws IOException {
        
        List<File> files = new ArrayList<>();
        
        for (MultipartFile multipartFile : multipartFiles) {
            files.add(multipartFileToFile(multipartFile));
        }
        
        return files;
    }

}
