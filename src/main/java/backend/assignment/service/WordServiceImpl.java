package backend.assignment.service;

import backend.assignment.exception.ResourceNotFoundException;
import backend.assignment.model.Word;
import backend.assignment.repository.WordRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class WordServiceImpl implements WordService {
    private final WordRepository repo;

    public WordServiceImpl(WordRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<Word> getAll() {
        return repo.findAll();
    }

    @Override
    public Word getById(Long id) {
        return repo.findById(id)
                   .orElseThrow(() -> new ResourceNotFoundException("Word not found with id " + id));
    }

    @Override
    public Word create(Word Word) {
        return repo.save(Word);
    }

    @Override
    public Word update(Long id, Word details) {
        Word existing = getById(id);
        existing.setName(details.getName());
        existing.setDefinition(details.getDefinition());
        return repo.save(existing);
    }

    @Override
    public void delete(Long id) {
        Word existing = getById(id);
        repo.delete(existing);
    }
}
