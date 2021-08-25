package com.divisapp.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Data
public class Folder {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    private String name;

    @Temporal(TemporalType.DATE)
    private Date createdAt;

    @ManyToOne
    private User user;

    @ManyToOne
    private Folder parent;

public List<Folder> getGenericTree() {
        List<Folder> genericTree = new ArrayList<>();

        Folder thisParent = parent;

        if (thisParent == null) {
            return genericTree;
        }

        while (true) {
            genericTree.add(thisParent);
            
            Folder parentOfThisParent = thisParent.getParent();
            if (parentOfThisParent == null) {
                break;
            }

            thisParent = parentOfThisParent;
            
        }
        
        Collections.reverse(genericTree);

        return genericTree;
    }

}
