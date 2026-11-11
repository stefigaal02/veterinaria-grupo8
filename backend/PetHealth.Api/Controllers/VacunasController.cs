using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using PetHealth.Application.DTOs;
using PetHealth.Application.Services;

namespace PetHealth.Api.Controllers;
[Authorize]
[ApiController]
[Route("api/vacunas")]
public class VacunasController : ControllerBase
{
    private readonly VacunaService _service;

    public VacunasController(VacunaService service)
    {
        _service = service;
    }

    [HttpGet]
    public async Task<IActionResult> Listar() => Ok(await _service.ListarAsync());

[Authorize(Roles = "Administrador")]

    [HttpPost]
    public async Task<IActionResult> Crear(CrearVacunaDto dto) =>
        StatusCode(201, await _service.CrearAsync(dto));

    [HttpPost("aplicaciones")]
    public async Task<IActionResult> Aplicar(AplicarVacunaDto dto)
    {
        try
        {
            return StatusCode(201, await _service.AplicarAsync(dto));
        }
        catch (InvalidOperationException ex)
        {
            return BadRequest(new { mensaje = ex.Message });
        }
    }

    [HttpGet("aplicaciones/{mascotaId}")]
    public async Task<IActionResult> PorMascota(int mascotaId) =>
        Ok(await _service.ListarPorMascotaAsync(mascotaId));

    [HttpGet("proximas")]
    public async Task<IActionResult> Proximas(int dias = 30) =>
        Ok(await _service.ListarProximasAsync(dias));
}