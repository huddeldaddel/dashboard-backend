package engineer.thomas_werner.dashboard.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import engineer.thomas_werner.dashboard.domain.PullRequest;
import engineer.thomas_werner.dashboard.domain.Repository;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class GithubService {

    private String owner;
    private String[] repositories;
    private String token;

    public GithubService(@Value("${github.owner}") final String owner,
                         @Value("${github.repositories}") final String repositories,
                         @Value("${github.token}") final String token) {
        this.owner = owner;
        this.repositories = repositories.split(",");
        this.token = token;
    }

    public Optional<Repository> loadRepository(final String repository) {
        try {
            final StopWatch watch = new StopWatch();
            watch.start();

            final String json = loadResource("https://api.github.com/repos/" + owner + "/" + repository + "/pulls");
            final List<PullRequest> result = new ObjectMapper().readValue(json, new TypeReference<List<PullRequest>>(){});

            watch.stop();
            log.info("Retrieved pull requests for " + repository + " in " + watch.getTotalTimeMillis() + " ms");

            return Optional.of(new Repository(repository, result));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    private String loadResource(String url) throws IOException {
        final Request request = new Request.Builder()
                .url(url)
                .addHeader("Accept", "application/vnd.github.v3+json")
                .addHeader("Authorization", "token " + token)
                .build();
        final Response response = new OkHttpClient().newCall(request).execute();
        if(200 != response.code())
            throw new IOException("Failed to load resource");
        return response.body().string();
    }

}
