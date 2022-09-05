package io.github.dejw.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource
public interface TaskRepository extends JpaRepository<Task, Integer> {

    // blocking possibility to delete tasks
    @Override
    @RestResource(exported = false)
    void deleteById(Integer integer);
    @Override
    @RestResource(exported = false)
    void delete(Task entity);
//    wyszukiwanie tasków w liście po właściwości 'done' true of false
//    nazwa parametru 'state'
//    ścieżka w przeglądarce: 'localhost:8080/tasks/search/done?state=true' lub false
    @RestResource(path = "done", rel = "done")
    List<Task> findByDone(@Param("state") boolean done);
}
