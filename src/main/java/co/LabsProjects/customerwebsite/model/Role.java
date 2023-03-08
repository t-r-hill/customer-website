package co.LabsProjects.customerwebsite.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Roles role;

    public Role(Roles role) {
        this.role = role;
    }

    @JsonIgnore
    public String getAuthority() {
        return role.name();
    }

    public enum Roles{
        USER_ROLE,
        ADMIN_ROLE
    }

}


