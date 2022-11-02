package io.github.dejw.controller;

import io.github.dejw.model.Task;
import io.github.dejw.model.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

//kontroler do zarządzania interfejsem TaskRepository, potrzebny np do nadpisywania metod, bądź
//wrzucania logów, itp

@RestController
public class TaskController {
    //  dociągnięty logger, do tworzenia logów
    public static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    private final TaskRepository repository;

    TaskController(final TaskRepository repository) {
        this.repository = repository;
    }

    //    nadpisuje metode readAllTasks z TaskRepository i dodaje info do logów o wywołaniu tej metody
    //    żeby móc dalej korzystać z parametrów(np sortowanie), muszę wymusić ich ignorowanie przy wywołaniu tej metody
    //    w innym przypadku przystkie GETy, niezależnie od paramów będę wystawiały tylko liste tasków(np.nieposortowaną)
    @RequestMapping(method = RequestMethod.GET, path = "/tasks", params = {"!sort", "!page", "!size"})
    ResponseEntity<List<Task>> readAllTasks() {
        logger.warn("Exposing all tasks!");
        return ResponseEntity.ok(repository.findAll());
    }

    // sortowanie i stronicowanie przerzucone do kotrolera, a nie do repository
    @GetMapping(path = "/tasks")
    ResponseEntity<List<Task>> readAllTasks(Pageable page) {
        logger.info("Custom pageable");
        return ResponseEntity.ok(repository.findAll(page).getContent());
    }
    @PutMapping("/tasks/{id}") // ↓ uproszczona wersja tego wyrażenia -> @PathVariable("id") int taskId
    ResponseEntity<?> updateTask(@PathVariable int id, @RequestBody @Valid Task toUpdate) {
        if (!repository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        toUpdate.setId(id);
        repository.save(toUpdate);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/tasks/{id}")
    ResponseEntity<?> getTask(@PathVariable int id){
        if(repository.findById(id).isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(repository.findById(id).get());
    }
    @PostMapping("/tasks")
    ResponseEntity<?> saveTask(@RequestBody @Valid Task taskToSave){
        Task saved = repository.save(taskToSave);

        return ResponseEntity.created(URI.create("/"+saved.getId())).body(saved);
    }
}
