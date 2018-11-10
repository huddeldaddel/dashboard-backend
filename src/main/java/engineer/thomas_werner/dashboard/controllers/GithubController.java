package engineer.thomas_werner.dashboard.controllers;

import engineer.thomas_werner.dashboard.domain.PullRequest;
import engineer.thomas_werner.dashboard.domain.Repository;
import engineer.thomas_werner.dashboard.services.GithubService;
import engineer.thomas_werner.dashboard.utilities.Cache;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.time.temporal.ChronoUnit.MINUTES;

@RestController
public class GithubController {

    private Cache<Repository> repositoryCache;
    private GithubService githubService;

    public GithubController(final GithubService githubService) {
        this.githubService = githubService;
        this.repositoryCache = new Cache<>(githubService::loadRepository, 5, MINUTES);
        this.repositoryCache.setName("Github repository cache");
    }

    @GetMapping("/github/repositories")
    public List<String> getCurrentSprint() {
        return githubService.getRepositoryNames();
    }

    @GetMapping("/github/repositories/{name}/pulls")
    public List<PullRequest> getPullRequests(@PathVariable final String name) {
        final Repository repository = repositoryCache.get(name).orElseThrow(NotFoundException::new);
        return repository.getPullRequests();
    }

}
