package net.coursemaker.backendv2.tag.command.domain.repository;

import net.coursemaker.backendv2.tag.command.domain.aggregate.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

	// 이름으로 태그를 검색
	Optional<Tag> findByName(String name);

	// 특정 이름이 이미 존재하는지 확인
	boolean existsByName(String name);
}
