using Microsoft.EntityFrameworkCore;
using PetHealth.Infrastructure.Data;
using PetHealth.Application.Interfaces;
using PetHealth.Application.Services;
using PetHealth.Infrastructure.Repositories;

var builder = WebApplication.CreateBuilder(args);

// Base de datos
var cs = builder.Configuration.GetConnectionString("Default");
builder.Services.AddDbContext<AppDbContext>(o => o.UseMySql(cs, ServerVersion.AutoDetect(cs)));

// Repositorios y servicios
builder.Services.AddScoped<IPropietarioRepository, PropietarioRepository>();
builder.Services.AddScoped<PropietarioService>();
builder.Services.AddScoped<IMascotaRepository, MascotaRepository>();   
builder.Services.AddScoped<MascotaService>(); 
builder.Services.AddScoped<IHistoriaClinicaRepository, HistoriaClinicaRepository>();
builder.Services.AddScoped<HistoriaClinicaService>();

builder.Services.AddControllers();
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

var app = builder.Build();

if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.UseHttpsRedirection();
app.UseAuthorization();
app.MapControllers();

app.Run();