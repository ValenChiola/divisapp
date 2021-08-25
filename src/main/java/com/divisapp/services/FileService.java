package com.divisapp.services;

import com.divisapp.converters.FileConverter;
import com.divisapp.entities.File;
import com.divisapp.entities.Folder;
import com.divisapp.errors.WebException;
import com.divisapp.models.FileModel;
import com.divisapp.repositories.FileRepository;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FileService {

    private final FileRepository fileRepository;
    private final FileConverter fileConverter;

    @Transactional(rollbackOn = {WebException.class, Exception.class})
    public File save(FileModel model) throws WebException {

        validateModel(model);
        File file = fileConverter.modelToEntity(model);

        if (file.getCreatedAt() == null) {
            file.setCreatedAt(new Date());
        }

        return fileRepository.save(file);
    }

    @Transactional(rollbackOn = {WebException.class, Exception.class})
    public File save(File file) throws WebException {

        if (file.getCreatedAt() == null) {
            file.setCreatedAt(new Date());
        }

        return fileRepository.save(file);
    }

    @Transactional(rollbackOn = {WebException.class, Exception.class})
    public void deleteByFolder(String id) throws WebException {
        List<File> files = getAllByFolder(id);
        for (File file : files) {
            Folder fileFolder = file.getFolder();
            if (fileFolder != null) {
                List<File> folderFiles = getAllByFolder(fileFolder.getId());
                deleteAll(folderFiles);
            }
            delete(file);
        }
    }

    public File getById(String id) {
        return fileRepository.getById(id);
    }

    public List<File> getAllByFolder(String folderId) throws WebException {
        return fileRepository.getAllByFolder(folderId);
    }

    public List<FileModel> getAllModelByFolder(String folderId) throws WebException {
        return fileConverter.entitiesToModels(getAllByFolder(folderId));
    }

    public List<File> getAllByUser(String userId) throws WebException {
        return fileRepository.getAllByUser(userId);
    }

    public List<FileModel> getAllModelByUser(String userId) throws WebException {
        return fileConverter.entitiesToModels(fileRepository.getAllByUser(userId));
    }

    public void deleteById(String id) {
        fileRepository.deleteById(id);
    }

    public void delete(File file) {
        fileRepository.delete(file);
    }

    public void deleteAll(List<File> files) {
        fileRepository.deleteAll(files);
    }

    private void validateModel(FileModel model) throws WebException {

        if (dataIsWrong(model.getMime())) {
            throw new WebException("La mime no puede estar vacía.");
        }
        if (dataIsWrong(model.getName())) {
            throw new WebException("El nombre no puede estar vacío.");
        }
        if (dataIsWrong(Arrays.toString(model.getContent()))) {
            throw new WebException("Error al cargar la foto. (vacía)");
        }

    }

    private boolean dataIsWrong(String s) {
        return s == null || s.isEmpty();
    }

}
