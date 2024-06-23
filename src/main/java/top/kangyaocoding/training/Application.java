package top.kangyaocoding.training;

import com.feiniaojin.gracefulresponse.EnableGracefulResponse;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 描述: Springboot 启动类
 *
 * @author K·Herbert
 * @since 2024-06-22 14:45
 */

@SpringBootApplication
@EnableGracefulResponse
@Configurable
@MapperScan("top.kangyaocoding.training.mapper")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
