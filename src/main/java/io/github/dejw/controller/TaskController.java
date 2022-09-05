package io.github.dejw.controller;

import io.github.dejw.model.Task;
import io.github.dejw.model.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

//kontroler do zarządzania interfejsem TaskRepository, potrzebny np do nadpisywania metod, bądź
//wrzucania logów, itp
@RepositoryRestController
public class TaskController {
//  dociągnięty logger, do tworzenia logów
    public static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    private final TaskRepository repository;

    TaskController(final TaskRepository repository){
        this.repository = repository;
    }
//    nadpisuje metode readAllTasks z TaskRepository i dodaje info do logów o wywołaniu tej metody
//    żeby móc dalej korzystać z parametrów(np sortowanie), muszę wymusić ich ignorowanie przy wywołaniu tej metody
//    w innym przypadku przystkie GETy, niezależnie od paramów będę wystawiały tylko liste tasków(np.nieposortowaną)
    @RequestMapping(method = RequestMethod.GET, path = "/tasks", params = {"!sort", "!page", "!size"})
    ResponseEntity<List<Task>> readAllTasks(){
        logger.warn("Exposing all tasks!");
        return ResponseEntity.ok(repository.findAll());
    }
// sortowanie i stronicowanie przerzucone do kotrolera, a nie do repository
    @GetMapping( path = "/tasks")
    ResponseEntity<List<Task>> readAllTasks(Pageable page){
        logger.info("Custom pageable");
        return ResponseEntity.ok(repository.findAll(page).getContent());
    }
}
