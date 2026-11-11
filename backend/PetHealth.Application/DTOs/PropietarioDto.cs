using System.ComponentModel.DataAnnotations;

namespace PetHealth.Application.DTOs;

public class PropietarioDto
{
    public int Id { get; set; }
    public string Documento { get; set; } = string.Empty;
    public string Nombre { get; set; } = string.Empty;
    public string Telefono { get; set; } = string.Empty;
}

public class CrearPropietarioDto
{
    [Required(ErrorMessage = "El documento es obligatorio")]
    public string Documento { get; set; } = string.Empty;

    [Required(ErrorMessage = "El nombre es obligatorio")]
    public string Nombre { get; set; } = string.Empty;

    [Required(ErrorMessage = "El teléfono es obligatorio")]
    public string Telefono { get; set; } = string.Empty;
}