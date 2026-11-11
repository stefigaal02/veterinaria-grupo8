using Microsoft.EntityFrameworkCore;
using PetHealth.Domain.Entities;

namespace PetHealth.Infrastructure.Data;

public class AppDbContext : DbContext
{
    public AppDbContext(DbContextOptions<AppDbContext> options) : base(options) { }

    public DbSet<Usuario> Usuarios => Set<Usuario>();
    public DbSet<Propietario> Propietarios => Set<Propietario>();
    public DbSet<Mascota> Mascotas => Set<Mascota>();
    public DbSet<HistoriaClinica> HistoriasClinicas => Set<HistoriaClinica>();
    public DbSet<Consulta> Consultas => Set<Consulta>();
    public DbSet<Diagnostico> Diagnosticos => Set<Diagnostico>();
    public DbSet<Vacuna> Vacunas => Set<Vacuna>();
    public DbSet<AplicacionVacuna> AplicacionesVacunas => Set<AplicacionVacuna>();

    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        // Relación 1:1 Mascota - HistoriaClinica
        modelBuilder.Entity<Mascota>()
            .HasOne(m => m.HistoriaClinica)
            .WithOne(h => h.Mascota)
            .HasForeignKey<HistoriaClinica>(h => h.MascotaId);
    }
}