package engineer.thomas_werner.dashboard.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import engineer.thomas_werner.dashboard.domain.Sprint;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

    public List<Sprint> loadCurrentIteration() throws IOException {
        final StopWatch watch = new StopWatch();
        watch.start();

        final String url = urlPrefix + "?scope=current";
        final String json = loadResource(url);
        final List<Sprint> result = new ObjectMapper().readValue(json, new TypeReference<List<Sprint>>(){});

        watch.stop();
        log.info("Retrieved current Tracker iteration in " + watch.getTime(TimeUnit.MILLISECONDS) + " ms");

        return result;
    }

    public Sprint loadIteration(final Integer number) throws IOException {
        final StopWatch watch = new StopWatch();
        watch.start();

        final String url = urlPrefix + "/" + number;
        final String json = loadResource(url);
        final Sprint result = new ObjectMapper().readValue(json, Sprint.class);

        watch.stop();
        log.info("Retrieved Tracker iteration " + number + " in " + watch.getTime(TimeUnit.MILLISECONDS) + " ms");

        return result;
    }

    private String loadResource(String url) throws IOException {
        final Request request = new Request.Builder()
                .url(url)
                .header("X-TrackerToken", token)
                .build();
        return new OkHttpClient().newCall(request).execute().body().string();
    }

}
