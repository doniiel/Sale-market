package com.ecom.sale.repository.specification.builder;

import com.ecom.sale.enums.Role;
import com.ecom.sale.model.User;
import com.ecom.sale.repository.specification.UserSpecification;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecificationBuilder {

    private Specification<User> spec = null;

    public UserSpecificationBuilder withUsername(String username) {
        if (username != null && !username.isEmpty())  {
            spec = append(spec, UserSpecification.hasUsername(username));
        }
        return this;
    }

    public UserSpecificationBuilder withEmail(String email) {
        if (email != null && !email.isEmpty())  {
            spec = append(spec, UserSpecification.hasEmail(email));
        }
        return this;
    }

    public UserSpecificationBuilder withPhone(String phone) {
        if (phone != null && !phone.isEmpty())  {
            spec = append(spec, UserSpecification.hasPhone(phone));
        }
        return this;
    }

    public UserSpecificationBuilder withRole(Role role) {
        if (role != null)  {
            spec = append(spec, UserSpecification.hasRole(role));
        }
        return this;
    }

    public Specification<User> build() {
        return spec;
    }

    private Specification<User> append(Specification<User> current, Specification<User> addition) {
        if (current == null) {
            return addition;
        }
        return current.and(addition);
    }
}
