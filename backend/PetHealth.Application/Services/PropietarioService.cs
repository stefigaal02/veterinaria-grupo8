using PetHealth.Application.DTOs;
using PetHealth.Application.Interfaces;
using PetHealth.Domain.Entities;

namespace PetHealth.Application.Services;

public class PropietarioService
{
    private readonly IPropietarioRepository _repo;

    public PropietarioService(IPropietarioRepository repo)
    {
        _repo = repo;
    }

    public async Task<List<PropietarioDto>> ListarAsync()
    {
        var lista = await _repo.ListarAsync();
        return lista.Select(Convertir).ToList();
    }

    public async Task<PropietarioDto?> ObtenerAsync(int id)
    {
        var p = await _repo.ObtenerAsync(id);
        return p == null ? null : Convertir(p);
    }

    public async Task<PropietarioDto> CrearAsync(CrearPropietarioDto dto)
    {
        if (await _repo.ExisteDocumentoAsync(dto.Documento))
            throw new InvalidOperationException("Ya existe un propietario con ese documento");

        var p = new Propietario
        {
            Documento = dto.Documento,
            Nombre = dto.Nombre,
            Telefono = dto.Telefono
        };
        await _repo.AgregarAsync(p);
        return Convertir(p);
    }

    private static PropietarioDto Convertir(Propietario p) => new()
    {
        Id = p.Id,
        Documento = p.Documento,
        Nombre = p.Nombre,
        Telefono = p.Telefono
    };
}