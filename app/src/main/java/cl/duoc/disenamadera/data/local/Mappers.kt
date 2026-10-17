package cl.duoc.disenamadera.data.local

import cl.duoc.disenamadera.data.local.entity.ConsultaTecnicaEntity
import cl.duoc.disenamadera.data.local.entity.HistorialConSolucion
import cl.duoc.disenamadera.data.local.entity.NormativaEntity
import cl.duoc.disenamadera.data.local.entity.RecursoEntity
import cl.duoc.disenamadera.data.local.entity.SolucionEntity
import cl.duoc.disenamadera.data.model.ConsultaTecnica
import cl.duoc.disenamadera.data.model.EntradaHistorial
import cl.duoc.disenamadera.data.model.Normativa
import cl.duoc.disenamadera.data.model.Recurso
import cl.duoc.disenamadera.data.model.Solucion
import cl.duoc.disenamadera.data.model.TipoRecurso

// Conversión entidad (tabla) <-> modelo de dominio, para que la UI nunca dependa de Room.

fun SolucionEntity.toModel() = Solucion(
    id, nombre, descripcion, sistemaConstructivo, material, areaTematica, aplicacion, imagen, destacada
)

fun NormativaEntity.toModel() = Normativa(id, codigo, titulo, descripcion, url)

fun RecursoEntity.toModel() = Recurso(id, titulo, TipoRecurso.valueOf(tipo), descripcion, url)

fun HistorialConSolucion.toModel() = EntradaHistorial(historial.id, solucion.toModel(), historial.fecha)

fun ConsultaTecnicaEntity.toModel() = ConsultaTecnica(
    id, nombre, correo, telefono, perfil, solucionId, mensaje, fotoUri, fecha
)

fun ConsultaTecnica.toEntity() = ConsultaTecnicaEntity(
    id, nombre, correo, telefono, perfil, solucionId, mensaje, fotoUri, fecha
)
