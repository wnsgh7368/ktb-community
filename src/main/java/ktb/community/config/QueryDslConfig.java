package ktb.community.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueryDslConfig {

    // EntityManager를 영속성 컨텍스트로 지정
    @PersistenceContext
    private EntityManager entityManager;

    // DI를 위해 스프링 빈으로 등록 (entityManager 주입)
    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }
}
