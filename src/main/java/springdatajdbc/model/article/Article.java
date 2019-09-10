package springdatajdbc.model.article;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.experimental.Wither;
import org.springframework.data.annotation.Id;

import java.util.List;

@RequiredArgsConstructor
@Value
@Wither
@Builder
public class Article {
    @Id
    private final int id;
    private final String title;
    private final List<Paragraph> paragraphs;
}
