namespace PetHealth.Domain.Entities;

public class Consulta
{
    public int Id { get; set; }
    public DateTime Fecha { get; set; } = DateTime.UtcNow;
    public string Motivo { get; set; } = string.Empty;
    public decimal? Peso { get; set; }
    public decimal? Temperatura { get; set; }
    public string? Observaciones { get; set; }
    public string? Tratamiento { get; set; }

    public int HistoriaClinicaId { get; set; }
    public HistoriaClinica HistoriaClinica { get; set; } = null!;

    public int VeterinarioId { get; set; }
    public Usuario Veterinario { get; set; } = null!;

    public List<Diagnostico> Diagnosticos { get; set; } = new();
}