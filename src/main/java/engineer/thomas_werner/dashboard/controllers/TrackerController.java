package engineer.thomas_werner.dashboard.controllers;

import engineer.thomas_werner.dashboard.domain.Sprint;
import engineer.thomas_werner.dashboard.repositories.SprintRepository;
import engineer.thomas_werner.dashboard.services.TrackerService;
import engineer.thomas_werner.dashboard.utilities.Cache;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.MINUTES;

@RestController
public class TrackerController {

    public static final String CURRENT_SPRINT_MAPPING = "/tracker/sprints/current";
    public static final String SPECIFIC_SPRINT_MAPPING = "/tracker/sprints/{id}";

    private final Cache<Sprint> currentSprintCache;
    private final SprintRepository sprintRepository;
    private final TrackerService trackerService;

    public TrackerController(final SprintRepository sprintRepository,
                             final TrackerService trackerService) {
        this.sprintRepository = sprintRepository;
        this.trackerService = trackerService;
        this.currentSprintCache = new Cache<>((key) -> trackerService.loadCurrentSprint(),5, MINUTES);
        this.currentSprintCache.setName("Tracker current sprint cache");
    }

    @GetMapping(CURRENT_SPRINT_MAPPING)
    public Sprint getCurrentSprint() {
        return currentSprintCache.get("current").orElseThrow(NotFoundException::new);
    }

    @GetMapping(SPECIFIC_SPRINT_MAPPING)
    public Sprint getSprint(@PathVariable final Integer id) {
        final List<Sprint> cached = sprintRepository.findByNumber(id);
        if(!cached.isEmpty())
            return cached.get(0);

        final Optional<Sprint> sprint = trackerService.loadSprint(id);
        sprint.ifPresent(sprintRepository::insert);
        return sprint.orElseThrow(NotFoundException::new);
    }

}
