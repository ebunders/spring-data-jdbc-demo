package springdatajdbc.model.blog;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface BlogRepository extends CrudRepository<Blog, Integer> {

    @SuppressWarnings("UnnecessaryInterfaceModifier")
    @Query("SELECT b.* FROM blog b INNER JOIN blogpost as bp on b.id = bp.blog where bp.id = :blogPostId")
    public Optional<Blog> findBlogForBlogPostId(int blogPostId);

    @Query("select b.* from blog b where b.owner = :userId")
    List<Blog> findBlogsForUser(int userId);

    @Query("select b.* from blog b inner join blogpost bp on b.id = bp.blog inner join blogpost_user bpu on bp.id = bpu.blogpost where bpu.user = :userId")
    List<Blog> findBlogsWhereUserPosted(int userId);

    @Query("select bp.* from blogpost bp where bp.blog = :blogId and :userId in(select bpu.user from blogpost_user bpu where bpu.blogpost = bp.id)")
    List<Blogpost> findBlogpostsForUserInBlog(int userId, int blogId);
}
