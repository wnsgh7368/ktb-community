package ktb.community.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class LikeId implements Serializable {
    private Long postId;
    private Long userId;
}
/**
 * 복합키 클래스를 생성하기 위한 4가지 조건
 * 1. Serializable을 상속해야 한다.
 * 2. 빈 생성자가 존재하야 한다.
 * 3. 해당 Id 클래스를 사용하는 엔티티 클래스에 @IdClass를 명시해야 한다
 * 4. equals() 와 hashCode() 메서드가 존재해야 한다.
 */
