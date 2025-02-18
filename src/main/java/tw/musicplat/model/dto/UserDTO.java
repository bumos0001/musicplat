package tw.musicplat.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;

    private String username;

    private String gender;

    private Date birthday;

    private String email;

    private String phone;

    private String address;

    private String photo;

    private Boolean enabled;

    private Date created;

    private Date modified;
    private String role;
}
