package ra.model.reponsitory;

import org.springframework.data.jpa.repository.JpaRepository;
import ra.model.entity.User;

public interface UserReponsitory extends JpaRepository<User,Integer> {
    User findByUserName (String userName); // lấy thông tin username
    boolean existsByUserName(String UserName); // kiểm tra user đã tồn tại chưa
    boolean existsByEmail(String Email);  // mail đã tồn tại chưa
}
