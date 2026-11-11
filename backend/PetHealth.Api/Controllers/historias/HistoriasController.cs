using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using PetHealth.Application.DTOs;
using PetHealth.Application.Services;

namespace PetHealth.Api.Controllers;

[Authorize]
[ApiController]
[Route("api/historias")]
public class HistoriasController : ControllerBase
{
    private readonly HistoriaClinicaService _service;

    public HistoriasController(HistoriaClinicaService service)
    {
        _service = service;
    }

    [HttpGet("{mascotaId}")]
    public async Task<IActionResult> ObtenerPorMascota(int mascotaId)
    {
        var h = await _service.ObtenerPorMascotaAsync(mascotaId);
        if (h == null) return NotFound(new { mensaje = "La mascota no tiene historia clínica" });
        return Ok(h);
    }

    [Authorize(Roles = "Veterinario,Administrador")]
    
    [HttpPost]
    public async Task<IActionResult> Abrir(AbrirHistoriaDto dto)
    {
        try
        {
            return StatusCode(201, await _service.AbrirAsync(dto));
        }
        catch (InvalidOperationException ex)
        {
            return BadRequest(new { mensaje = ex.Message });
        }
    }
}