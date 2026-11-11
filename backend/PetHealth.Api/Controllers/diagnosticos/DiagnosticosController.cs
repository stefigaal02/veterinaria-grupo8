using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using PetHealth.Application.DTOs;
using PetHealth.Application.Services;

namespace PetHealth.Api.Controllers;

[Authorize(Roles = "Veterinario,Administrador")]

[ApiController]
[Route("api/diagnosticos")]
public class DiagnosticosController : ControllerBase
{
    private readonly HistoriaClinicaService _service;

    public DiagnosticosController(HistoriaClinicaService service)
    {
        _service = service;
    }

    [HttpPost]
    public async Task<IActionResult> Crear(CrearDiagnosticoDto dto)
    {
        try
        {
            return StatusCode(201, await _service.CrearDiagnosticoAsync(dto));
        }
        catch (InvalidOperationException ex)
        {
            return BadRequest(new { mensaje = ex.Message });
        }
    }
}