using PetHealth.Application.DTOs;
using PetHealth.Application.Interfaces;
using PetHealth.Domain.Entities;

namespace PetHealth.Application.Services;

public class MascotaService
{
    private readonly IMascotaRepository _repo;
    private readonly IPropietarioRepository _propietarios;

    public MascotaService(IMascotaRepository repo, IPropietarioRepository propietarios)
    {
        _repo = repo;
        _propietarios = propietarios;
    }

    public async Task<List<MascotaDto>> ListarAsync()
    {
        var lista = await _repo.ListarAsync();
        return lista.Select(Convertir).ToList();
    }

    public async Task<MascotaDto?> ObtenerAsync(int id)
    {
        var m = await _repo.ObtenerAsync(id);
        return m == null ? null : Convertir(m);
    }

    public async Task<MascotaDto> CrearAsync(CrearMascotaDto dto)
    {
        var propietario = await _propietarios.ObtenerAsync(dto.PropietarioId);
        if (propietario == null)
            throw new InvalidOperationException("El propietario no existe");

        var m = new Mascota
        {
            Nombre = dto.Nombre,
            Especie = dto.Especie,
            Raza = dto.Raza,
            PropietarioId = dto.PropietarioId,
            Propietario = propietario
        };
        await _repo.AgregarAsync(m);
        return Convertir(m);
    }

    private static MascotaDto Convertir(Mascota m) => new()
    {
        Id = m.Id,
        Nombre = m.Nombre,
        Especie = m.Especie,
        Raza = m.Raza,
        PropietarioId = m.PropietarioId,
        PropietarioNombre = m.Propietario?.Nombre ?? ""
    };
}