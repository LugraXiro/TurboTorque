@echo off
REM ==========================================================
REM Script: generar_estructura_total.cmd
REM Genera un archivo estructura_total.txt con todo el árbol
REM del proyecto Android (todas las carpetas y archivos)
REM ==========================================================

echo Estructura completa del proyecto - %date% %time% > estructura_total.txt
echo. >> estructura_total.txt

tree /F /A >> estructura_total.txt

echo. >> estructura_total.txt
echo Archivo generado correctamente: estructura_total.txt
