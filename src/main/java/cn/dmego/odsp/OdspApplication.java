package cn.dmego.odsp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;

import javax.servlet.MultipartConfigElement;

@SpringBootApplication
public class OdspApplication {

    public static void main(String[] args) {
        SpringApplication.run(OdspApplication.class, args);
    }

    /**
     * 文件上传临时目录
     * @return
     */
    @Bean
    MultipartConfigElement multipartConfigElement(){
        MultipartConfigFactory factory = new MultipartConfigFactory();
        String path = Class.class.getClass().getResource("/").getPath();
        factory.setLocation(path+"/static/upload/temp");
        return factory.createMultipartConfig();
    }
}