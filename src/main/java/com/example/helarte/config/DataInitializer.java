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
        // Inicializar usuarios iniciales según los roles requeridos
        if (usuarioRepository.count() == 0) {
            Usuario admin = new Usuario(
                    "Administrador Helarte",
                    "admin@helarte.com",
                    passwordEncoder.encode("admin123"),
                    "ADMIN",
                    "999111222"
            );
            usuarioRepository.save(admin);

            Usuario empleado = new Usuario(
                    "Carlos Colaborador",
                    "empleado@helarte.com",
                    passwordEncoder.encode("empleado123"),
                    "EMPLEADO",
                    "999333444"
            );
            usuarioRepository.save(empleado);

            Usuario cliente = new Usuario(
                    "Ana Cliente",
                    "cliente@helarte.com",
                    passwordEncoder.encode("cliente123"),
                    "CLIENTE",
                    "999555666"
            );
            usuarioRepository.save(cliente);

            System.out.println(">>> [H-elarte] Usuarios de prueba inicializados con éxito.");
        }

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
}
