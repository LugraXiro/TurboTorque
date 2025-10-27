@echo off
REM ==========================================================
REM Script: generar_estructura_app.cmd
REM Genera un archivo estructura_app.txt solo con la carpeta app
REM y su contenido (src, res, manifests, etc.)
REM ==========================================================

echo Estructura de la carpeta APP - %date% %time% > estructura_app.txt
echo. >> estructura_app.txt

if exist app (
    tree app /F /A >> estructura_app.txt
    echo. >> estructura_app.txt
    echo Archivo generado correctamente: estructura_app.txt
) else (
    echo ERROR: No se encontró la carpeta "app"
)
