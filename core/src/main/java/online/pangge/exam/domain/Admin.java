package online.pangge.exam.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class Admin extends BaseDomain{
    private String name;

    private String username;

    private String password;

    private Boolean isSuperAdmin;

    private List<Role> roles = new ArrayList<>();
}