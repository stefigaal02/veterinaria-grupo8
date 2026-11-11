using PetHealth.Domain.Entities;

namespace PetHealth.Application.Interfaces;

public interface IUsuarioRepository
{
    Task<Usuario?> ObtenerPorEmailAsync(string email);
    Task<Usuario?> ObtenerAsync(int id);
    Task AgregarAsync(Usuario usuario);
}