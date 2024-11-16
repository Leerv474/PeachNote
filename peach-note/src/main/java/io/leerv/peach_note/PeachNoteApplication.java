package io.leerv.peach_note;

import io.leerv.peach_note.authorities.Role;
import io.leerv.peach_note.authorities.RoleRepository;
import io.leerv.peach_note.permission.Permission;
import io.leerv.peach_note.permission.PermissionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
public class PeachNoteApplication {
	public static void main(String[] args) {
		SpringApplication.run(PeachNoteApplication.class, args);
	}

	// TODO: absolutely needed
	// 		- check for uniqueness of names of boards, status tables, tasks and projects for each user

	@Bean
	public CommandLineRunner runner(RoleRepository roleRepository, PermissionRepository permissionRepository) {
		return args -> {
			if (roleRepository.findByName("USER").isEmpty()) {
				roleRepository.save(
						Role.builder().name("USER").build());
			}

			if (permissionRepository.findById("CREATOR").isEmpty()) {
				permissionRepository.save(
						Permission.builder()
								.name("CREATOR")
								.description("User who created the board, has the right to edit and delete the board")
								.roleActive(true)
								.permissionLevel(3)
								.build()
				);
			}

			if (permissionRepository.findById("EDITOR").isEmpty()) {
				permissionRepository.save(
						Permission.builder()
								.name("EDITOR")
								.description("The user with rights to edit contents of the board")
								.roleActive(true)
								.permissionLevel(2)
								.build()
				);
			}

			if (permissionRepository.findById("VIEWER").isEmpty()) {
				permissionRepository.save(
						Permission.builder()
								.name("VIEWER")
								.description("The user with rights to view contents of the board")
								.roleActive(true)
								.permissionLevel(1)
								.build()
				);
			}
		};
	}
}
