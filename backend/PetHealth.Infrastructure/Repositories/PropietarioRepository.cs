using Microsoft.EntityFrameworkCore;
using PetHealth.Application.Interfaces;
using PetHealth.Domain.Entities;
using PetHealth.Infrastructure.Data;

namespace PetHealth.Infrastructure.Repositories;

public class PropietarioRepository : IPropietarioRepository
{
    private readonly AppDbContext _db;

    public PropietarioRepository(AppDbContext db)
    {
        _db = db;
    }

    public Task<List<Propietario>> ListarAsync() => _db.Propietarios.ToListAsync();

    public async Task<Propietario?> ObtenerAsync(int id) => await _db.Propietarios.FindAsync(id);

    public Task<bool> ExisteDocumentoAsync(string documento) =>
        _db.Propietarios.AnyAsync(p => p.Documento == documento);

    public async Task AgregarAsync(Propietario propietario)
    {
        _db.Propietarios.Add(propietario);
        await _db.SaveChangesAsync();
    }
}