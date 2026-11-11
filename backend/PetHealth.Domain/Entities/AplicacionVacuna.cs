namespace PetHealth.Domain.Entities;

public class AplicacionVacuna
{
    public int Id { get; set; }
    public DateTime FechaAplicacion { get; set; }
    public DateTime FechaRefuerzo { get; set; }
    public int MascotaId { get; set; }
    public Mascota? Mascota { get; set; }
    public int VacunaId { get; set; }
    public Vacuna? Vacuna { get; set; }
}