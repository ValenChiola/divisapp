package com.divisapp.models;

import com.divisapp.enums.Role;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.ElementCollection;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;

@Data
public class UserModel implements Serializable {

    private static final long serialVersionUID = 123456789L;

    private String id;
    private String name;
    private String surname;
    private String userPhone;
    private String motherPhone;
    private String fatherPhone;
    private String adress;
    private String email;
    private String password;
    private List<Role> roles;

}
