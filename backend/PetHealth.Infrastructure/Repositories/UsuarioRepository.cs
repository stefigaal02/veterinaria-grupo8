using Microsoft.EntityFrameworkCore;
using PetHealth.Application.Interfaces;
using PetHealth.Domain.Entities;
using PetHealth.Infrastructure.Data;

namespace PetHealth.Infrastructure.Repositories;

public class UsuarioRepository : IUsuarioRepository
{
    private readonly AppDbContext _db;

    public UsuarioRepository(AppDbContext db)
    {
        _db = db;
    }

    public Task<Usuario?> ObtenerPorEmailAsync(string email) =>
        _db.Usuarios.FirstOrDefaultAsync(u => u.Email == email);

    public async Task<Usuario?> ObtenerAsync(int id) => await _db.Usuarios.FindAsync(id);

    public async Task AgregarAsync(Usuario usuario)
    {
        _db.Usuarios.Add(usuario);
        await _db.SaveChangesAsync();
    }
}