package backend.assignment.service;

import backend.assignment.exception.ResourceNotFoundException;
import backend.assignment.model.Word;
import backend.assignment.repository.WordRepository;
import backend.assignment.service.WordServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WordServiceImplTest {

    @Mock
    private WordRepository repo;

    @InjectMocks
    private WordServiceImpl service;

    private Word word;

    @BeforeEach
    void setup() {
        word = new Word(1L, "Test", "Def");
    }

    @Test
    void getAll_returnsList() {
        when(repo.findAll()).thenReturn(List.of(word));
        List<Word> result = service.getAll();
        assertThat(result).hasSize(1).contains(word);
    }

    @Test
    void getById_existing_returnsWord() {
        when(repo.findById(1L)).thenReturn(Optional.of(word));
        assertThat(service.getById(1L)).isEqualTo(word);
    }

    @Test
    void getById_notFound_throws() {
        when(repo.findById(2L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.getById(2L))
          .isInstanceOf(ResourceNotFoundException.class)
          .hasMessageContaining("2");
    }

    @Test
    void create_savesAndReturns() {
        when(repo.save(any())).thenReturn(word);
        Word saved = service.create(new Word(null, "X", "Y"));
        assertThat(saved).isEqualTo(word);
    }

    @Test
    void update_existing_updatesAndReturns() {
        Word updated = new Word(1L, "New", "NewDef");
        when(repo.findById(1L)).thenReturn(Optional.of(word));
        when(repo.save(any())).thenReturn(updated);

        Word res = service.update(1L, new Word(null, "New", "NewDef"));
        assertThat(res.getName()).isEqualTo("New");
        assertThat(res.getDefinition()).isEqualTo("NewDef");
    }

    @Test
    void update_notFound_throws() {
        when(repo.findById(3L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.update(3L, word))
          .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void delete_existing_deletes() {
        when(repo.findById(1L)).thenReturn(Optional.of(word));
        service.delete(1L);
        verify(repo).delete(word);
    }

    @Test
    void delete_notFound_throws() {
        when(repo.findById(4L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.delete(4L))
          .isInstanceOf(ResourceNotFoundException.class);
    }
}