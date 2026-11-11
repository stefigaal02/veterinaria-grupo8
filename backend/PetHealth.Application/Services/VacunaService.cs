using PetHealth.Application.DTOs;
using PetHealth.Application.Interfaces;
using PetHealth.Domain.Entities;

namespace PetHealth.Application.Services;

public class VacunaService
{
    private readonly IVacunaRepository _repo;
    private readonly IMascotaRepository _mascotas;

    public VacunaService(IVacunaRepository repo, IMascotaRepository mascotas)
    {
        _repo = repo;
        _mascotas = mascotas;
    }

    public async Task<List<VacunaDto>> ListarAsync()
    {
        var lista = await _repo.ListarAsync();
        return lista.Select(v => new VacunaDto { Id = v.Id, Nombre = v.Nombre }).ToList();
    }

    public async Task<VacunaDto> CrearAsync(CrearVacunaDto dto)
    {
        var v = new Vacuna { Nombre = dto.Nombre };
        await _repo.AgregarAsync(v);
        return new VacunaDto { Id = v.Id, Nombre = v.Nombre };
    }

    public async Task<AplicacionDto> AplicarAsync(AplicarVacunaDto dto)
    {
        var mascota = await _mascotas.ObtenerAsync(dto.MascotaId);
        if (mascota == null)
            throw new InvalidOperationException("La mascota no existe");

        var vacuna = await _repo.ObtenerAsync(dto.VacunaId);
        if (vacuna == null)
            throw new InvalidOperationException("La vacuna no existe");

        // Regla: el refuerzo tiene que ser después de la aplicación
        if (dto.FechaRefuerzo!.Value <= dto.FechaAplicacion!.Value)
            throw new InvalidOperationException("La fecha de refuerzo debe ser posterior a la de aplicación");

        var a = new AplicacionVacuna
        {
            MascotaId = dto.MascotaId,
            VacunaId = dto.VacunaId,
            FechaAplicacion = dto.FechaAplicacion.Value,
            FechaRefuerzo = dto.FechaRefuerzo.Value
        };
        await _repo.AgregarAplicacionAsync(a);

        return new AplicacionDto
        {
            Id = a.Id,
            MascotaId = mascota.Id,
            MascotaNombre = mascota.Nombre,
            VacunaId = vacuna.Id,
            VacunaNombre = vacuna.Nombre,
            FechaAplicacion = a.FechaAplicacion,
            FechaRefuerzo = a.FechaRefuerzo
        };
    }

    public async Task<List<AplicacionDto>> ListarPorMascotaAsync(int mascotaId)
    {
        var lista = await _repo.ListarPorMascotaAsync(mascotaId);
        return lista.Select(Convertir).ToList();
    }

    public async Task<List<AplicacionDto>> ListarProximasAsync(int dias)
    {
        var hoy = DateTime.Today;
        var lista = await _repo.ListarProximasAsync(hoy, hoy.AddDays(dias));
        return lista.Select(Convertir).ToList();
    }

    private static AplicacionDto Convertir(AplicacionVacuna a) => new()
    {
        Id = a.Id,
        MascotaId = a.MascotaId,
        MascotaNombre = a.Mascota?.Nombre ?? "",
        VacunaId = a.VacunaId,
        VacunaNombre = a.Vacuna?.Nombre ?? "",
        FechaAplicacion = a.FechaAplicacion,
        FechaRefuerzo = a.FechaRefuerzo
    };
}