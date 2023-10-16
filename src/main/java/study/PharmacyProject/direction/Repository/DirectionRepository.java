package study.PharmacyProject.direction.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.PharmacyProject.direction.entity.Direction;

public interface DirectionRepository extends JpaRepository<Direction,Long> {
    //추천결과를 저장하는 기능이 꼭 필요할까?
}
