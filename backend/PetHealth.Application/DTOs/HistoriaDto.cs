using System.ComponentModel.DataAnnotations;

namespace PetHealth.Application.DTOs;

public class AbrirHistoriaDto
{
    [Range(1, int.MaxValue, ErrorMessage = "Debe seleccionar una mascota")]
    public int MascotaId { get; set; }
}

public class HistoriaDto
{
    public int Id { get; set; }
    public int MascotaId { get; set; }
    public string MascotaNombre { get; set; } = string.Empty;
    public DateTime FechaApertura { get; set; }
    public List<ConsultaDto> Consultas { get; set; } = new();
}

public class CrearConsultaDto
{
    [Range(1, int.MaxValue, ErrorMessage = "Debe indicar la historia clínica")]
    public int HistoriaClinicaId { get; set; }

    [Required(ErrorMessage = "El motivo es obligatorio")]
    public string Motivo { get; set; } = string.Empty;

    public string Tratamiento { get; set; } = string.Empty;
}

public class ConsultaDto
{
    public int Id { get; set; }
    public DateTime Fecha { get; set; }
    public string Motivo { get; set; } = string.Empty;
    public string Tratamiento { get; set; } = string.Empty;
    public List<string> Diagnosticos { get; set; } = new();
}

public class CrearDiagnosticoDto
{
    [Range(1, int.MaxValue, ErrorMessage = "Debe indicar la consulta")]
    public int ConsultaId { get; set; }

    [Required(ErrorMessage = "La descripción es obligatoria")]
    public string Descripcion { get; set; } = string.Empty;
}

public class DiagnosticoDto
{
    public int Id { get; set; }
    public int ConsultaId { get; set; }
    public string Descripcion { get; set; } = string.Empty;
}