package ra.controller;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import ra.jwt.JwtTokenProvider;
import ra.model.entity.ERole;
import ra.model.entity.Roles;
import ra.model.entity.User;
import ra.model.service.RolesService;
import ra.model.service.UserService;
import ra.payload.reponse.JwtReponse;
import ra.payload.reponse.MessageReponse;
import ra.payload.request.LoginRequest;
import ra.payload.request.SignupRequest;
import ra.security.CustomUserDetails;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin("*")
@Controller
@RequestMapping("api/v1/auth/**")
public class UserController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private UserService userService;
    @Autowired
    private RolesService rolesService;
    @Autowired
    private PasswordEncoder encoder;

    public boolean errSignup = false;

    @GetMapping("/signup")
    public String showSignupPage(Model model){
        if(errSignup == true){
            model.addAttribute("errSignup", "Username or Email is existed!");
        }
        model.addAttribute("user", new User());
        return "register";
    }

//    @PostMapping("/signup")
//    public String registerUser(@RequestBody User user) {
////         if (userService.existsByUserName(signupRequest.getUserName())) {
////             return ResponseEntity.badRequest().body(new MessageReponse("Error:Username is already"));
////         }
////         if (userService.existsByEmail(signupRequest.getEmail())) {
////             return ResponseEntity.badRequest().body(new MessageReponse("Error:Mail is already"));
////         }
//
//            if (userService.existsByUserName(user.getUserName())) {
//        // Return a message or redirect with an error message
//        return "redirect:/signup?error=username";
//    }
//
//    if (userService.existsByEmail(user.getEmail())) {
//        // Return a message or redirect with an error message
//        return "redirect:/signup?error=email";
//    }
//        User user1 = new User();
//        user1.setUserName(user.getUserName());
//        user1.setPassword(encoder.encode(user.getPassword()));
//        user1.setEmail(user.getEmail());
//        user1.setPhone(user.getPhone());
//        user1.setUserStatus(true);
//
//        //created ngày
//        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//        Date dateNow = new Date();
//        String strNow = sdf.format(dateNow);
//        try {
//            user1.setCreated(sdf.parse(strNow));
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
//        Set<Roles> strRoles = user.getListRoles();
//        Set<Roles> listRoles = new HashSet<>();
//        if (strRoles == null) {
//            // Lấy vai trò người dùng mặc định
//            Roles userRole = rolesService.findByRoleName(ERole.ROLE_USER).orElseThrow(() -> new RuntimeException("Default user role not found"));
//            listRoles.add(userRole);
//        }
//
//        else {
//            strRoles.forEach(role->{
//                if (role.equals("admin")) {
//                    Roles admin = rolesService.findByRoleName(ERole.ROLE_ADMIN)
//                            .orElseThrow(() -> new RuntimeException("Error is not found "));
//                    listRoles.add(admin);
//                } else if (role.equals("moderator")) {
//                    Roles modRole = rolesService.findByRoleName(ERole.ROLE_MODERATOR)
//                            .orElseThrow(() -> new RuntimeException("Error is not found"));
//                    listRoles.add(modRole);
//                } else if (role.equals("user")) {
//                    Roles userRole = rolesService.findByRoleName(ERole.ROLE_USER)
//                            .orElseThrow(() -> new RuntimeException("Error is not found"));
//                    listRoles.add(userRole);
//                }
//            });
//
//        }
//        user.setListRoles(listRoles);
//        userService.saveOrUpdate(user);
//
//        return "redirect:/api/v1/auth/signin";
//    }


    @PostMapping("/signup")
    public String registerUser(@ModelAttribute User user) { // Use @ModelAttribute instead of @RequestBody

        if (userService.existsByUserName(user.getUserName()) || userService.existsByEmail(user.getEmail())) {
            // Redirect with an error message
            errSignup = true;
            return "redirect:/api/v1/auth/signup";
        }

        // if (userService.existsByEmail(user.getEmail())) {
        //     // Redirect with an error message
        //     errEmail = true;
        //     return "redirect:/api/v1/auth/signup";
        // }

        User user1 = new User();
        user1.setUserName(user.getUserName());
        user1.setPassword(encoder.encode(user.getPassword()));
        user1.setEmail(user.getEmail());
        user1.setPhone(user.getPhone());
        user1.setUserStatus(true);

        // Created ngày
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date dateNow = new Date();
        String strNow = sdf.format(dateNow);
        try {
            user1.setCreated(sdf.parse(strNow));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Set<Roles> strRoles = user.getListRoles();
        Set<Roles> listRoles = new HashSet<>();
        if (strRoles == null) {
            // Lấy vai trò người dùng mặc định
            Roles userRole = rolesService.findByRoleName(ERole.ROLE_USER).orElseThrow(() -> new RuntimeException("Default user role not found"));
            listRoles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                if (role.getRoleName().equals("admin")) { // Use getRoleName() to compare role names
                    Roles admin = rolesService.findByRoleName(ERole.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Error is not found"));
                    listRoles.add(admin);
                } else if (role.getRoleName().equals("moderator")) {
                    Roles modRole = rolesService.findByRoleName(ERole.ROLE_MODERATOR)
                            .orElseThrow(() -> new RuntimeException("Error is not found"));
                    listRoles.add(modRole);
                } else if (role.getRoleName().equals("user")) {
                    Roles userRole = rolesService.findByRoleName(ERole.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Error is not found"));
                    listRoles.add(userRole);
                }
            });
        }

        user.setListRoles(listRoles);
        userService.saveOrUpdate(user1); // Save user1 instead of user

        return "redirect:/api/v1/auth/signin";
    }

    @GetMapping("/signin")
   public String showSigninPage(){
    return "login";
}

    @PostMapping("/signin")
    public String loginUser(@ModelAttribute  User user){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        // sinh jwt trả về token
        String jwt = jwtTokenProvider.generateToken(customUserDetails);
        // lấy các quyền của user
        List<String> listRoles = customUserDetails.getAuthorities().stream()
                .map(item->item.getAuthority()).collect(Collectors.toList());

        return "home";

    }


}
