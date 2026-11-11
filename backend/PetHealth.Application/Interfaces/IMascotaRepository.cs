using PetHealth.Domain.Entities;

namespace PetHealth.Application.Interfaces;

public interface IMascotaRepository
{
    Task<List<Mascota>> ListarAsync();
    Task<Mascota?> ObtenerAsync(int id);
    Task AgregarAsync(Mascota mascota);
}