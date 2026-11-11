using System.ComponentModel.DataAnnotations;

namespace PetHealth.Application.DTOs;

public class VacunaDto
{
    public int Id { get; set; }
    public string Nombre { get; set; } = string.Empty;
}

public class CrearVacunaDto
{
    [Required(ErrorMessage = "El nombre es obligatorio")]
    public string Nombre { get; set; } = string.Empty;
}

public class AplicarVacunaDto
{
    [Range(1, int.MaxValue, ErrorMessage = "Debe seleccionar una mascota")]
    public int MascotaId { get; set; }

    [Range(1, int.MaxValue, ErrorMessage = "Debe seleccionar una vacuna")]
    public int VacunaId { get; set; }

    [Required(ErrorMessage = "La fecha de aplicación es obligatoria")]
    public DateTime? FechaAplicacion { get; set; }

    [Required(ErrorMessage = "La fecha de refuerzo es obligatoria")]
    public DateTime? FechaRefuerzo { get; set; }
}

public class AplicacionDto
{
    public int Id { get; set; }
    public int MascotaId { get; set; }
    public string MascotaNombre { get; set; } = string.Empty;
    public int VacunaId { get; set; }
    public string VacunaNombre { get; set; } = string.Empty;
    public DateTime FechaAplicacion { get; set; }
    public DateTime FechaRefuerzo { get; set; }
}