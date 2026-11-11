namespace PetHealth.Domain.Entities;

public class Consulta
{
    public int Id { get; set; }
    public DateTime Fecha { get; set; }
    public string Motivo { get; set; } = string.Empty;
    public string Tratamiento { get; set; } = string.Empty;
    public int HistoriaClinicaId { get; set; }
    public HistoriaClinica? HistoriaClinica { get; set; }
    public List<Diagnostico> Diagnosticos { get; set; } = new();
}