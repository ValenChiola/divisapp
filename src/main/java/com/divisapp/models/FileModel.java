package com.divisapp.models;

import com.divisapp.entities.User;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class FileModel implements Serializable {

    private static final long serialVersionUID = 123456789L;

    private String id;
    private String name;
    private String mime;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] content;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    private UserModel user;

}
