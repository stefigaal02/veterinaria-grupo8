using Microsoft.EntityFrameworkCore;
using PetHealth.Application.Interfaces;
using PetHealth.Domain.Entities;
using PetHealth.Infrastructure.Data;

namespace PetHealth.Infrastructure.Repositories;

public class VacunaRepository : IVacunaRepository
{
    private readonly AppDbContext _db;

    public VacunaRepository(AppDbContext db)
    {
        _db = db;
    }

    public Task<List<Vacuna>> ListarAsync() => _db.Vacunas.ToListAsync();

    public async Task<Vacuna?> ObtenerAsync(int id) => await _db.Vacunas.FindAsync(id);

    public async Task AgregarAsync(Vacuna vacuna)
    {
        _db.Vacunas.Add(vacuna);
        await _db.SaveChangesAsync();
    }

    public async Task AgregarAplicacionAsync(AplicacionVacuna aplicacion)
    {
        _db.AplicacionesVacunas.Add(aplicacion);
        await _db.SaveChangesAsync();
    }

    public Task<List<AplicacionVacuna>> ListarPorMascotaAsync(int mascotaId) =>
        _db.AplicacionesVacunas
            .Include(a => a.Mascota)
            .Include(a => a.Vacuna)
            .Where(a => a.MascotaId == mascotaId)
            .OrderByDescending(a => a.FechaAplicacion)
            .ToListAsync();

    public Task<List<AplicacionVacuna>> ListarProximasAsync(DateTime desde, DateTime hasta) =>
        _db.AplicacionesVacunas
            .Include(a => a.Mascota)
            .Include(a => a.Vacuna)
            .Where(a => a.FechaRefuerzo >= desde && a.FechaRefuerzo <= hasta)
            .OrderBy(a => a.FechaRefuerzo)
            .ToListAsync();
}