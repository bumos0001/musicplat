package tw.musicplat.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name = "users")
public class User {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, unique = true, columnDefinition = "NVARCHAR(255)")
    @NotBlank
    private String username;

    @Column(name = "password", nullable = false, columnDefinition = "NVARCHAR(255)")
    @Length(min = 6)
    @NotBlank
    private String password;

    @Column(name = "gender", columnDefinition = "NVARCHAR(50)")
    private String gender;

    @Column(name = "birthday", columnDefinition = "DATE")
    @Past
    private Date birthday;

    @Column(name = "email", nullable = false, unique = true, columnDefinition = "NVARCHAR(255)")
    @Email
    private String email;

    @Column(name = "phone", columnDefinition = "NVARCHAR(20)")
    private String phone;

    @Column(name = "address", columnDefinition = "NVARCHAR(255)")
    private String address;

    @Column(name = "photo", columnDefinition = "NVARCHAR(255)")
    private String photo;

    @Column(name = "enabled", nullable = false, columnDefinition = "BIT")
    private Boolean enabled;

    @Column(name = "created_date", nullable = false, columnDefinition = "DATETIME2")
    private Date created;

    @Column(name = "modified_date", nullable = false, columnDefinition = "DATETIME2")
    private Date modified;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(name = "user_role", joinColumns = {
            @JoinColumn(name = "fk_user_id", referencedColumnName = "user_id")}, inverseJoinColumns = {
            @JoinColumn(name = "fk_role_id", referencedColumnName = "role_id")})
    private Set<Role> roles = new HashSet<Role>();

    @PrePersist
    protected void onCreate() {
        created = new Date();
        modified = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        modified = new Date();
    }
}
