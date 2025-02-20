package tw.musicplat.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "role_name", nullable = false, unique = true, columnDefinition = "NVARCHAR(100)")
    private String roleName;

    @Column(name = "role_description", columnDefinition = "NVARCHAR(255)")
    private String roleDescription;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<User>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(name = "role_permission", joinColumns = {
            @JoinColumn(name = "fk_role_id", referencedColumnName = "role_id") }, inverseJoinColumns = {
            @JoinColumn(name = "fk_permission_id", referencedColumnName = "permission_id") })
    private Set<Permission> permissions = new HashSet<Permission>();
}
