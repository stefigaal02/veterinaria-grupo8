using PetHealth.Domain.Enums;

namespace PetHealth.Domain.Entities;

public class Diagnostico
{
    public int Id { get; set; }
    public string Descripcion { get; set; } = string.Empty;
    public TipoDiagnostico Tipo { get; set; }

    public int ConsultaId { get; set; }
    public Consulta Consulta { get; set; } = null!;
}