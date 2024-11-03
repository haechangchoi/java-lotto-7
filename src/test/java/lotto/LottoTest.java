package lotto;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;

import java.util.List;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LottoTest {
    @Test
    void 로또_번호의_개수가_6개가_넘어가면_예외가_발생한다() {
        assertThatThrownBy(() -> new Lotto(List.of(1, 2, 3, 4, 5, 6, 7)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("로또 번호에 중복된 숫자가 있으면 예외가 발생한다.")
    @Test
    void 로또_번호에_중복된_숫자가_있으면_예외가_발생한다() {
        assertThatThrownBy(() -> new Lotto(List.of(1, 2, 3, 4, 5, 5)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("올바른 로또 번호 리스트가 주어졌을 때 예외가 발생하지 않는다")
    void 올바른_로또_번호_리스트가_주어졌을_때_예외가_발생하지_않는다() {
        assertThatNoException().isThrownBy(() -> new Lotto(List.of(1, 2, 3, 4, 5, 6)));
    }
    // TODO: 추가 기능 구현에 따른 테스트 코드 작성
    @ParameterizedTest
    @ValueSource(ints = {0, 46})
    @DisplayName("로또 번호가 1~45 범위를 벗어나면 예외가 발생한다.")
    void 로또_번호가_범위를_벗어나면_예외가_발생한다(int invalidNumber) {
        assertThatThrownBy(() -> new Lotto(List.of(1, 2, 3, 4, 5, invalidNumber)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR]");
    }

    @TestFactory
    @DisplayName("로또 번호의 일치 수 계산 테스트 - 다양한 당첨 번호 조합")
    Stream<DynamicTest> 로또_번호의_일치_수_계산_테스트() {
        return Stream.of(
                DynamicTest.dynamicTest("3개 일치", () -> {
                    Lotto lotto = new Lotto(List.of(1, 2, 3, 7, 8, 9));
                    int matchCount = lotto.getMatchCount(List.of(1, 2, 3, 4, 5, 6));
                    assertThat(matchCount).isEqualTo(3);
                }),
                DynamicTest.dynamicTest("5개 일치", () -> {
                    Lotto lotto = new Lotto(List.of(1, 2, 3, 4, 5, 10));
                    int matchCount = lotto.getMatchCount(List.of(1, 2, 3, 4, 5, 6));
                    assertThat(matchCount).isEqualTo(5);
                }),
                DynamicTest.dynamicTest("6개 일치", () -> {
                    Lotto lotto = new Lotto(List.of(1, 2, 3, 4, 5, 6));
                    int matchCount = lotto.getMatchCount(List.of(1, 2, 3, 4, 5, 6));
                    assertThat(matchCount).isEqualTo(6);
                })
        );
    }
}
