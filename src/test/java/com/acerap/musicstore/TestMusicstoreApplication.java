package com.acerap.musicstore;

import org.springframework.boot.SpringApplication;

public class TestMusicstoreApplication {

	public static void main(String[] args) {
		SpringApplication.from(MusicStoreApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
