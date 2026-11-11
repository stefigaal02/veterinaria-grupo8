using Microsoft.AspNetCore.Mvc;
using PetHealth.Application.DTOs;
using PetHealth.Application.Services;

namespace PetHealth.Api.Controllers;

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
}