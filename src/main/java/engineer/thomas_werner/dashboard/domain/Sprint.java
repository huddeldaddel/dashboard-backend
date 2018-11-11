package engineer.thomas_werner.dashboard.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Sprint {

    @Id
    private String id;
    private Integer number;
    private String start;
    private List<Story> stories;

}
