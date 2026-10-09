package com.example.helarte;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class HelarteApplicationTests {

    @Test
    void generarHashes() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println("HASH_ADMIN: " + encoder.encode("admin123"));
        System.out.println("HASH_EMPLEADO: " + encoder.encode("empleado123"));
        System.out.println("HASH_CLIENTE: " + encoder.encode("cliente123"));
    }
}
