using Microsoft.EntityFrameworkCore;
using PetHealth.Application.Interfaces;
using PetHealth.Domain.Entities;
using PetHealth.Infrastructure.Data;

namespace PetHealth.Infrastructure.Repositories;

public class HistoriaClinicaRepository : IHistoriaClinicaRepository
{
    private readonly AppDbContext _db;

    public HistoriaClinicaRepository(AppDbContext db)
    {
        _db = db;
    }

    public Task<HistoriaClinica?> ObtenerPorMascotaAsync(int mascotaId) =>
        _db.HistoriasClinicas
            .Include(h => h.Mascota)
            .Include(h => h.Consultas)
                .ThenInclude(c => c.Diagnosticos)
            .FirstOrDefaultAsync(h => h.MascotaId == mascotaId);

    public Task<bool> ExisteHistoriaAsync(int historiaId) =>
        _db.HistoriasClinicas.AnyAsync(h => h.Id == historiaId);

    public Task<bool> ExisteConsultaAsync(int consultaId) =>
        _db.Consultas.AnyAsync(c => c.Id == consultaId);

    public async Task AgregarHistoriaAsync(HistoriaClinica historia)
    {
        _db.HistoriasClinicas.Add(historia);
        await _db.SaveChangesAsync();
    }

    public async Task AgregarConsultaAsync(Consulta consulta)
    {
        _db.Consultas.Add(consulta);
        await _db.SaveChangesAsync();
    }

    public async Task AgregarDiagnosticoAsync(Diagnostico diagnostico)
    {
        _db.Diagnosticos.Add(diagnostico);
        await _db.SaveChangesAsync();
    }
}