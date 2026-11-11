namespace PetHealth.Domain.Entities;

public class AplicacionVacuna
{
    public int Id { get; set; }
    public DateTime FechaAplicacion { get; set; } = DateTime.UtcNow;
    public DateTime? FechaRefuerzo { get; set; }
    public string? Lote { get; set; }
    public string? Observaciones { get; set; }

    public int MascotaId { get; set; }
    public Mascota Mascota { get; set; } = null!;

    public int VacunaId { get; set; }
    public Vacuna Vacuna { get; set; } = null!;

    public int AplicadaPorId { get; set; }
    public Usuario AplicadaPor { get; set; } = null!;
}