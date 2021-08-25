package com.divisapp.services;

import com.divisapp.entities.File;
import com.divisapp.entities.Folder;
import com.divisapp.entities.User;
import com.divisapp.errors.WebException;
import static com.divisapp.utils.Texts.BASE_URL;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class EmailService {

    private final FolderService folderService;
    private final FileService fileService;

    @Async
    public void exportData(User user) throws WebException {

        List<File> allFiles = fileService.getAllByUser(user.getId());
        List<Folder> folders = folderService.getAllByUser(user.getId());

        for (Folder folder : folders) {
            allFiles.addAll(fileService.getAllByFolder(folder.getId()));
            for (Folder child : folderService.getAllFolderByParent(folder.getId())) {
                allFiles.addAll(fileService.getAllByFolder(child.getId()));
            }
        }

        System.out.println("Legajo de " + user.getName() + " " + user.getSurname());
        String info;
        for (File file : allFiles) {
            info = "";
            for (Folder folder : file.getFolder().getGenericTree()) {
                info = info.concat(folder.getName() + " > ");
            }
            info = info.concat(file.getFolder().getName() + " - " + BASE_URL + "/file/load/" + file.getId());
            System.out.println(info);
        }
    }

    @Async
    public void exportFolder(String email, Folder f) throws WebException {
        List<File> allFiles = new ArrayList<>();
        allFiles.addAll(fileService.getAllByFolder(f.getId()));
        
        for (Folder child : folderService.getAllFolderByParent(f.getId())) {
            allFiles.addAll(fileService.getAllByFolder(child.getId()));
        }

        System.out.println("Compartiendo con " + email);
        
        String info;
        for (File file : allFiles) {
            info = "";
            for (Folder folder : file.getFolder().getGenericTree()) {
                info = info.concat(folder.getName() + " > ");
            }
            info = info.concat(file.getFolder().getName() + " - " + BASE_URL + "/file/load/" + file.getId());
            System.out.println(info);
        }
    }

}
