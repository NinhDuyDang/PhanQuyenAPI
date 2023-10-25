package ra.payload.request;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.Set;

public class SignupRequest {
    private String userName;
    private String password;
    private String email;
    private String phone;
    @DateTimeFormat(pattern = "dd/MM/yyy")
    private Date create;
    private boolean userStatus;
    private Set<String> ListRoles;

    public SignupRequest(){
        
    }

    public SignupRequest(String userName, String password, String email, String phone, boolean userStatus, Set<String> listRoles) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.userStatus = userStatus;
        ListRoles = listRoles;


    }



    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getCreate() {
        return create;
    }

    public void setCreate(Date create) {
        this.create = create;
    }

    public boolean isUserStatus() {
        return userStatus;
    }

    public void setUserStatus(boolean userStatus) {
        this.userStatus = userStatus;
    }

    public Set<String> getListRoles() {
        return ListRoles;
    }

    public void setListRoles(Set<String> listRoles) {
        ListRoles = listRoles;
    }
}
