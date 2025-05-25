package backend.assignment.controller;

import backend.assignment.model.Word;
import backend.assignment.service.WordService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/words")
public class WordController {
    private final WordService service;

    public WordController(WordService service) {
        this.service = service;
    }

    @GetMapping
    public List<Word> all() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Word one(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public ResponseEntity<Word> create(@RequestBody Word Word) {
        Word saved = service.create(Word);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public Word update(@PathVariable Long id, @RequestBody Word Word) {
        return service.update(id, Word);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
