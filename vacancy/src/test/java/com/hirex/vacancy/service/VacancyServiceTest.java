package com.hirex.vacancy.service;

import com.hirex.vacancy.VacancyMapper;
import com.hirex.vacancy.VacancyRepository;
import com.hirex.vacancy.domain.Vacancy;
import com.hirex.vacancy.domain.enums.JobFormat;
import com.hirex.vacancy.domain.enums.JobType;
import com.hirex.vacancy.domain.enums.VacancyStatus;
import com.hirex.vacancy.dto.VacancyDto;
import com.hirex.vacancy.dto.VacancyResumoDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VacancyServiceTest {

    @Mock
    private VacancyMapper vacancyMapper;

    @Mock
    private VacancyRepository vacancyRepository;

    @InjectMocks
    private VacancyService vacancyService;

    private Vacancy vacancyEntity;
    private VacancyDto vacancyDto;

    @BeforeEach
    void setUp() {
        vacancyEntity = new Vacancy();
        vacancyEntity.setId(1L);
        vacancyEntity.setTitle("Java Developer");
        vacancyEntity.setDescription("Backend position");
        vacancyEntity.setLocation("Remote");
        vacancyEntity.setJobFormat(JobFormat.REMOTE);
        vacancyEntity.setJobType(JobType.FULL_TIME);
        vacancyEntity.setStatus(VacancyStatus.OPEN);

        vacancyDto = new VacancyDto();
        vacancyDto.setId(1L);
        vacancyDto.setTitle("Java Developer");
        vacancyDto.setDescription("Backend position");
        vacancyDto.setLocation("Remote");
        vacancyDto.setJobFormat(JobFormat.REMOTE);
        vacancyDto.setJobType(JobType.FULL_TIME);
        vacancyDto.setStatus(VacancyStatus.OPEN);
    }

    @Test
    @DisplayName("findAll returns mapped resumo DTOs with paging by title")
    void findAll_shouldReturnResumoDtos() {
        Page<Vacancy> page = new PageImpl<>(List.of(vacancyEntity));
        when(vacancyRepository.findAll(any(PageRequest.class))).thenReturn(page);
        VacancyResumoDto resumo = new VacancyResumoDto();
        resumo.setId(1L);
        resumo.setTitle("Java Developer");
        resumo.setLocation("Remote");
        resumo.setJobFormat(JobFormat.REMOTE);
        resumo.setJobType(JobType.FULL_TIME);
        when(vacancyMapper.toVacanciesResumoDto(page)).thenReturn(List.of(resumo));

        List<VacancyResumoDto> result = vacancyService.findAll(0, 10, "title");

        assertEquals(1, result.size());
        assertEquals("Java Developer", result.get(0).getTitle());

        ArgumentCaptor<PageRequest> captor = ArgumentCaptor.forClass(PageRequest.class);
        verify(vacancyRepository).findAll(captor.capture());
        PageRequest pr = captor.getValue();
        assertEquals(0, pr.getPageNumber());
        assertEquals(10, pr.getPageSize());
    }

    @Test
    @DisplayName("get maps entity to DTO when found")
    void get_shouldMapEntityToDto() {
        when(vacancyRepository.findById(1L)).thenReturn(Optional.of(vacancyEntity));
        when(vacancyMapper.toVacancyDto(vacancyEntity)).thenReturn(vacancyDto);

        VacancyDto result = vacancyService.get(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    @DisplayName("save persists a new vacancy")
    void save_shouldPersist() {
        when(vacancyMapper.toVacancy(vacancyDto)).thenReturn(vacancyEntity);
        when(vacancyRepository.save(vacancyEntity)).thenReturn(vacancyEntity);
        when(vacancyMapper.toVacancyDto(vacancyEntity)).thenReturn(vacancyDto);

        VacancyDto saved = vacancyService.save(vacancyDto);

        assertNotNull(saved);
        verify(vacancyRepository).save(vacancyEntity);
    }

    @Test
    @DisplayName("update maps fields and saves when valid")
    void update_shouldMapAndSave() {
        Vacancy toUpdate = new Vacancy();
        toUpdate.setId(1L);
        toUpdate.setTitle("Old Title");
        when(vacancyRepository.findById(1L)).thenReturn(Optional.of(toUpdate));

        VacancyDto patch = new VacancyDto();
        patch.setTitle("New Title");

        doAnswer(invocation -> {
            VacancyDto src = invocation.getArgument(0);
            Vacancy target = invocation.getArgument(1);
            target.setTitle(src.getTitle());
            return null;
        }).when(vacancyMapper).updateVacancyFromDto(any(VacancyDto.class), any(Vacancy.class));

        when(vacancyRepository.save(any(Vacancy.class))).thenAnswer(inv -> inv.getArgument(0));
        when(vacancyMapper.toVacancyDto(any(Vacancy.class))).thenAnswer(inv -> {
            Vacancy v = inv.getArgument(0);
            VacancyDto dto = new VacancyDto();
            dto.setId(v.getId());
            dto.setTitle(v.getTitle());
            return dto;
        });

        VacancyDto updated = vacancyService.update(1L, patch);

        assertEquals("New Title", updated.getTitle());
        verify(vacancyRepository).save(any(Vacancy.class));
    }

    @Test
    @DisplayName("delete delegates to repository")
    void delete_shouldCallRepository() {
        when(vacancyRepository.findById(1L)).thenReturn(Optional.of(vacancyEntity));
        vacancyService.delete(1L);
        verify(vacancyRepository).deleteById(1L);
    }
}
