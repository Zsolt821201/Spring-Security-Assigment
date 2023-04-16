package io.github.zsolt821201.springsecurityassigment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/").permitAll()
                .requestMatchers(HttpMethod.GET, "/superAdmin/**").hasRole("SUPERADMIN")
                .requestMatchers(HttpMethod.GET, "/admin/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/user/**").hasRole("USER")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .failureUrl("/failure")
                .defaultSuccessUrl("/products");
        return httpSecurity.build();

    }
    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager(PasswordEncoder passwordEncoder){
        UserDetails user = User.withUsername("mary")
                .password(passwordEncoder.encode("pass"))
                .roles("USER")
                .build();
        UserDetails superAdmin = User.withUsername("bob")
                .password(passwordEncoder.encode("pass"))
                .roles("SUPERADMIN")
                .build();
        UserDetails admin = User.withUsername("tom")
                .password(passwordEncoder.encode("pass"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user,superAdmin,admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return  new BCryptPasswordEncoder();
    }
}
