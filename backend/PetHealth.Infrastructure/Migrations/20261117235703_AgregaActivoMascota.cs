using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace PetHealth.Infrastructure.Migrations
{
    /// <inheritdoc />
    public partial class AgregaActivoMascota : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.AddColumn<bool>(
                name: "Activo",
                table: "Mascotas",
                type: "tinyint(1)",
                nullable: false,
                defaultValue: true);
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropColumn(
                name: "Activo",
                table: "Mascotas");
        }
    }
}
