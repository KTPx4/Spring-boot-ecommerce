package ecommerce.infra.security.service;

import ecommerce.core.domain.auth.UserPrincipal;
import ecommerce.core.infra.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserPrincipal userPrincipal = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        if (userPrincipal.getStatus() == null || userPrincipal.getStatus() != 1) {
            throw new UsernameNotFoundException("User is inactive: " + username);
        }

        return User.builder()
                .username(userPrincipal.getUsername())
                .password(userPrincipal.getPassword()) // Use actual password hash from database
                .authorities(getAuthorities(userPrincipal))
                .build();
    }

    private Collection<? extends GrantedAuthority> getAuthorities(UserPrincipal userPrincipal) {
        return userPrincipal.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
    }

    public UserPrincipal loadUserPrincipalByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }
}
