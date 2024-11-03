package lotto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.DynamicTest;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LottoMachineTest {

  private final LottoMachine lottoMachine = new LottoMachine();

  @Test
  @DisplayName("올바른 구입 금액으로 로또를 발행하면 예상 개수의 로또가 생성된다.")
  void purchaseLottos_createsCorrectNumberOfLottos() {
    int purchaseAmount = 5000;
    List<Lotto> lottos = lottoMachine.purchaseLottos(purchaseAmount);
    assertThat(lottos).hasSize(5); // 5000원으로 5개의 로또가 생성
  }

  @Test
  @DisplayName("구입 금액이 1,000원 단위가 아니면 예외가 발생한다.")
  void purchaseLottos_throwsExceptionForInvalidAmount() {
    int invalidAmount = 550; // 1,000원 단위가 아닌 금액
    assertThatThrownBy(() -> lottoMachine.purchaseLottos(invalidAmount))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("구입 금액은 1,000원 단위로 입력해야 합니다.");
  }

  @TestFactory
  @DisplayName("수익률 계산 테스트 - 다양한 경우에 따른 수익률 계산 결과를 검증")
  Stream<DynamicTest> calculateYield_dynamicTests() {
    Map<Rank, Integer> resultsCase1 = Map.of(Rank.FIRST, 1); // 1등 상금 2,000,000,000원
    Map<Rank, Integer> resultsCase2 = Map.of(Rank.SECOND, 1, Rank.FOURTH, 2); // 2등 상금 30,000,000원, 4등 2개 50,000원
    Map<Rank, Integer> resultsCase3 = Map.of(Rank.FIFTH, 5); // 5등 상금 5,000원 * 5개 = 25,000원

    return Stream.of(
            DynamicTest.dynamicTest("1등 1개 - 예상 수익률 400000.0", () -> {
              double yield = lottoMachine.calculateYield(5000, resultsCase1);
              assertThat(yield).isEqualTo(400000.0);
            }),
            DynamicTest.dynamicTest("2등 1개, 4등 2개 - 예상 수익률 301.0", () -> {
              double yield = lottoMachine.calculateYield(100000, resultsCase2);
              assertThat(yield).isEqualTo(301.0);
            }),
            DynamicTest.dynamicTest("5등 5개 - 예상 수익률 0.25", () -> {
              double yield = lottoMachine.calculateYield(100000, resultsCase3);
              assertThat(yield).isEqualTo(0.25);
            })
    );
  }

  @Test
  @DisplayName("로또 번호 결과 계산 테스트 - 당첨 번호와 보너스 번호로 당첨 결과를 정확히 계산한다.")
  void calculateResults_correctlyCalculatesResults() {
    List<Lotto> purchasedLottos = List.of(
            new Lotto(List.of(1, 2, 3, 4, 5, 6)), // 1등
            new Lotto(List.of(1, 2, 3, 4, 5, 7)), // 2등
            new Lotto(List.of(1, 2, 3, 4, 5, 8)), // 3등
            new Lotto(List.of(1, 2, 3, 4, 9, 10)), // 4등
            new Lotto(List.of(1, 2, 3, 11, 12, 13)) // 5등
    );

    List<Integer> winningNumbers = List.of(1, 2, 3, 4, 5, 6);
    int bonusNumber = 7;

    Map<Rank, Integer> results = lottoMachine.calculateResults(purchasedLottos, winningNumbers, bonusNumber);

    assertThat(results.get(Rank.FIRST)).isEqualTo(1);
    assertThat(results.get(Rank.SECOND)).isEqualTo(1);
    assertThat(results.get(Rank.THIRD)).isEqualTo(1);
    assertThat(results.get(Rank.FOURTH)).isEqualTo(1);
    assertThat(results.get(Rank.FIFTH)).isEqualTo(1);
  }
}
