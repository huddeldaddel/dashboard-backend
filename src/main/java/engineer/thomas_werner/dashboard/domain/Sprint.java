package engineer.thomas_werner.dashboard.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class Sprint {

    @Id
    public String id;

    public Integer number;

    public List<Story> stories;

}
