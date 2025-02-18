package tw.musicplat.tools;


import tw.musicplat.config.MyUserDetail;
import tw.musicplat.model.dto.UserDTO;
import tw.musicplat.model.entity.User;

public class DTOConvertUtil {
    public static UserDTO toUserDTO(User user) {
        if (user == null) {
            return null;
        }

        String role = user.getRoles().iterator().next().getRoleName();

        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getGender(),
                user.getBirthday(),
                user.getEmail(),
                user.getPhone(),
                user.getAddress(),
                user.getPhoto(),
                user.getEnabled(),
                user.getCreated(),
                user.getModified(),
                role
        );
    }
    public static UserDTO toUserDTO(MyUserDetail myUserDetail) {
        if (myUserDetail == null) {
            return null;
        }

        String role = null;
        if (!myUserDetail.getAuthorities().isEmpty()) {
            role = myUserDetail.getAuthorities().iterator().next().getAuthority();
        }

        return new UserDTO(
                myUserDetail.getId(),
                myUserDetail.getUsername(),
                myUserDetail.getGender(),
                myUserDetail.getBirthday(),
                myUserDetail.getEmail(),
                myUserDetail.getPhone(),
                myUserDetail.getAddress(),
                myUserDetail.getPhoto(),
                myUserDetail.isEnabled(),
                myUserDetail.getCreated(),
                myUserDetail.getModified(),
                role
        );
    }
}
