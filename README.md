# Sistema de Reporte de Incidencias Sanitarias — Rosmar

[![CI - Pruebas Unitarias](https://github.com/CarlosTirado16/incidencias-sanitarias/actions/workflows/ci.yml/badge.svg)](https://github.com/CarlosTirado16/incidencias-sanitarias/actions/workflows/ci.yml)

Sistema web para el registro, seguimiento y gestión de incidencias sanitarias en plantas empacadoras de carne. Desarrollado con Java, Spring Boot y MySQL para Rosmar Consultoría de Sanidad.

---

## Tabla de Contenidos

- [Descripción](#descripción)
- [Problema Identificado](#problema-identificado)
- [Solución](#solución)
- [Arquitectura](#arquitectura)
- [Requerimientos](#requerimientos)
- [Instalación](#instalación)
- [Configuración](#configuración)
- [Uso](#uso)
- [Contribución](#contribución)
- [Roadmap](#roadmap)

---

## Descripción

Aplicación web MVP que permite a un equipo de 9 personas (8 técnicos de sanidad y 1 supervisor) registrar, gestionar y dar seguimiento a incidencias sanitarias desde cualquier dispositivo con navegador web dentro de la red local de la planta.

## Problema Identificado

El equipo de sanidad de Rosmar registraba incidencias de forma manual en papel y comunicaba los reportes por WhatsApp. Esto generaba pérdida de información, falta de trazabilidad y dificultades para preparar reportes en auditorías sanitarias.

## Solución

Sistema web con dos roles de usuario:

- **Empleado**: registra incidencias con título, descripción, área y severidad.
- **Supervisor**: consulta el historial, filtra por área y estatus, cambia el estatus de las incidencias y descarga reportes PDF para auditorías.

## Arquitectura

```
Navegador web (HTML + CSS + JS)
        ↓ HTTP
Spring Boot 4 + Tomcat embebido
  ├── Spring Security (autenticación y roles)
  ├── Spring Data JPA (acceso a datos)
  ├── Thymeleaf (plantillas HTML)
  └── iText (generación de PDF)
        ↓ JDBC
MySQL 8.0 (local)
  ├── usuarios
  ├── areas
  └── incidencias
```

---

## Requerimientos

### Servidor de aplicación
- Apache Tomcat 11 (embebido en Spring Boot)

### Base de datos
- MySQL 8.0

### Runtime
- Java 21 (JDK)
- Maven 3.9+ (o usar el wrapper `./mvnw` incluido)

### Paquetes principales
| Dependencia | Versión |
|---|---|
| Spring Boot | 4.0.5 |
| Spring Security | 7.0.4 |
| Spring Data JPA | 4.0.4 |
| Hibernate | 7.2.7 |
| Thymeleaf | 3.1.3 |
| MySQL Connector/J | 9.6.0 |
| iText PDF | 5.5.13.3 |
| H2 (tests) | latest |

---

## Instalación

### 1. Clonar el repositorio

```bash
git clone git@github.com:CarlosTirado16/incidencias-sanitarias.git
cd incidencias-sanitarias
```

### 2. Crear la base de datos

Abre MySQL Workbench o la terminal MySQL y ejecuta:

```sql
CREATE DATABASE rosmar_incidencias;
```

### 3. Insertar datos iniciales

```sql
USE rosmar_incidencias;

INSERT INTO areas (nombre, descripcion) VALUES 
('Área de Empaque', 'Zona de empaque de productos cárnicos'),
('Área de Refrigeración', 'Zona de almacenamiento en frío'),
('Área de Procesamiento', 'Zona de procesamiento de carne'),
('Área de Limpieza', 'Zona de sanitización y limpieza');

-- Contraseña: Admin1234
INSERT INTO usuarios (nombre, apellido, email, password, rol) VALUES 
('Supervisor', 'Rosmar', 'supervisor@rosmar.com', 
'$2a$10$aVgjHwEYLeja5fjBHf8ZIOcax9qIEobgkYiJND/XDCF7Syn9h8sRa', 
'SUPERVISOR');

-- Contraseña: Tecnico1234
INSERT INTO usuarios (nombre, apellido, email, password, rol) VALUES 
('Juan', 'Martínez', 'juan@rosmar.com',
'$2a$10$Z.GfWzS3frLpYu5V6FfmkeznxXTXIHu4QQ3VHRPypxUuFY/horHo2',
'EMPLEADO');
```

### 4. Configurar application.properties

Edita `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/rosmar_incidencias
spring.datasource.username=root
spring.datasource.password=TU_PASSWORD_AQUI
```

### 5. Ejecutar el proyecto

```bash
./mvnw spring-boot:run
```

Abre el navegador en: `http://localhost:8080`

### Ejecutar pruebas manualmente

```bash
./mvnw test
```

### Implementar en producción (JAR)

```bash
./mvnw clean package -DskipTests
java -jar target/incidencias-0.0.1-SNAPSHOT.jar
```

---

## Configuración

### Archivos de configuración

| Archivo | Ubicación | Descripción |
|---|---|---|
| `application.properties` | `src/main/resources/` | Conexión a BD, puerto, JPA |
| `ci.yml` | `.github/workflows/` | Pipeline de GitHub Actions |

### Variables principales en application.properties

```properties
# Base de datos
spring.datasource.url=jdbc:mysql://localhost:3306/rosmar_incidencias
spring.datasource.username=root
spring.datasource.password=TU_PASSWORD

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Servidor
server.port=8080
```

---

## Uso

### Usuario Empleado

1. Accede a `http://localhost:8080` desde cualquier navegador en la red local
2. Inicia sesión con tu correo y contraseña
3. Haz clic en **+ Nueva incidencia**
4. Llena el formulario: título, descripción, área y severidad
5. Haz clic en **Guardar incidencia**
6. La incidencia aparece en el historial con estatus **ABIERTA**

**Credenciales de prueba:**
- Email: `juan@rosmar.com`
- Contraseña: `Tecnico1234`

### Usuario Supervisor

Además de registrar incidencias, el supervisor puede:

1. **Filtrar el historial** por área o estatus usando los selectores en la parte superior
2. **Cambiar el estatus** de cualquier incidencia directamente desde la tabla (ABIERTA → EN PROCESO → RESUELTA)
3. **Descargar reporte PDF** con el botón verde — respeta los filtros activos en pantalla

**Credenciales de prueba:**
- Email: `supervisor@rosmar.com`
- Contraseña: `Admin1234`

---

## Contribución

### Guía de contribución

Este proyecto sigue el flujo de trabajo **Git Flow** con dos ramas principales protegidas: `main` y `develop`.

#### Pasos para contribuir

**1. Clona el repositorio**
```bash
git clone git@github.com:CarlosTirado16/incidencias-sanitarias.git
cd incidencias-sanitarias
```

**2. Crea un branch desde develop**
```bash
git checkout develop
git pull origin develop
git checkout -b feature/nombre-de-tu-feature
```

Convención de nombres:
- `feature/` — nueva funcionalidad
- `fix/` — corrección de errores
- `docs/` — documentación

**3. Desarrolla y haz commit**
```bash
git add .
git commit -m "feat: descripcion del cambio (#numero-issue)"
```

Convención de commits:
- `feat:` — nueva funcionalidad
- `fix:` — corrección de error
- `docs:` — documentación
- `test:` — pruebas
- `chore:` — configuración

**4. Sube tu branch**
```bash
git push origin feature/nombre-de-tu-feature
```

**5. Crea un Pull Request en GitHub**
- Base: `develop`
- Compare: `feature/nombre-de-tu-feature`
- Describe los cambios realizados

**6. Espera el merge**
- El CI ejecutará las pruebas automáticamente
- Una vez que las pruebas pasen, el PR puede ser mergeado a `develop`
- Los cambios de `develop` se integran a `main` mediante PR al finalizar cada milestone

---

## Roadmap

Las siguientes funcionalidades están documentadas como mejoras futuras (fuera del alcance de v1.0):

| # | Funcionalidad | Descripción |
|---|---|---|
| #16 | Subida de imágenes | Adjuntar fotografías de evidencia a las incidencias |
| #17 | Dashboard con gráficas | Panel visual con Chart.js para el supervisor |
| #18 | Exportación Excel | Descarga del historial en formato Excel (Apache POI) |
| #19 | Notificaciones automáticas | Alertas al supervisor cuando se registra una incidencia |
| #20 | Despliegue en la nube | Railway o AWS para acceso remoto desde cualquier lugar |

---

## Información del Proyecto

- **Alumno:** Carlos Eduardo Tirado Bañuelos
- **Institución:** Universidad Tecmilenio
- **Materia:** Taller de Productividad Basada en Herramientas Tecnológicas
- **Empresa:** Rosmar Consultoría de Sanidad
- **Versión:** 1.0.0-MVP