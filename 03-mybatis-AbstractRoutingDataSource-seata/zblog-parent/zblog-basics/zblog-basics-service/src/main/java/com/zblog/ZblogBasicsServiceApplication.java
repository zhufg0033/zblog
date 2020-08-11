package com.zblog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(value = "com.zblog.basics.dao")
public class ZblogBasicsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZblogBasicsServiceApplication.class, args);
	}

}
