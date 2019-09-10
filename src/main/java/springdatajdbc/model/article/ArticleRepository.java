package springdatajdbc.model.article;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ArticleRepository extends CrudRepository<Article, Integer> {

    @Query("SELECT a.* FROM article a INNER JOIN paragraph p ON p.article = a.id WHERE p.id = :paragraphId")
    Optional<Article> findArticleForParagraph(int paragraphId);
}
