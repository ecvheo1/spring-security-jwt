package com.example.SpringSecurityJwt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.example.SpringSecurityJwt.global.config.JwtConfig;
import com.example.SpringSecurityJwt.global.config.RedisConfig;

@SpringBootApplication
@EnableConfigurationProperties({
		JwtConfig.class,
		RedisConfig.class
})
public class SpringSecurityJwtApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityJwtApplication.class, args);
	}

}
