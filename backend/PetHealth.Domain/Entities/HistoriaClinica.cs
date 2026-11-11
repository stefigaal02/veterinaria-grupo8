namespace PetHealth.Domain.Entities;

public class HistoriaClinica
{
    public int Id { get; set; }
    public DateTime FechaApertura { get; set; } = DateTime.UtcNow;
    public string? Antecedentes { get; set; }

    public int MascotaId { get; set; }
    public Mascota Mascota { get; set; } = null!;

    public List<Consulta> Consultas { get; set; } = new();
}