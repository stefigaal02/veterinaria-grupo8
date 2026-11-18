using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using PetHealth.Application.DTOs;
using PetHealth.Application.Services;

namespace PetHealth.Api.Controllers;

[Authorize]
[ApiController]
[Route("api/mascotas")]
public class MascotasController : ControllerBase
{
    private readonly MascotaService _service;

    public MascotasController(MascotaService service)
    {
        _service = service;
    }

    [HttpGet]
    public async Task<IActionResult> Listar() => Ok(await _service.ListarAsync());

    [HttpGet("{id}")]
    public async Task<IActionResult> Obtener(int id)
    {
        var m = await _service.ObtenerAsync(id);
        if (m == null) return NotFound(new { mensaje = "Mascota no encontrada" });
        return Ok(m);
    }

    [HttpPost]
    public async Task<IActionResult> Crear(CrearMascotaDto dto)
    {
        try
        {
            var creada = await _service.CrearAsync(dto);
            return CreatedAtAction(nameof(Obtener), new { id = creada.Id }, creada);
        }
        catch (InvalidOperationException ex)
        {
            return BadRequest(new { mensaje = ex.Message });
        }
    }

    [HttpPut("{id}")]
    public async Task<IActionResult> Actualizar(int id, CrearMascotaDto dto)
    {
        try
        {
            var m = await _service.ActualizarAsync(id, dto);
            if (m == null) return NotFound(new { mensaje = "Mascota no encontrada" });
            return Ok(m);
        }
        catch (InvalidOperationException ex)
        {
            return BadRequest(new { mensaje = ex.Message });
        }
    }

    [Authorize(Roles = "Administrador")]
    [HttpPut("{id}/anular")]
    public async Task<IActionResult> Anular(int id)
    {
        try
        {
            if (!await _service.AnularAsync(id)) return NotFound(new { mensaje = "Mascota no encontrada" });
            return Ok(new { mensaje = "Paciente anulado" });
        }
        catch (InvalidOperationException ex)
        {
            return BadRequest(new { mensaje = ex.Message });
        }
    }

    [Authorize(Roles = "Administrador")]
    [HttpPut("{id}/reactivar")]
    public async Task<IActionResult> Reactivar(int id)
    {
        try
        {
            if (!await _service.ReactivarAsync(id)) return NotFound(new { mensaje = "Mascota no encontrada" });
            return Ok(new { mensaje = "Paciente reactivado" });
        }
        catch (InvalidOperationException ex)
        {
            return BadRequest(new { mensaje = ex.Message });
        }
    }

    [Authorize(Roles = "Administrador")]
    [HttpGet("anulados")]
    public async Task<IActionResult> Anulados() => Ok(await _service.ListarAnuladasAsync());
}
