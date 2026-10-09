package com.example.helarte.config;

import com.example.helarte.model.Mesa;
import com.example.helarte.model.Servicio;
import com.example.helarte.model.Usuario;
import com.example.helarte.repository.MesaRepository;
import com.example.helarte.repository.ServicioRepository;
import com.example.helarte.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final MesaRepository mesaRepository;
    private final ServicioRepository servicioRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UsuarioRepository usuarioRepository,
                           MesaRepository mesaRepository,
                           ServicioRepository servicioRepository,
                           PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.mesaRepository = mesaRepository;
        this.servicioRepository = servicioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Asegurar la existencia y contraseñas correctas para los 3 actores principales
        inicializarOActualizarUsuario("Administrador Helarte", "admin@helarte.com", "admin123", "ADMIN", "999111222");
        inicializarOActualizarUsuario("Carlos Colaborador", "empleado@helarte.com", "empleado123", "EMPLEADO", "999333444");
        inicializarOActualizarUsuario("Ana Cliente", "cliente@helarte.com", "cliente123", "CLIENTE", "999555666");

        // Inicializar mesas si no existen
        if (mesaRepository.count() == 0) {
            mesaRepository.save(new Mesa(1, 2, "Terraza Frontal", "DISPONIBLE"));
            mesaRepository.save(new Mesa(2, 4, "Salón Principal", "DISPONIBLE"));
            mesaRepository.save(new Mesa(3, 4, "Salón Principal", "DISPONIBLE"));
            mesaRepository.save(new Mesa(4, 6, "Zona Jardín de Helados", "DISPONIBLE"));
            mesaRepository.save(new Mesa(5, 8, "Salón VIP Eventos", "DISPONIBLE"));
            System.out.println(">>> [H-elarte] Mesas del establecimiento inicializadas con éxito.");
        }

        // Inicializar servicios de heladería si no existen
        if (servicioRepository.count() == 0) {
            servicioRepository.save(new Servicio(
                    "Reserva Mesa Estándar - Heladería",
                    "Reserva de mesa con atención en mesa para consumo a la carta de nuestra variedad de helados artesanales.",
                    15.00,
                    60,
                    true
            ));

            servicioRepository.save(new Servicio(
                    "Cata Degustación de Helados Gourmet",
                    "Degustación guiada de 8 sabores artesanales selectos con toppings premium y barquillos artesanales.",
                    45.00,
                    45,
                    true
            ));

            servicioRepository.save(new Servicio(
                    "Experiencia Dulce: Fondue de Chocolate & Copas",
                    "Fondue de chocolate belga caliente con frutas frescas, malvaviscos y dos copas gigantes de helado artesanal.",
                    65.00,
                    60,
                    true
            ));

            servicioRepository.save(new Servicio(
                    "Taller & Cumpleaños Heladero",
                    "Espacio reservado con mini taller para personalizar helados, incluye barra libre de helados por 90 minutos.",
                    120.00,
                    90,
                    true
            ));
            System.out.println(">>> [H-elarte] Catálogo de servicios de heladería inicializado con éxito.");
        }
    }

    private void inicializarOActualizarUsuario(String nombre, String email, String passwordPlana, String rol, String telefono) {
        usuarioRepository.findByEmail(email).ifPresentOrElse(
                usuarioExistente -> {
                    // Actualizar contraseña asegurando BCrypt válido y rol correcto
                    usuarioExistente.setPassword(passwordEncoder.encode(passwordPlana));
                    usuarioExistente.setRol(rol);
                    usuarioExistente.setActivo(true);
                    usuarioRepository.save(usuarioExistente);
                    System.out.println(">>> [H-elarte] Usuario verificado y credenciales sincronizadas: " + email);
                },
                () -> {
                    Usuario nuevo = new Usuario(nombre, email, passwordEncoder.encode(passwordPlana), rol, telefono);
                    usuarioRepository.save(nuevo);
                    System.out.println(">>> [H-elarte] Usuario inicializado: " + email);
                }
        );
    }
}
