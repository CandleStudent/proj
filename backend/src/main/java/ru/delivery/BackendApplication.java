package ru.delivery;

import com.google.ortools.Loader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        Loader.loadNativeLibraries(); // для корректной работы Google OR-TOOLS
        SpringApplication.run(BackendApplication.class, args);
    }

}
