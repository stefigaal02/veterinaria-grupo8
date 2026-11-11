namespace PetHealth.Domain.Entities;

public class Propietario
{
    public int Id { get; set; }
    public string Documento { get; set; } = string.Empty;
    public string Nombre { get; set; } = string.Empty;
    public string Telefono { get; set; } = string.Empty;
    public List<Mascota> Mascotas { get; set; } = new();
}