package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.exception.accionInvalida.*;
import ar.edu.unq.epersgeist.exception.notFound.EspirituNoEncontradoException;
import ar.edu.unq.epersgeist.exception.notFound.HabilidadNoEncontradaException;
import ar.edu.unq.epersgeist.modelo.Condicion;
import ar.edu.unq.epersgeist.modelo.Evaluacion;
import ar.edu.unq.epersgeist.modelo.Habilidad;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.persistencia.dao.EspirituDAO;
import ar.edu.unq.epersgeist.persistencia.dao.HabilidadDAO;
import ar.edu.unq.epersgeist.servicios.HabilidadService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class HabilidadServiceImpl implements HabilidadService {

    private final HabilidadDAO habilidadDAO;
    private final EspirituDAO espirituDAO;

    public HabilidadServiceImpl(HabilidadDAO habilidadDAO, EspirituDAO espirituDAO) {
        this.habilidadDAO = habilidadDAO;
        this.espirituDAO = espirituDAO;
    }

    @Override
    public Habilidad crear(Habilidad habilidad) {
        try{
            return habilidadDAO.save(habilidad);
        }catch (DataIntegrityViolationException e){
            throw new HabilidadDuplicadaException(habilidad.getNombre());
        }
    }

    @Override
    public Habilidad recuperar(String nombre) {
        Habilidad habilidadRecuperada = habilidadDAO.recuperar(nombre);
        if (habilidadRecuperada == null) throw new HabilidadNoEncontradaException(nombre);
        return habilidadRecuperada;
    }

    @Override
    public Evaluacion recuperarEvaluacionDeCondicionDe(Habilidad habilidad, Habilidad habilidadDestino) {
        return habilidadDAO.evaluacionDeCondicionDeLaHabilidad(habilidad.getNombre(),habilidadDestino.getNombre());
    }

    @Override
    public Integer recuperarCantidadDeCondicionDe(Habilidad habilidad, Habilidad habilidadDestino) {
        return habilidadDAO.cantidadDeCondicionDeLaHabilidad(habilidad.getNombre(),habilidadDestino.getNombre());
    }

    @Override
    public void clearAll() {
        habilidadDAO.deleteAll();
    }

    @Override
    public void eliminar(String nombre){
        habilidadDAO.delete(habilidadDAO.recuperar(nombre));
    }

    @Override
    public void descubrirHabilidad(String nombreHabilidadOrigen, String nombreHabilidadDestino, Condicion condicion) {
        if (!habilidadDAO.habilidadExiste(nombreHabilidadOrigen)) {
            throw new HabilidadNoEncontradaException(nombreHabilidadOrigen);
        }
        if (!habilidadDAO.habilidadExiste(nombreHabilidadDestino)) {
            throw new HabilidadNoEncontradaException(nombreHabilidadDestino);
        }
        if(habilidadDAO.estanConectados(nombreHabilidadDestino, nombreHabilidadOrigen)) throw new HabilidadesYaConectadasException(nombreHabilidadDestino, nombreHabilidadOrigen);
        if(Objects.equals(nombreHabilidadOrigen, nombreHabilidadDestino)) throw new MismaHabilidadException(nombreHabilidadOrigen);
        habilidadDAO.descubrirHabilidad(nombreHabilidadOrigen, nombreHabilidadDestino, condicion.getEvaluacion(), condicion.getCantidad());
    }



    @Override
    public void evolucionar(Long espirituId) {
        Espiritu espRecuperado = espirituDAO.findById(espirituId).orElseThrow(() -> new EspirituNoEncontradoException(espirituId));
        List<String> habilidadesAMutar = new ArrayList<String>();
        if (espRecuperado.getHabilidades().isEmpty()){
            habilidadesAMutar = habilidadDAO.nombresHabilidadesRaices();
        }else{
            habilidadesAMutar = habilidadDAO.nombresDeHabilidadesPosibles(espRecuperado.getHabilidades(),
                    espRecuperado.getEnergia(), espRecuperado.getNivelDeConexion(),
                    espRecuperado.getCantExorcismosEvitados(), espRecuperado.getCantExorcismosHechos());
        }
        if (habilidadesAMutar.isEmpty()) return;
        espRecuperado.mutarA(habilidadesAMutar);
        espirituDAO.save(espRecuperado);
    }

    @Override
    public Set<Habilidad> habilidadesConectadas(String nombreHabilidad) {
        return habilidadDAO.habilidadesConectadas(nombreHabilidad);
    }

    @Override
    public Set<Habilidad> habilidadesPosibles(Long espirituId) {
        Espiritu espRecuperado = espirituDAO.findById(espirituId).orElseThrow(() -> new EspirituNoEncontradoException(espirituId));
        if(espRecuperado.getHabilidades().isEmpty()){
            return habilidadDAO.habilidadesRaices();
        }
        return habilidadDAO.habilidadesPosibles(espRecuperado.getHabilidades(), espRecuperado.getEnergia(), espRecuperado.getNivelDeConexion(),
                espRecuperado.getCantExorcismosEvitados(), espRecuperado.getCantExorcismosHechos());
    }

    @Override
    public List<Habilidad> caminoMasRentable(String nombreHabilidadOrigen, String nombreHabilidadDestino, Set<Evaluacion> evaluaciones) {
        if(evaluaciones.isEmpty()) throw new ListaDeEvaluacionesVaciaException();
        if (!habilidadDAO.estanConectados(nombreHabilidadOrigen, nombreHabilidadDestino)) throw new HabilidadesNoConectadasException(nombreHabilidadOrigen, nombreHabilidadDestino);
        List<Habilidad> camino = habilidadDAO.caminoMasRentable(nombreHabilidadOrigen, nombreHabilidadDestino, evaluaciones);
        if (camino.isEmpty()) throw new MutacionImposibleException();
        return camino;
    }

    @Override
    public List<Habilidad> caminoMasMutable(Long espirituId, String nombreHabilidad) {
        Espiritu espRecuperado = espirituDAO.findById(espirituId).orElseThrow(() -> new EspirituNoEncontradoException(espirituId));
        if (!espRecuperado.getHabilidades().contains(nombreHabilidad)) throw new HabilidadNoMutadaException(nombreHabilidad);
        return habilidadDAO.caminoMasMutable(nombreHabilidad, espRecuperado.getEnergia(), espRecuperado.getNivelDeConexion(),
                                             espRecuperado.getCantExorcismosEvitados(), espRecuperado.getCantExorcismosHechos());
    }

    @Override
    public List<Habilidad> caminoMenosMutable(String nombreHabilidad, Long espirituId) {
        Espiritu espRecuperado = espirituDAO.findById(espirituId).orElseThrow(() -> new EspirituNoEncontradoException(espirituId));
        if (!espRecuperado.getHabilidades().contains(nombreHabilidad)) throw new HabilidadNoMutadaException(nombreHabilidad);
        if (!habilidadDAO.tieneRelacionLindante(nombreHabilidad)) return List.of();
        return habilidadDAO.caminoMenosMutable(nombreHabilidad, espRecuperado.getEnergia(), espRecuperado.getNivelDeConexion(),
                espRecuperado.getCantExorcismosEvitados(), espRecuperado.getCantExorcismosHechos());
    }


    @Override
    public List<Habilidad> recuperarTodos() {
        return habilidadDAO.findAll();
    }
}
