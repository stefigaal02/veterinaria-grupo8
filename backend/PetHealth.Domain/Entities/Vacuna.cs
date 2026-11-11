namespace PetHealth.Domain.Entities;

public class Vacuna
{
    public int Id { get; set; }
    public string Nombre { get; set; } = string.Empty;
    public string? Descripcion { get; set; }
    public string EspecieAplicable { get; set; } = string.Empty;
    public int DiasParaRefuerzo { get; set; }
}