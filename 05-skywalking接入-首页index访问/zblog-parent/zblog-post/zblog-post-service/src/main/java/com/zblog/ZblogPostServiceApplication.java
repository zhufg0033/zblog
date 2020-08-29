package com.zblog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(value = "com.zblog.sharedb.dao")
public class ZblogPostServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZblogPostServiceApplication.class, args);
	}

}
