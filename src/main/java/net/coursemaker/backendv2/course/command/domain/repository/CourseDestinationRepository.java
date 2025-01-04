package net.coursemaker.backendv2.course.command.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.coursemaker.backendv2.course.command.domain.aggregate.CourseDestination;

@Repository
public interface CourseDestinationRepository extends JpaRepository<CourseDestination, Long> {
}
