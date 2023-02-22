package Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                //disable CSRF for Postman usage
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                //authorize all requests to access CSS and JavaScript
                .authorizeRequests(auth -> auth
                        .antMatchers("/js/**", "/css/**", "/img/**", "/webjars/**", "/error/**", "/login/**", "/images/**", "/register").permitAll()
                        // Allow users to access the customer-view
                        .antMatchers("/customer-view").hasRole("USER_ROLE")
                        //all other requests should be role admin
                        .anyRequest().hasRole("ADMIN_ROLE"))
//                        .anyRequest().permitAll())
                //users should log in with HTTP Basic.
                .httpBasic(Customizer.withDefaults())
                .formLogin();
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}