package com.sekolah.sisteminformasi.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Sistem Informasi Sekolah REST API",
                version = "v1.0",
                description = "Backend REST API Enterprise untuk Sistem Informasi Akademik Sekolah (Manajemen Guru, Mapel, Penjadwalan Cerdas, Presensi, Rapor UTS/Akhir, dan Transkrip Kumulatif SMP)",
                contact = @Contact(name = "Developer Portofolio", email = "developer@sekolah.com")
        ),
        security = @SecurityRequirement(name = "Bearer Authentication")
)
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        description = "Masukkan Token JWT yang didapat dari endpoint POST /api/auth/login"
)
public class OpenApiConfig {
}
