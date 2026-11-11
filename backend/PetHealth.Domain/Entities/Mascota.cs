namespace PetHealth.Domain.Entities;

public class Mascota
{
    public int Id { get; set; }
    public string Nombre { get; set; } = string.Empty;
    public string Especie { get; set; } = string.Empty;
    public string? Raza { get; set; }
    public string Sexo { get; set; } = string.Empty;
    public DateTime? FechaNacimiento { get; set; }
    public decimal? PesoActual { get; set; }
    public bool Activo { get; set; } = true;

    public int PropietarioId { get; set; }
    public Propietario Propietario { get; set; } = null!;

    public HistoriaClinica? HistoriaClinica { get; set; }
    public List<AplicacionVacuna> Vacunas { get; set; } = new();
}