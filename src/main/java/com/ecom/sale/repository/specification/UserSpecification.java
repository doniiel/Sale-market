package com.ecom.sale.repository.specification;

import com.ecom.sale.enums.Role;
import com.ecom.sale.model.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

    public static Specification<User> hasUsername(String userName) {
        return ((root, query, cb) ->
                cb.equal(root.get("username"), "%" + userName.toLowerCase() + "%"));
    }

    public static Specification<User> hasEmail(String email) {
        return ((root, query, cb) ->
                cb.equal(root.get("email"), "%" + email.toLowerCase() + "%"));
    }

    public static Specification<User> hasPhone(String phone) {
        return ((root, query, cb) ->
                cb.equal(root.get("phone"), phone.toLowerCase()));
    }

    public static Specification<User> hasRole(Role role) {
        return ((root, query, cb) ->
                cb.equal(root.get("role"), role));
    }
}
