package engineer.thomas_werner.dashboard.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import engineer.thomas_werner.dashboard.domain.Sprint;
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
public class TrackerService {

    private final String token;
    private final String urlPrefix;

    public TrackerService(@Value("${tracker.project}") final String project,
                          @Value("${tracker.token}") final String token) {
        this.token = token;
        this.urlPrefix = "https://www.pivotaltracker.com/services/v5/projects/" + project + "/iterations";
    }

    public Optional<Sprint> loadCurrentSprint() {
        try {
            final StopWatch watch = new StopWatch();
            watch.start();
            final String json = loadResource(urlPrefix + "?scope=current");
            final List<Sprint> result = new ObjectMapper().readValue(json, new TypeReference<List<Sprint>>(){});
            watch.stop();
            log.info("Retrieved current Tracker iteration in " + watch.getTotalTimeMillis() + " ms");

            return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
        } catch(Exception ex) {
            log.error("Failed to load current sprint from Pivotal Tracker", ex);
            return Optional.empty();
        }
    }

    public Optional<Sprint> loadSprint(final Integer number) {
        try {
            final StopWatch watch = new StopWatch();
            watch.start();

            final String url = urlPrefix + "/" + number;
            final String json = loadResource(url);
            final Sprint result = new ObjectMapper().readValue(json, Sprint.class);

            watch.stop();
            log.info("Retrieved Tracker iteration " + number + " in " + watch.getTotalTimeMillis() + " ms");

            return Optional.of(result);
        } catch(Exception ex) {
            log.error("Failed to load sprint #" + number + " from Pivotal Tracker", ex);
            return Optional.empty();
        }
    }

    private String loadResource(String url) throws IOException {
        final Request request = new Request.Builder()
                .url(url)
                .header("X-TrackerToken", token)
                .build();
        final Response response = new OkHttpClient().newCall(request).execute();
        if(200 != response.code())
            throw new IOException("Failed to load resource");
        if (response.body() != null) {
            return response.body().string();
        } else {
            throw new IOException("No resource body found");
        }
    }

}
