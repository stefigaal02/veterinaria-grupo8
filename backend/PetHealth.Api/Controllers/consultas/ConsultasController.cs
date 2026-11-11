using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using PetHealth.Application.DTOs;
using PetHealth.Application.Services;

namespace PetHealth.Api.Controllers;

[Authorize(Roles = "Veterinario,Administrador")]
[ApiController]
[Route("api/consultas")]
public class ConsultasController : ControllerBase
{
    private readonly HistoriaClinicaService _service;

    public ConsultasController(HistoriaClinicaService service)
    {
        _service = service;
    }

    [HttpPost]
    public async Task<IActionResult> Crear(CrearConsultaDto dto)
    {
        try
        {
            return StatusCode(201, await _service.CrearConsultaAsync(dto));
        }
        catch (InvalidOperationException ex)
        {
            return BadRequest(new { mensaje = ex.Message });
        }
    }
}