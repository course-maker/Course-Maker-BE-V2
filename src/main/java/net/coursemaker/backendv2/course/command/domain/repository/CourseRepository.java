package net.coursemaker.backendv2.course.command.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import net.coursemaker.backendv2.course.command.domain.aggregate.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
}
