package study.PharmacyProject

import org.springframework.boot.test.context.SpringBootTest
import org.testcontainers.containers.GenericContainer
import spock.lang.Specification

/*테스트 컨테이너를 이용한 통합테스트를 위한 싱글톤테스트컨테이너 만들기*/
@SpringBootTest
abstract class AbstractIntegrationContainerBaseTest extends Specification{

    //redis추가를 위한 GenericContainer

    static final GenericContainer MY_REDIS_CONTAINER

    static {
        MY_REDIS_CONTAINER = new GenericContainer<>("redis:6")
                .withExposedPorts(6379)

        MY_REDIS_CONTAINER.start()

        System.setProperty("spring.redis.host", MY_REDIS_CONTAINER.getHost())
        System.setProperty("spring.redis.port", MY_REDIS_CONTAINER.getMappedPort(6379).toString())
    }
    
}
