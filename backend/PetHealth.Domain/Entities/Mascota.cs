namespace PetHealth.Domain.Entities;

public class Mascota
{
    public int Id { get; set; }
    public string Nombre { get; set; } = string.Empty;
    public string Especie { get; set; } = string.Empty;
    public string Raza { get; set; } = string.Empty;
    public int PropietarioId { get; set; }
    public Propietario? Propietario { get; set; }
    public HistoriaClinica? HistoriaClinica { get; set; }
}