package kr.goodee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync //websocket을 사용하기 위한 설정
@EnableAspectJAutoProxy //AOP 사용 설정
public class StsshopApplication {

	public static void main(String[] args) {
		SpringApplication.run(StsshopApplication.class, args);
	}
	
	@Bean
	public FilterRegistrationBean<SiteMeshFilter> siteMeshFilter() {
		FilterRegistrationBean<SiteMeshFilter> filter = new FilterRegistrationBean<SiteMeshFilter>();
		filter.setFilter(new SiteMeshFilter());
		return filter;
	}

}
