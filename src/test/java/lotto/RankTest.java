package lotto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RankTest {

  @Test
  @DisplayName("6개 일치 -> 1등")
  void rankFirstTest() {
    assertThat(Rank.of(6, false)).isEqualTo(Rank.FIRST);
  }

  @Test
  @DisplayName("5개 일치 + 보너스 일치 -> 2등")
  void rankSecondWithBonusTest() {
    assertThat(Rank.of(5, true)).isEqualTo(Rank.SECOND);
  }

  @Test
  @DisplayName("5개 일치 + 보너스 불일치 -> 3등")
  void rankThirdWithoutBonusTest() {
    assertThat(Rank.of(5, false)).isEqualTo(Rank.THIRD);
  }

  @Test
  @DisplayName("4개 일치 -> 4등")
  void rankFourthTest() {
    assertThat(Rank.of(4, false)).isEqualTo(Rank.FOURTH);
  }

  @Test
  @DisplayName("3개 일치 -> 5등")
  void rankFifthTest() {
    assertThat(Rank.of(3, false)).isEqualTo(Rank.FIFTH);
  }

  @Test
  @DisplayName("2개 이하 일치 -> 당첨 없음")
  void rankNoneTest() {
    assertThat(Rank.of(2, false)).isEqualTo(Rank.NONE);
    assertThat(Rank.of(1, false)).isEqualTo(Rank.NONE);
    assertThat(Rank.of(0, false)).isEqualTo(Rank.NONE);
  }
}
