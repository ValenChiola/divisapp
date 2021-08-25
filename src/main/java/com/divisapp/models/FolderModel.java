package com.divisapp.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class FolderModel implements Serializable {

    private static final long serialVersionUID = 123456789L;

    private String id;
    private String name;
    private String userId;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    private FolderModel parent;

    public List<FolderModel> getGenericTree() {
        List<FolderModel> genericTree = new ArrayList<>();

        FolderModel thisParent = parent;

        if (thisParent == null) {
            return genericTree;
        }

        while (true) {
            genericTree.add(thisParent);
            
            FolderModel parentOfThisParent = thisParent.getParent();
            if (parentOfThisParent == null) {
                break;
            }

            thisParent = parentOfThisParent;
            
        }
        
        Collections.reverse(genericTree);

        return genericTree;
    }

}
