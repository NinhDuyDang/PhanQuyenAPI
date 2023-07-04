package ra.model.service;

import ra.model.entity.User;

public interface UserService {
    User findByUserName (String userName); // lấy thông tin username
    boolean existsByUserName(String UserName); // kiểm tra user đã tồn tại chưa
    boolean existsByEmail(String Email);  // mail đã tồn tại chưa
    User saveOrUpdate(User user);
}
