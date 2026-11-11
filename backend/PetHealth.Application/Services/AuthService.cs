using PetHealth.Application.DTOs;
using PetHealth.Application.Interfaces;
using PetHealth.Domain.Entities;
using PetHealth.Domain.Enums;

namespace PetHealth.Application.Services;

public class AuthService
{
    private readonly IUsuarioRepository _repo;

    public AuthService(IUsuarioRepository repo)
    {
        _repo = repo;
    }

    public async Task<UsuarioDto> RegistrarAsync(RegistroDto dto)
    {
        if (!Enum.TryParse<Rol>(dto.Rol, true, out var rol) || !Enum.IsDefined(rol))
            throw new InvalidOperationException("Rol no válido. Use Administrador, Veterinario o Auxiliar");

        var email = dto.Email.Trim().ToLower();
        if (await _repo.ObtenerPorEmailAsync(email) != null)
            throw new InvalidOperationException("Ya existe un usuario con ese correo");

        var u = new Usuario
        {
            Nombre = dto.Nombre,
            Email = email,
            PasswordHash = BCrypt.Net.BCrypt.HashPassword(dto.Password), // nunca en texto plano
            Rol = rol
        };
        await _repo.AgregarAsync(u);
        return Convertir(u);
    }

    public async Task<UsuarioDto?> LoginAsync(LoginDto dto)
    {
        var u = await _repo.ObtenerPorEmailAsync(dto.Email.Trim().ToLower());
        if (u == null || !BCrypt.Net.BCrypt.Verify(dto.Password, u.PasswordHash))
            return null;
        return Convertir(u);
    }

    public async Task<UsuarioDto?> PerfilAsync(int id)
    {
        var u = await _repo.ObtenerAsync(id);
        return u == null ? null : Convertir(u);
    }

    private static UsuarioDto Convertir(Usuario u) => new()
    {
        Id = u.Id,
        Nombre = u.Nombre,
        Email = u.Email,
        Rol = u.Rol.ToString()
    };
}