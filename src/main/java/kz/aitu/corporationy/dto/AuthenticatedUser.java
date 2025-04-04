package kz.aitu.corporationy.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;

@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
public class AuthenticatedUser extends User {

    private final Long id;
    private final String fullName;

    public AuthenticatedUser(Long id,
                             String fullName,
                             String username,
                             String password,
                             String role) {
        super(username, password, Collections.singletonList(new SimpleGrantedAuthority(role)));
        this.id = id;
        this.fullName = fullName;
    }
}