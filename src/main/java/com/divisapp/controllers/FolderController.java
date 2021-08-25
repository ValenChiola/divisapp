package com.divisapp.controllers;

import com.divisapp.entities.Folder;
import com.divisapp.errors.WebException;
import com.divisapp.models.FileModel;
import com.divisapp.models.FolderModel;
import com.divisapp.models.UserModel;
import com.divisapp.services.EmailService;
import com.divisapp.services.FileService;
import com.divisapp.services.FolderService;
import com.divisapp.services.UserService;
import com.divisapp.utils.Redirect;
import static com.divisapp.utils.Texts.*;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/folder")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FolderController {

    private final FolderService folderService;
    private final UserService userService;
    private final FileService fileService;
    private final EmailService emailService;

    @GetMapping("/{folderId}")
    public ModelAndView folder(@PathVariable String folderId) {

        ModelAndView model = new ModelAndView(FOLDER_VIEW_LABEL);

        try {

            FolderModel folderModel = folderService.getModelById(folderId);
            UserModel userModel = userService.getModelById(folderModel.getUserId());
            List<FileModel> files = fileService.getAllModelByFolder(folderId);
            List<FolderModel> folders = folderService.getAllFolderModelByParent(folderId);

            model.addObject(FOLDER_LABEL, folderModel);
            model.addObject(USER_LABEL, userModel);
            model.addObject(FILES_LABEL, files);
            model.addObject(FOLDERS_LABEL, folders);

        } catch (WebException e) {
            e.printStackTrace();
            model.addObject(ERROR, "No se ha encontrado la carpeta solicitada.");
        } catch (Exception e) {        
            e.printStackTrace();
            model.addObject(ERROR, "No se ha encontrado la carpeta solicitada.");
        }
        
        return model;
    }

    @PostMapping("/save")
    public String save(RedirectAttributes attr, @ModelAttribute(FOLDER_LABEL) FolderModel folderModel,
            @RequestParam String userId, @RequestParam(required = false) String parentFolderId) {

        Folder folder = null;
        
        folderModel.setUserId(userId);

        try {

            if (parentFolderId != null) {
                folderModel.setParent(folderService.getModelById(parentFolderId));
            }

            folder = folderService.save(folderModel);

        } catch (WebException e) {
            e.printStackTrace();
            attr.addFlashAttribute(ERROR, "No se pudo guardar la carpeta.");
        }

        return Redirect.getRedirectUrl(folder);
    }

    @PostMapping("/edit")
    public String edit(RedirectAttributes attr, @RequestParam String folderId, @RequestParam String folderName) {

        Folder folder = null;
        
        try {

            FolderModel folderModel = folderService.getModelById(folderId);
            folderModel.setName(folderName);
            folder = folderService.save(folderModel);

        } catch (WebException e) {
            e.printStackTrace();
            attr.addFlashAttribute(ERROR, "No se pudo editar la carpeta");
        }
        
        return Redirect.getRedirectUrl(folder);
    }

    @PostMapping("/delete")
    public String delete(RedirectAttributes attr, @RequestParam String folderId) {
        Folder folder = null;

        try {

            folder = folderService.deleteById(folderId);

        } catch (WebException e) {
            e.printStackTrace();
            attr.addFlashAttribute(ERROR, "No se pudo eliminar la carpeta");
        }

        return "redirect:/user/profile/" + folder.getUser().getId();
    }
    
    @PostMapping("/share")
    public String share(@RequestParam String email, @RequestParam String folderId) throws WebException {
        
        Folder folder = folderService.getById(folderId);
        
        emailService.exportFolder(email, folder);
        
        return Redirect.getRedirectUrl(folder);
    }
    
    @PostMapping("/level-up")
    public String levelUp(@RequestParam String folderId) throws WebException {
        
        Folder folder = folderService.getById(folderId);
        folderService.levelUp(folder);
        
        return Redirect.getRedirectUrl(folder);
    }

}
