package backend.assignment.service;

import backend.assignment.model.Word;
import java.util.List;

public interface WordService {
    List<Word> getAll();
    Word getById(Long id);
    Word create(Word word);
    Word update(Long id, Word word);
    void delete(Long id);
}
