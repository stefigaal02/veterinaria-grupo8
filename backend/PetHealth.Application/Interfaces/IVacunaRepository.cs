using PetHealth.Domain.Entities;

namespace PetHealth.Application.Interfaces;

public interface IVacunaRepository
{
    Task<List<Vacuna>> ListarAsync();
    Task<Vacuna?> ObtenerAsync(int id);
    Task AgregarAsync(Vacuna vacuna);
    Task AgregarAplicacionAsync(AplicacionVacuna aplicacion);
    Task<List<AplicacionVacuna>> ListarPorMascotaAsync(int mascotaId);
    Task<List<AplicacionVacuna>> ListarProximasAsync(DateTime desde, DateTime hasta);
}