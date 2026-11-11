using PetHealth.Domain.Entities;

namespace PetHealth.Application.Interfaces;

public interface IHistoriaClinicaRepository
{
    Task<HistoriaClinica?> ObtenerPorMascotaAsync(int mascotaId);
    Task<bool> ExisteHistoriaAsync(int historiaId);
    Task<bool> ExisteConsultaAsync(int consultaId);
    Task AgregarHistoriaAsync(HistoriaClinica historia);
    Task AgregarConsultaAsync(Consulta consulta);
    Task AgregarDiagnosticoAsync(Diagnostico diagnostico);
}