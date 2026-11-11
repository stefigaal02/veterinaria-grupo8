using PetHealth.Application.DTOs;
using PetHealth.Application.Interfaces;
using PetHealth.Domain.Entities;
using System.Text;


namespace PetHealth.Application.Services;

public class HistoriaClinicaService
{
    private readonly IHistoriaClinicaRepository _repo;
    private readonly IMascotaRepository _mascotas;
    private readonly IVacunaRepository _vacunas;

    public HistoriaClinicaService(IHistoriaClinicaRepository repo, IMascotaRepository mascotas, IVacunaRepository vacunas)
    {
    _repo = repo;
    _mascotas = mascotas;
    _vacunas = vacunas;
    }
    public async Task<HistoriaDto> AbrirAsync(AbrirHistoriaDto dto)
    {
        var mascota = await _mascotas.ObtenerAsync(dto.MascotaId);
        if (mascota == null)
            throw new InvalidOperationException("La mascota no existe");

        if (await _repo.ObtenerPorMascotaAsync(dto.MascotaId) != null)
            throw new InvalidOperationException("La mascota ya tiene historia clínica");

        var h = new HistoriaClinica
        {
            MascotaId = dto.MascotaId,
            FechaApertura = DateTime.Now
        };
        await _repo.AgregarHistoriaAsync(h);

        return new HistoriaDto
        {
            Id = h.Id,
            MascotaId = mascota.Id,
            MascotaNombre = mascota.Nombre,
            FechaApertura = h.FechaApertura
        };
    }

    public async Task<HistoriaDto?> ObtenerPorMascotaAsync(int mascotaId)
    {
        var h = await _repo.ObtenerPorMascotaAsync(mascotaId);
        if (h == null) return null;

        return new HistoriaDto
        {
            Id = h.Id,
            MascotaId = h.MascotaId,
            MascotaNombre = h.Mascota?.Nombre ?? "",
            FechaApertura = h.FechaApertura,
            // Cronología: de la consulta más reciente a la más antigua
            Consultas = h.Consultas
                .OrderByDescending(c => c.Fecha)
                .Select(ConvertirConsulta)
                .ToList()
        };
    }

    public async Task<ConsultaDto> CrearConsultaAsync(CrearConsultaDto dto)
    {
        if (!await _repo.ExisteHistoriaAsync(dto.HistoriaClinicaId))
            throw new InvalidOperationException("La historia clínica no existe");

        var c = new Consulta
        {
            HistoriaClinicaId = dto.HistoriaClinicaId,
            Fecha = DateTime.Now,
            Motivo = dto.Motivo,
            Tratamiento = dto.Tratamiento
        };
        await _repo.AgregarConsultaAsync(c);
        return ConvertirConsulta(c);
    }

    public async Task<DiagnosticoDto> CrearDiagnosticoAsync(CrearDiagnosticoDto dto)
    {
        if (!await _repo.ExisteConsultaAsync(dto.ConsultaId))
            throw new InvalidOperationException("La consulta no existe");

        var d = new Diagnostico
        {
            ConsultaId = dto.ConsultaId,
            Descripcion = dto.Descripcion
        };
        await _repo.AgregarDiagnosticoAsync(d);

        return new DiagnosticoDto
        {
            Id = d.Id,
            ConsultaId = d.ConsultaId,
            Descripcion = d.Descripcion
        };
    }

    public async Task<string?> GenerarResumenAsync(int mascotaId)
{
    var h = await _repo.ObtenerPorMascotaAsync(mascotaId);
    if (h == null) return null;

    var vacunas = await _vacunas.ListarPorMascotaAsync(mascotaId);
    var m = h.Mascota;
    var sb = new StringBuilder();

    sb.AppendLine("RESUMEN CLÍNICO - PetHealth Cloud S.A.S.");
    sb.AppendLine($"Generado: {DateTime.Now:dd/MM/yyyy HH:mm}");
    sb.AppendLine();
    sb.AppendLine($"Paciente: {m?.Nombre} ({m?.Especie} - {m?.Raza})");
    sb.AppendLine($"Propietario: {m?.Propietario?.Nombre} - Tel. {m?.Propietario?.Telefono}");
    sb.AppendLine($"Historia abierta el: {h.FechaApertura:dd/MM/yyyy}");
    sb.AppendLine();

    sb.AppendLine("CONSULTAS");
    if (!h.Consultas.Any()) sb.AppendLine("  Sin consultas registradas");
    foreach (var c in h.Consultas.OrderByDescending(c => c.Fecha))
    {
        sb.AppendLine($"- {c.Fecha:dd/MM/yyyy}: {c.Motivo}");
        foreach (var d in c.Diagnosticos)
            sb.AppendLine($"    Diagnóstico: {d.Descripcion}");
        if (!string.IsNullOrWhiteSpace(c.Tratamiento))
            sb.AppendLine($"    Tratamiento: {c.Tratamiento}");
    }
    sb.AppendLine();

    sb.AppendLine("VACUNAS");
    if (!vacunas.Any()) sb.AppendLine("  Sin vacunas registradas");
    foreach (var v in vacunas)
        sb.AppendLine($"- {v.Vacuna?.Nombre}: aplicada {v.FechaAplicacion:dd/MM/yyyy}, refuerzo {v.FechaRefuerzo:dd/MM/yyyy}");

    return sb.ToString();
}

    private static ConsultaDto ConvertirConsulta(Consulta c) => new()
    {
        Id = c.Id,
        Fecha = c.Fecha,
        Motivo = c.Motivo,
        Tratamiento = c.Tratamiento,
        Diagnosticos = c.Diagnosticos.Select(d => d.Descripcion).ToList()
    };
}