package co.LabsProjects.customerwebsite.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable=false)
    private String username;

    @JsonIgnore
    private String password;

    @Column(nullable=false)
    @Builder.Default
    private boolean accountNonExpired = true;

    @Column(nullable=false)
    @Builder.Default
    private boolean accountNonLocked = true;

    @Column(nullable=false)
    @Builder.Default
    private boolean credentialsNonExpired = true;

    @Column(nullable=false)
    @Builder.Default
    private boolean enabled = true;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_authority",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> authorities;

    @OneToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    // Constructor for normal users - linked to a customer account
    public User(String username, String password, List<Role> authorities, Customer customer){
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.customer = customer;
        accountNonExpired = true;
        accountNonLocked = true;
        credentialsNonExpired = true;
        enabled = true;
    }

    // Constructor for admin users - don't need to be linked to a customer account
    public User(String username, String password, List<Role> authorities){
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        accountNonExpired = true;
        accountNonLocked = true;
        credentialsNonExpired = true;
        enabled = true;
    }

}
