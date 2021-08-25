package com.divisapp.entities;

import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;


@Data
@Entity
public class File {
    
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    private String name;
    private String mime;
    
    @Lob@Basic(fetch = FetchType.LAZY)
    private byte[] content;
    
    @Temporal(TemporalType.DATE)
    private Date createdAt;
    
    @ManyToOne
    private User user;
    
    @ManyToOne
    private Folder folder;
    
}
