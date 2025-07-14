package com.thesis.backend.service.client.specification;

import com.thesis.backend.entity.client.Client;
import org.springframework.data.jpa.domain.Specification;

public class ClientSpecification {

    public static Specification<Client> hasUserId(Long userId) {
        return (root, query, cb) -> cb.equal(root.get("user").get("id"), userId);
    }

    public static Specification<Client> hasName(String name) {
        return (root, query, cb) -> {
            if (name == null || name.isEmpty()) return null;
            return cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<Client> hasEmail(String email) {
        return (root, query, cb) -> {
            if (email == null || email.isEmpty()) return null;
            return cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%");
        };
    }

    public static Specification<Client> hasPhoneNumber(String phoneNumber) {
        return (root, query, cb) -> {
            if (phoneNumber == null || phoneNumber.isEmpty()) return null;
            return cb.like(root.get("phoneNumber"), "%" + phoneNumber + "%");
        };
    }
}
