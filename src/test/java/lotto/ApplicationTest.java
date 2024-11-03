package lotto;

import camp.nextstep.edu.missionutils.test.NsTest;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;

import java.util.List;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static camp.nextstep.edu.missionutils.test.Assertions.assertRandomUniqueNumbersInRangeTest;
import static camp.nextstep.edu.missionutils.test.Assertions.assertSimpleTest;
import static org.assertj.core.api.Assertions.assertThat;

class ApplicationTest extends NsTest {
    private static final String ERROR_MESSAGE = "[ERROR]";

    @Test
    void 기능_테스트() {
        assertRandomUniqueNumbersInRangeTest(
                () -> {
                    run("8000", "1,2,3,4,5,6", "7");
                    assertThat(output()).contains(
                            "8개를 구매했습니다.",
                            "[8, 21, 23, 41, 42, 43]",
                            "[3, 5, 11, 16, 32, 38]",
                            "[7, 11, 16, 35, 36, 44]",
                            "[1, 8, 11, 31, 41, 42]",
                            "[13, 14, 16, 38, 42, 45]",
                            "[7, 11, 30, 40, 42, 43]",
                            "[2, 13, 22, 32, 38, 45]",
                            "[1, 3, 5, 14, 22, 45]",
                            "3개 일치 (5,000원) - 1개",
                            "4개 일치 (50,000원) - 0개",
                            "5개 일치 (1,500,000원) - 0개",
                            "5개 일치, 보너스 볼 일치 (30,000,000원) - 0개",
                            "6개 일치 (2,000,000,000원) - 0개",
                            "총 수익률은 62.5%입니다."
                    );
                },
                List.of(8, 21, 23, 41, 42, 43),
                List.of(3, 5, 11, 16, 32, 38),
                List.of(7, 11, 16, 35, 36, 44),
                List.of(1, 8, 11, 31, 41, 42),
                List.of(13, 14, 16, 38, 42, 45),
                List.of(7, 11, 30, 40, 42, 43),
                List.of(2, 13, 22, 32, 38, 45),
                List.of(1, 3, 5, 14, 22, 45)
        );
    }

    @Test
    void 예외_테스트() {
        assertSimpleTest(() -> {
            runException("1000j");
            assertThat(output()).contains(ERROR_MESSAGE);
        });
    }

    @Override
    public void runMain() {
        Application.main(new String[]{});
    }

    @ParameterizedTest
    @DisplayName("구입 금액 예외 테스트 - 잘못된 구입 금액 입력 시 예외 발생")
    @ValueSource(strings = {"500", "-1000", "10000j"})
    void purchaseAmountInvalid(String input) {
        assertSimpleTest(() -> {
            runException(input, "8000"); // 잘못된 입력 후, 올바른 입력 제공
            assertThat(output()).contains(ERROR_MESSAGE);
        });
    }

    @TestFactory
    @DisplayName("수익률 계산 테스트 - 다양한 당첨 조합에 따른 수익률 검증")
    Stream<DynamicTest> calculateYieldTests() {
        return Stream.of(
                DynamicTest.dynamicTest("1등 1개 - 예상 수익률 400000.0%", () -> {
                    var results = Map.of(Rank.FIRST, 1);
                    assertThat(new LottoMachine().calculateYield(5000, results)).isEqualTo(400000.0);
                }),
                DynamicTest.dynamicTest("2등 1개, 4등 2개 - 예상 수익률 301.0%", () -> {
                    var results = Map.of(Rank.SECOND, 1, Rank.FOURTH, 2);
                    assertThat(new LottoMachine().calculateYield(100000, results)).isEqualTo(301.0);
                }),
                DynamicTest.dynamicTest("5등 5개 - 예상 수익률 0.25%", () -> {
                    var results = Map.of(Rank.FIFTH, 5);
                    assertThat(new LottoMachine().calculateYield(100000, results)).isEqualTo(0.25);
                })
        );
    }

    @ParameterizedTest
    @DisplayName("보너스 번호 범위 예외 테스트")
    @ValueSource(strings = {"0", "46"})
    void bonusNumberOutOfRangeTest(String bonusNumber) {
        assertSimpleTest(() -> {
            run("8000", "1,2,3,4,5,6", bonusNumber, "7"); // 잘못된 보너스 번호 후 올바른 입력
            assertThat(output()).contains(ERROR_MESSAGE);  // 에러 메시지 포함 확인
        });
    }

    @ParameterizedTest
    @DisplayName("당첨 번호 입력 개수 부족 예외 테스트")
    @ValueSource(strings = {"1,2,3,4,5", "1,2,3,4,5,6,7"})
    void winningNumbersInvalidCountTest(String winningNumbers) {
        assertSimpleTest(() -> {
            run("8000", winningNumbers, "1,2,3,4,5,6", "7"); // 잘못된 당첨 번호 개수 후 올바른 당첨 번호와 보너스 번호 입력
            assertThat(output()).contains(ERROR_MESSAGE); // 에러 메시지 포함 확인
        });
    }
}
