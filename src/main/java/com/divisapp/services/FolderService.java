package com.divisapp.services;

import com.divisapp.converters.FolderConverter;
import com.divisapp.entities.File;
import com.divisapp.entities.Folder;
import com.divisapp.errors.WebException;
import com.divisapp.models.FolderModel;
import com.divisapp.repositories.FolderRepository;
import java.util.Date;
import java.util.List;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FolderService {
    
    private final FolderConverter folderConverter;
    private final FolderRepository folderRepository;
    private final FileService fileService;
    private final List<Folder> childs;
    
    @Transactional(rollbackOn = {WebException.class, Exception.class})
    public Folder save(FolderModel model) throws WebException {
        
        Folder entity = folderConverter.modelToEntity(model);
        
        if (entity.getCreatedAt() == null) {
            entity.setCreatedAt(new Date());
        }
        
        return folderRepository.save(entity);
    }
    
    @Async
    @Transactional(rollbackOn = {WebException.class, Exception.class})
    public Folder deleteById(String id) throws WebException {
        Folder folder = getById(id);
        
        deleteContent(id);
        
        folderRepository.delete(folder);
        
        return folder;
    }
    
    @Async
    @Transactional(rollbackOn = {WebException.class, Exception.class})
    public void delete(Folder folder) throws WebException {
        deleteContent(folder.getId());
        folderRepository.delete(folder);
    }

    @Async
    @Transactional(rollbackOn = {WebException.class, Exception.class})
    public void deleteContent(String id) throws WebException {
        
        fileService.deleteByFolder(id);
        
        List<Folder> childs = getAllChilds(id);
        System.out.println(childs.size());

        for (Folder child : childs) {
            fileService.deleteByFolder(child.getId());
        }
        
        folderRepository.deleteAll(childs);
    }
    
    public Folder levelUp(Folder folder) {
        if (folder.getParent() == null) {
            return folder;
        }
        
        folder.setParent(folder.getParent().getParent());
        
        return folderRepository.save(folder);
    }
    
    @Async
    public List<Folder> getAllChilds(String id) throws WebException {
        List<Folder> childs = getAllFolderByParent(id);
        this.childs.addAll(childs);
        
        for (Folder child : childs) {
            this.childs.addAll(getAllChilds(child.getId()));
        }
        
        return this.childs;
    }
    
    public Folder getById(String id) throws WebException {
        return folderRepository.getById(id);
    }
    
    public FolderModel getModelById(String id) throws WebException {
        return folderConverter.entityToModel(folderRepository.getById(id));
    }
    
    
    public List<Folder> getAllByUser(String userId) throws WebException {
        return folderRepository.getAllByUser(userId);
    }
    
    public List<FolderModel> getAllModelByUser(String userId) throws WebException {
        return folderConverter.entitiesToModels(getAllByUser(userId));
    }
    
    public List<Folder> getAllFolderByParent(String folderId) throws WebException {
        return folderRepository.getAllFolderByParent(folderId);
    }
    
    public List<FolderModel> getAllFolderModelByParent(String folderId) throws WebException {
        return folderConverter.entitiesToModels(getAllFolderByParent(folderId));
    }
    
}
