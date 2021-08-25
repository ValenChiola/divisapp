package com.divisapp.controllers;

import com.divisapp.converters.FileConverter;
import com.divisapp.entities.File;
import com.divisapp.entities.Folder;
import com.divisapp.entities.User;
import com.divisapp.errors.WebException;
import com.divisapp.services.FileService;
import com.divisapp.services.FolderService;
import com.divisapp.services.UserService;
import com.divisapp.utils.MediaTypeConverter;
import static com.divisapp.utils.Texts.*;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/file")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FileController {

    private final FileConverter fileConverter;
    private final FileService fileService;
    private final UserService userService;
    private final FolderService folderService;

    @GetMapping("/upload/{userId}")
    public ModelAndView form(@PathVariable String userId) {

        ModelAndView model = new ModelAndView(FILE_FORM_LABEL);
        model.addObject(USER_LABEL, userService.getById(userId));

        return model;
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id) {
        
        File file = fileService.getById(id);
        fileService.delete(file);
        
        String url = "/user/profile" + file.getUser().getId();
        
        if (file.getFolder() != null) {
            url = "/folder/" + file.getFolder().getId();
        }
        
        return "redirect:" + url;
    }

    @GetMapping("/load/{fileId}")
    public ResponseEntity<byte[]> load(@PathVariable String fileId) {
        File file = fileService.getById(fileId);
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaTypeConverter.getMediaType(file.getMime()));
        return new ResponseEntity<>(file.getContent(), headers, HttpStatus.OK);
    }

    @PostMapping("/save")
    public String save(@RequestParam List<MultipartFile> files, @RequestParam String userId, @RequestParam(required = false) String folderId) {

        String url = "/user/profile/" + userId;

        User user = userService.getById(userId);
        Folder folder = null;
        try {

            if (folderId != null && !folderId.isEmpty()) {
                folder = folderService.getById(folderId);
                url = "/folder/" + folderId;
            }

            for (MultipartFile multipartFile : files) {
                if (multipartFile.isEmpty()) continue;
                File file = fileConverter.multiparFileToFile(multipartFile);
                file.setUser(user);
                file.setFolder(folder);
                fileService.save(file);
            }

        } catch (IOException | WebException e) {
            e.printStackTrace();
        }

        return "redirect:" + url;
    }

}
