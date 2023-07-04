package ra.jwt;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.extern.slf4j.Slf4j;
import ra.security.CustomUserDetailsService;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter implements Filter {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    private String getJwtFromRequest(HttpServletRequest request){
        String bearderToken = request.getHeader("Authorization");
        // kiểm tra hearder Authorization có chứa thông tin jwt không
        if (StringUtils.hasText(bearderToken)&&bearderToken.startsWith("Bearer")){
            return bearderToken.substring(7); //( bearer + JWT )lấy từ giá trị thứ 7 sau bearer

        }
        return null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try{
            // lay jwt tu request
            String jwt = getJwtFromRequest(request);
            if( StringUtils.hasText(jwt)&& jwtTokenProvider.validateToken(jwt)){
                // lay thong tin tu chuoi jwt
                String userName = jwtTokenProvider.getUserNameFromJwt(jwt);
                // lấy thông tin tu nguoi dung userID
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(userName);
                if (userDetails!=null){
                    //nếu người dùng hợp lệ lấy thông tin cho sercurity context
                    UsernamePasswordAuthenticationToken authentication
                            = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception ex){
			System.out.println("fail on set user authentication: " + ex);
        }
        filterChain.doFilter(request,response);

    }



  }

