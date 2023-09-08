package study.PharmacyProject.direction.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.PharmacyProject.direction.entity.Direction;

public interface DirectionRepository extends JpaRepository<Direction,Long> {
    //거리결과를 꼭 저장해놔야하나? 가까운 3개?
    //해당 요청주소에 모든 약국의 거리를 다 저장한다?..
}
