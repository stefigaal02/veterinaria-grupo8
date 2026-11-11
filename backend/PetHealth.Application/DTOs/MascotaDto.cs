using System.ComponentModel.DataAnnotations;

namespace PetHealth.Application.DTOs;

public class MascotaDto
{
    public int Id { get; set; }
    public string Nombre { get; set; } = string.Empty;
    public string Especie { get; set; } = string.Empty;
    public string Raza { get; set; } = string.Empty;
    public int PropietarioId { get; set; }
    public string PropietarioNombre { get; set; } = string.Empty;
}

public class CrearMascotaDto
{
    [Required(ErrorMessage = "El nombre es obligatorio")]
    public string Nombre { get; set; } = string.Empty;

    [Required(ErrorMessage = "La especie es obligatoria")]
    public string Especie { get; set; } = string.Empty;

    public string Raza { get; set; } = string.Empty;

    [Range(1, int.MaxValue, ErrorMessage = "Debe seleccionar un propietario")]
    public int PropietarioId { get; set; }
}