package com.ecom.sale.repository.specification.builder;

import com.ecom.sale.enums.Role;
import com.ecom.sale.model.User;
import com.ecom.sale.repository.specification.UserSpecification;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecificationBuilder {
    private Specification<User> spec;

    public UserSpecificationBuilder withUsername(String username) {
        if (username != null && !username.isEmpty())  {
            spec = spec.and(UserSpecification.hasUsername(username));
        }
        return this;
    }

    public UserSpecificationBuilder withEmail(String email) {
        if (email != null && !email.isEmpty())  {
            spec = spec.and(UserSpecification.hasEmail(email));
        }
        return this;
    }

    public UserSpecificationBuilder withPhone(String phone) {
        if (phone != null && !phone.isEmpty())  {
            spec = spec.and(UserSpecification.hasPhone(phone));
        }
        return this;
    }

    public UserSpecificationBuilder withRole(Role role) {
        if (role != null)  {
            spec = spec.and(UserSpecification.hasRole(role));
        }
        return this;
    }

    public Specification<User> build() {
        return spec;
    }
}
