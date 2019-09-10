package springdatajdbc.model.blog;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface BlogRepository extends CrudRepository<Blog, Integer> {

    @Query("SELECT b.* FROM blog b INNER JOIN blog_post as bp on b.id = bp.blog where bp.id = :blogPostId")
    public Optional<Blog> findBlogForBlogPostId(int blogPostId);
}
