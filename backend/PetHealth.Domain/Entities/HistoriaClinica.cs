namespace PetHealth.Domain.Entities;

public class HistoriaClinica
{
    public int Id { get; set; }
    public DateTime FechaApertura { get; set; }
    public int MascotaId { get; set; }
    public Mascota? Mascota { get; set; }
    public List<Consulta> Consultas { get; set; } = new();
}