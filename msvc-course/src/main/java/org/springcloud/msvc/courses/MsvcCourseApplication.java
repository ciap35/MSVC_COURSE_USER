package org.springcloud.msvc.courses;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsvcCourseApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsvcCourseApplication.class, args);
	}

}
