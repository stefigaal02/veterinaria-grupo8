using System.Security.Claims;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using PetHealth.Api.Seguridad;
using PetHealth.Application.DTOs;
using PetHealth.Application.Services;

namespace PetHealth.Api.Controllers;

[ApiController]
[Route("api/auth")]
public class AuthController : ControllerBase
{
    private readonly AuthService _service;
    private readonly IConfiguration _config;

    public AuthController(AuthService service, IConfiguration config)
    {
        _service = service;
        _config = config;
    }

    [HttpPost("registro")]
    public async Task<IActionResult> Registro(RegistroDto dto)
    {
        try
        {
            return StatusCode(201, await _service.RegistrarAsync(dto));
        }
        catch (InvalidOperationException ex)
        {
            return BadRequest(new { mensaje = ex.Message });
        }
    }

    [HttpPost("login")]
    public async Task<IActionResult> Login(LoginDto dto)
    {
        var usuario = await _service.LoginAsync(dto);
        if (usuario == null)
            return Unauthorized(new { mensaje = "Correo o contraseña incorrectos" });

        return Ok(new LoginRespuestaDto
        {
            Token = JwtHelper.GenerarToken(usuario, _config),
            Usuario = usuario
        });
    }

    [Authorize]
    [HttpGet("perfil")]
    public async Task<IActionResult> Perfil()
    {
        var id = int.Parse(User.FindFirstValue(ClaimTypes.NameIdentifier)!);
        var perfil = await _service.PerfilAsync(id);
        if (perfil == null) return NotFound(new { mensaje = "Usuario no encontrado" });
        return Ok(perfil);
    }
}