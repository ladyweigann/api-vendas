package br.com.udemy.sb.springexpert;

import br.com.udemy.sb.springexpert.domain.entity.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class SpringExpertApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringExpertApplication.class, args);
	}

}
