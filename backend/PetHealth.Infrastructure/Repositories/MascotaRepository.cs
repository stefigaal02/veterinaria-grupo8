using Microsoft.EntityFrameworkCore;
using PetHealth.Application.Interfaces;
using PetHealth.Domain.Entities;
using PetHealth.Infrastructure.Data;

namespace PetHealth.Infrastructure.Repositories;

public class MascotaRepository : IMascotaRepository
{
    private readonly AppDbContext _db;

    public MascotaRepository(AppDbContext db)
    {
        _db = db;
    }

    public Task<List<Mascota>> ListarAsync() =>
        _db.Mascotas.Include(m => m.Propietario).ToListAsync();

    public Task<Mascota?> ObtenerAsync(int id) =>
        _db.Mascotas.Include(m => m.Propietario).FirstOrDefaultAsync(m => m.Id == id);

    public async Task AgregarAsync(Mascota mascota)
    {
        _db.Mascotas.Add(mascota);
        await _db.SaveChangesAsync();
    }
}