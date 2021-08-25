package com.divisapp.controllers;

import com.divisapp.converters.UserConverter;
import com.divisapp.entities.User;
import com.divisapp.errors.WebException;
import com.divisapp.models.FileModel;
import com.divisapp.models.FolderModel;
import com.divisapp.models.UserModel;
import com.divisapp.services.EmailService;
import com.divisapp.services.FileService;
import com.divisapp.services.FolderService;
import com.divisapp.services.UserService;
import static com.divisapp.utils.Texts.*;
import java.util.List;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserController {

    private final UserService userService;
    private final UserConverter userConverter;
    private final FileService fileService;
    private final FolderService folderService;
    private final EmailService emailService;

    @GetMapping("/form")
    public ModelAndView form(@RequestParam(required = false) String id) throws WebException {

        ModelAndView model = new ModelAndView(USER_FORM_LABEL);
        UserModel userModel = new UserModel();

        if (id != null) {
            userModel = userService.getModelById(id);
        }

        model.addObject(USER_LABEL, userModel);

        return model;
    }

    @PostMapping("/save")
    public ModelAndView save(@ModelAttribute(USER_LABEL) UserModel userModel,
            BindingResult result) {

        ModelAndView model = new ModelAndView(USER_FORM_LABEL);

        try {
            if (result.hasErrors()) {
                model.addObject(ERROR, result);
            } else {
                userService.save(userModel);
                model.setViewName(INDEX_LABEL);
            }
        } catch (WebException e) {
            loadModel(model, userModel, EDIT_LABEL);
            model.addObject(ERROR, e.getMessage());
        } catch (Exception e) {
            loadModel(model, userModel, EDIT_LABEL);
            model.addObject(ERROR, DEFAULT_ERROR_MESSAGE);
        }

        return model;
    }

    @GetMapping({"/profile", "/profile/{userId}"})
    public ModelAndView profile(HttpSession session, @PathVariable(required = false) String userId) {

        ModelAndView model = new ModelAndView(USER_PROFILE_LABEL);
        UserModel userModel = null;

        try {

            if (userId != null) {
                userModel = userService.getModelById(userId);
            } else {
                userModel = userConverter.entityToModel((User) session.getAttribute("usersession"));
            }

            //emailService.exportData(userConverter.modelToEntity(userModel));
            List<FileModel> files = fileService.getAllModelByUser(userModel.getId());
            List<FolderModel> folders = folderService.getAllModelByUser(userModel.getId());

            model.addObject(USER_LABEL, userModel);
            model.addObject(FOLDERS_LABEL, folders);
            model.addObject(FILES_LABEL, files);
            model.addObject(FOLDER_LABEL, new FolderModel());

        } catch (WebException e) {
            e.printStackTrace();
            model.addObject(ERROR, DEFAULT_ERROR_MESSAGE);
        }

        return model;
    }

    @GetMapping("/list")
    public ModelAndView list(@RequestParam(required = false) String q, @PageableDefault(size = 20)  Pageable pageable) {
        ModelAndView model = new ModelAndView(USER_LIST_LABEL);
//        List<User> users = null;
        try {

//            if (q != null) {
//                users = userService.getAllWithRole(Role.STUDENT, q);
//            } else {
//                users = userService.getAllWithRole(Role.STUDENT);
//            }
            Page<User> users = userService.getAll(pageable);

//            List<UserModel> userModels = userConverter.entitiesToModels(users);
            model.addObject("page", new PageImpl<>(userConverter.entitiesToModels(users.getContent())));

        } catch (WebException ex) {
            ex.printStackTrace();
        }

        return model;
    }

    private void loadModel(ModelAndView model, UserModel userModel, String action) {
        model.addObject(USER_LABEL, userModel);
        model.addObject(ACTION_LABEL, action);
    }

}
