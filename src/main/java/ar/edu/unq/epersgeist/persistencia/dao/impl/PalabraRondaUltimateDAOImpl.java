package ar.edu.unq.epersgeist.persistencia.dao.impl;

import ar.edu.unq.epersgeist.exception.notFound.RondaNoEncontradaException;
import ar.edu.unq.epersgeist.modelo.PalabraRondaUltimate;
import ar.edu.unq.epersgeist.persistencia.dao.PalabraRondaUltimateDAO;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.ListenerRegistration;
import com.google.cloud.firestore.WriteResult;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Repository
public class PalabraRondaUltimateDAOImpl implements PalabraRondaUltimateDAO {

    private final Firestore db;

    public PalabraRondaUltimateDAOImpl(Firestore db) {
        this.db = db;
    }

    @Override
    public Mono<Void> crearPalabraAdivinando(PalabraRondaUltimate palabra) {
        // Generar un ID único para el documento
        String idGenerado = db.collection("palabrasRondaFinal").document().getId();

        // Establecer el ID generado en el objeto palabra
        palabra.setId(idGenerado); // Asegúrate de que la clase PalabraRondaUltimate tenga un setter para el ID

        // Crear el documento con el ID generado
        ApiFuture<WriteResult> future = db.collection("palabrasRondaFinal").document(idGenerado).set(palabra);

        // Convertir el ApiFuture a un Mono
        return Mono.fromCallable(future::get).then();
    }


    @Override
    public Flux<String> palabraAdivinando(String id) {
        return Flux.create(sink -> {
            ListenerRegistration registration = db.collection("palabrasRondaFinal")
                    .whereEqualTo("id", id)
                    //Agrega el listener
                    .addSnapshotListener((querySnapshot, e) -> {
                        if (e != null) {
                            // Emite el error al flujo
                            sink.error(e);
                            return;
                        }
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            // Procesa el primer documento del resultado
                            DocumentSnapshot doc = querySnapshot.getDocuments().get(0);
                            PalabraRondaUltimate palabra = doc.toObject(PalabraRondaUltimate.class);
                            sink.next(palabra.getPalabraAdivinando());
                        }
                    });

            // Elimina el listener al cancelar la suscripción
            sink.onDispose(registration::remove);
        });
    }

    @Override
    public Mono<PalabraRondaUltimate> actualizarPalabraAdivinando(PalabraRondaUltimate palabra) {
        ApiFuture<WriteResult> future = db.collection("palabrasRondaFinal").document(palabra.getId()).set(palabra);
        return Mono.fromCallable(future::get)
                .flatMap(writeResult -> {
                    return Mono.just(palabra);
                });
    }

    @Override
    public Mono<PalabraRondaUltimate> recuperar(String idPalabraRondaUltimate) {
        ApiFuture<DocumentSnapshot> future = db.collection("palabrasRondaFinal").document(idPalabraRondaUltimate).get();

        return Mono.fromCallable(future::get)
                .flatMap(document -> {
                    if (!document.exists()) return Mono.error(new RondaNoEncontradaException(idPalabraRondaUltimate));
                    PalabraRondaUltimate palabra = document.toObject(PalabraRondaUltimate.class);
                    return Mono.just(palabra);
                });
    }

    @Override
    public Flux<String> letrasUsadas(String id) {
        return Flux.create(sink -> {
            ListenerRegistration registration = db.collection("palabrasRondaFinal")
                    .whereEqualTo("id", id)
                    .addSnapshotListener((querySnapshot, e) -> {
                        if (e != null) {
                            // Emitir el error al flujo
                            sink.error(e);
                            return;
                        }
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            // Procesar el primer documento del resultado
                            DocumentSnapshot doc = querySnapshot.getDocuments().get(0);
                            PalabraRondaUltimate palabra = doc.toObject(PalabraRondaUltimate.class);
                            sink.next(palabra.getLetrasUsadas());
                        }
                    });

            // Eliminar el listener al cancelar la suscripción
            sink.onDispose(registration::remove);
        });
    }


}
