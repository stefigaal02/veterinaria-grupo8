using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using PetHealth.Application.DTOs;
using PetHealth.Application.Services;

namespace PetHealth.Api.Controllers;

[Authorize]
[ApiController]
[Route("api/propietarios")]
public class PropietariosController : ControllerBase
{
    private readonly PropietarioService _service;

    public PropietariosController(PropietarioService service)
    {
        _service = service;
    }

    [HttpGet]
    public async Task<IActionResult> Listar() => Ok(await _service.ListarAsync());

    [HttpGet("{id}")]
    public async Task<IActionResult> Obtener(int id)
    {
        var p = await _service.ObtenerAsync(id);
        if (p == null) return NotFound(new { mensaje = "Propietario no encontrado" });
        return Ok(p);
    }

    [HttpPost]
    public async Task<IActionResult> Crear(CrearPropietarioDto dto)
    {
        try
        {
            var creado = await _service.CrearAsync(dto);
            return CreatedAtAction(nameof(Obtener), new { id = creado.Id }, creado);
        }
        catch (InvalidOperationException ex)
        {
            return BadRequest(new { mensaje = ex.Message });
        }
    }
}