package ra.jwt;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import ra.security.CustomUserDetails;

@Component
@Slf4j
public class JwtTokenProvider {
    @Value("${ra.jwt.secret}")
    private  String JWT_SECRET;
    @Value("${ra.jwt.expiration}")
    private  int JWT_EXPIRATION;
    // tao jwt từ thông tin của User
    public String generateToken(CustomUserDetails customUserDetails){
        Date now = new Date();
        Date dateExpired = new Date(now.getTime()+JWT_EXPIRATION);  // thời gian hiện tại + ngày hết hiệu lực
        // tạo chuỗi jwt bằng username
        return Jwts.builder().setSubject(customUserDetails.getUsername())
                .setIssuedAt(now) //  ngày bắt đầu có hiệu lực acc
                .setExpiration(dateExpired) //  ngày hết hạn
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET).compact(); // sử dụng giải thuật mã hóa HS512 kèm key để giải mã

    }
    // lấy thông tin user từ jwt
    public  String getUserNameFromJwt(String token){
        Claims claims = Jwts.parser().setSigningKey(JWT_SECRET)
                .parseClaimsJws(token).getBody();
        // Tra lại username
        return claims.getSubject();
    }
    // validate thông tin chuỗi jwt
    public boolean validateToken(String token){
        try{
            Jwts.parser().setSigningKey(JWT_SECRET)
                    .parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException ex){
			System.out.println("invail JWT Token: " + ex);
        } catch (ExpiredJwtException ex){
			System.out.println("Expired JWT Token: " + ex);
        }catch (UnsupportedJwtException ex){
			System.out.println("Unsuported JWT Token: " + ex);
        }catch (IllegalArgumentException ex){
			System.out.println("JWT claims String is empty: " + ex);
        }
        return false;
    }
}
