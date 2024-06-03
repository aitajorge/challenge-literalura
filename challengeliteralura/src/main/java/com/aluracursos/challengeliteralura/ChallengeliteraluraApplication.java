package com.aluracursos.challengeliteralura;

import com.aluracursos.challengeliteralura.principal.Principal;
import com.aluracursos.challengeliteralura.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChallengeliteraluraApplication implements CommandLineRunner {

	private final LibroRepository libroRepository;
	private final Principal principal;

	@Autowired
	public ChallengeliteraluraApplication(LibroRepository libroRepository, Principal principal) {
		this.libroRepository = libroRepository;
		this.principal = principal;
	}

	public static void main(String[] args) {
		SpringApplication.run(ChallengeliteraluraApplication.class, args);
	}

	@Override
	public void run(String... args) {
		principal.muestraMenu();
	}
}
