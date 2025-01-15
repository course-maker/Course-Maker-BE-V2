package net.coursemaker.backendv2.course.command.domain.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.coursemaker.backendv2.course.command.domain.aggregate.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

	@Query("SELECT c FROM Course c WHERE c.id = :id AND c.deletedAt IS NULL")
	Optional<Course> findByIdAndDeletedAtIsNull(@Param("id") Long id);

	@Query("SELECT c FROM Course c WHERE c.deletedAt IS NULL")
	Page<Course> findAllByDeletedAtIsNull(Pageable pageable);

	@Query("SELECT c FROM Course c WHERE c.deletedAt IS NULL ORDER BY c.views DESC")
	Page<Course> findAllByDeletedAtIsNullOrderByViewsDesc(Pageable pageable);

	@Query("SELECT c FROM Course c WHERE c.deletedAt IS NULL AND c.title LIKE %:title%")
	Page<Course> findByTitleContainingAndDeletedAtIsNull(@Param("title") String title, Pageable pageable);

	@Query("SELECT c FROM Course c WHERE c.deletedAt IS NULL AND c.author.nickname = :nickname")
	Page<Course> findByMemberNicknameAndDeletedAtIsNull(@Param("nickname") String nickname, Pageable pageable);

}
