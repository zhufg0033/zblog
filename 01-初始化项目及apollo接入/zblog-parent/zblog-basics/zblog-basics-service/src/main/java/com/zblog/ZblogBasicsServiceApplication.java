package com.zblog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "com.zblog.basics.mapper")
public class ZblogBasicsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZblogBasicsServiceApplication.class, args);
	}

}
