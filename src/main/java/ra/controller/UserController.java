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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/signup")
    public String showSignupPage(Model model){
        model.addAttribute("account", new User());
        return "register";
    }

    @GetMapping("/signin")
    public String showSigninPage(){
        return "login";
    }

    @PostMapping("/signup")
    public String registerUser(@RequestBody SignupRequest signupRequest) {
        // if (userService.existsByUserName(signupRequest.getUserName())) {
        //     return ResponseEntity.badRequest().body(new MessageReponse("Error:Username is already"));
        // }
        // if (userService.existsByEmail(signupRequest.getEmail())) {
        //     return ResponseEntity.badRequest().body(new MessageReponse("Error:Mail is already"));
        // }
        User user = new User();
        user.setUserName(signupRequest.getUserName());
        user.setPassword(encoder.encode(signupRequest.getPassword()));
        user.setEmail(signupRequest.getEmail());
        user.setPhone(signupRequest.getPhone());
        user.setUserStatus(true);

        //created ngày
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date dateNow = new Date();
        String strNow = sdf.format(dateNow);
        try {
            user.setCreated(sdf.parse(strNow));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Set<String> strRoles = signupRequest.getListRoles();
        Set<Roles> listRoles = new HashSet<>();
        if (strRoles == null) {
            // Lấy vai trò người dùng mặc định
            Roles userRole = rolesService.findByRoleName(ERole.ROLE_USER).orElseThrow(() -> new RuntimeException("Default user role not found"));
            listRoles.add(userRole);
        }

    else {
            strRoles.forEach(role->{
                switch (role){
                    case "admin":
                        Roles admin = rolesService.findByRoleName(ERole.ROLE_ADMIN)
                                .orElseThrow(()->new RuntimeException("Error is not found "));
                        listRoles.add(admin);
					break;
                    case"moderator" :
                        Roles modRole = rolesService.findByRoleName(ERole.ROLE_MODERATOR)
                                .orElseThrow(()->new RuntimeException("Error is not found"));
                        listRoles.add(modRole);
					break;
                    case "user":
                        Roles userRole = rolesService.findByRoleName(ERole.ROLE_USER)
                                .orElseThrow(()->new RuntimeException("Error is not found"));
                        listRoles.add(userRole);
					break;
                }
            });

    }
        user.setListRoles(listRoles);
        userService.saveOrUpdate(user);

        return "redirect:/api/v1/auth/signin";
    }
    @PostMapping("/signin")
    public String loginUser(@RequestBody LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword())
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
