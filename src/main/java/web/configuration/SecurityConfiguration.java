package web.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import web.configuration.jwt.JwtSecurityConfigurer;
import web.configuration.jwt.JwtTokenProvider;

import javax.sql.DataSource;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration{

    @Configuration
    @Order(1)
    @EnableGlobalMethodSecurity(prePostEnabled = true)
    public static class RestApiSecurityConfig extends WebSecurityConfigurerAdapter{

        @Autowired
        private JwtTokenProvider jwtTokenProvider;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
           http.httpBasic().disable()
                .csrf().disable()
                .antMatcher("/api/**")

                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests()
                   //.antMatchers("/api/specialization/getAll").permitAll()
                   .antMatchers("/api/auth/**").permitAll()
                .antMatchers("/api/request/**").permitAll()
                .antMatchers("/api/apartment/**").permitAll()
                .antMatchers("/api/occupant/**").permitAll()
                   .antMatchers("/api/employee/**").permitAll()
                   .antMatchers("/api/news/**").permitAll()
                   .antMatchers("/api/specialization/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .apply(new JwtSecurityConfigurer(jwtTokenProvider));
        }

        @Bean
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }
    }

    @Configuration
    @Order(2)
    @EnableGlobalMethodSecurity(prePostEnabled = true)
    public static class LoginFormSecurityConfig extends WebSecurityConfigurerAdapter{

        @Autowired
        private BCryptPasswordEncoder bCryptPasswordEncoder;

        @Autowired
        private DataSource dataSource;

        @Value("${spring.queries.users-query}")
        private String usersQuery;

        @Value("${spring.queries.roles-query}")
        private String rolesQuery;

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.jdbcAuthentication()
                .usersByUsernameQuery(usersQuery)
                .authoritiesByUsernameQuery(rolesQuery)
                .dataSource(dataSource)
                .passwordEncoder(bCryptPasswordEncoder);
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests()
                    .antMatchers("/").permitAll()
                    .antMatchers("/login").permitAll()
                    .antMatchers("/registration").permitAll()
                    .antMatchers("/request/**").permitAll()
                    //.antMatchers("/admin/**").hasAuthority("GLOBAL ADMIN")
                    .anyRequest()
                    .authenticated()
                    .and().csrf().disable().formLogin()
                    .loginPage("/login").failureUrl("/login?error=true")
                    .defaultSuccessUrl("/request/list")
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .and().logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/").and().exceptionHandling()
                    .accessDeniedPage("/access-denied");
        }

    }
}
