package ra.security;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ra.model.entity.User;
import ra.model.reponsitory.UserReponsitory;
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserReponsitory userReponsitory;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userReponsitory.findByUserName(username);
        if(user == null){
            throw new UsernameNotFoundException(("User not found"));
        }
        return CustomUserDetails.mapUserToUserDetail(user);

    }
}
