using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using PetHealth.Application.DTOs;
using PetHealth.Application.Services;
using System.Text;

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
    [HttpGet("{mascotaId}/resumen")]
    public async Task<IActionResult> Resumen(int mascotaId)
    {
        var texto = await _service.GenerarResumenAsync(mascotaId);
        if (texto == null) return NotFound(new { mensaje = "La mascota no tiene historia clínica" });

        var bytes = Encoding.UTF8.GetBytes(texto);
        return File(bytes, "text/plain; charset=utf-8", $"resumen-clinico-{mascotaId}.txt");
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