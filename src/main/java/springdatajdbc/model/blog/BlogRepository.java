package springdatajdbc.model.blog;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface BlogRepository extends CrudRepository<Blog, Integer> {

    @SuppressWarnings("UnnecessaryInterfaceModifier")
    @Query("SELECT b.* FROM blog b INNER JOIN blogpost as bp on b.id = bp.blog where bp.id = :blogPostId")
    public Optional<Blog> findBlogForBlogPostId(int blogPostId);
}
