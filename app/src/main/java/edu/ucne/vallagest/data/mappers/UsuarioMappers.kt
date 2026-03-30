package edu.ucne.vallagest.data.mappers
import edu.ucne.vallagest.data.local.entities.UsuarioEntity
import edu.ucne.vallagest.data.remote.dto.AuthResponse
import edu.ucne.vallagest.domain.usuarios.model.Usuario

fun AuthResponse.toEntity(): UsuarioEntity {
    return UsuarioEntity(
        usuarioId = this.usuarioId,
        nombre = this.nombre,
        email = this.email,
        rol = this.rol,
        estaLogueado = true
    )
}

fun UsuarioEntity.toUsuario(): Usuario {
    return Usuario(
        usuarioId = this.usuarioId,
        nombre = this.nombre,
        email = this.email,
        rol = this.rol
    )
}

fun AuthResponse.toUsuario(): Usuario {
    return Usuario(
        usuarioId = this.usuarioId,
        nombre = this.nombre,
        email = this.email,
        rol = this.rol
    )
}

