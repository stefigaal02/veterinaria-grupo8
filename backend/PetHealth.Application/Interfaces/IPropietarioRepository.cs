using PetHealth.Domain.Entities;

namespace PetHealth.Application.Interfaces;

public interface IPropietarioRepository
{
    Task<List<Propietario>> ListarAsync();
    Task<Propietario?> ObtenerAsync(int id);
    Task<bool> ExisteDocumentoAsync(string documento);
    Task AgregarAsync(Propietario propietario);
}