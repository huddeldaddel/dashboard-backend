package engineer.thomas_werner.dashboard.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final String user;
    private final String password;

    public WebSecurityConfig(@Value("${security.user}") final String user,
                             @Value("${security.password}") final String password) {
        this.user = user;
        this.password = password;
    }

    @Autowired
    private AuthenticationEntryPoint authEntryPoint;

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        if(isEnabled()) {
            http.csrf().disable();

            // All requests send to the Web Server request must be authenticated
            http.authorizeRequests().anyRequest().authenticated();

            // Use AuthenticationEntryPoint to authenticate user/password
            http.httpBasic().authenticationEntryPoint(authEntryPoint);
        }
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configureGlobal(final AuthenticationManagerBuilder auth) throws Exception {
        if(isEnabled()) {
            final String encryptedPassword = this.passwordEncoder().encode(password);
            final UserDetails userDetails = User.withUsername(user).password(encryptedPassword).roles("USER").build();
            auth.inMemoryAuthentication().withUser(userDetails);
        }
    }

    private boolean isEnabled() {
        return (null != user) && (user.length() > 0) && (null != password) && (password.length() > 0);
    }

}
