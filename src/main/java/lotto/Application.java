package lotto;

import camp.nextstep.edu.missionutils.Console;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Application {
    public static void main(String[] args) {
        LottoMachine lottoMachine = new LottoMachine();

        try {
            int purchaseAmount = getPurchaseAmount();
            List<Lotto> purchasedLottos = lottoMachine.purchaseLottos(purchaseAmount);
            displayPurchasedLottos(purchasedLottos);

            List<Integer> winningNumbers = getWinningNumbers();
            int bonusNumber = getBonusNumber();

            Map<Rank, Integer> results = lottoMachine.calculateResults(purchasedLottos, winningNumbers, bonusNumber);
            displayResults(results);

            double yield = lottoMachine.calculateYield(purchaseAmount, results);
            System.out.printf("총 수익률은 %.1f%%입니다.%n", yield * 100); // 수익률을 퍼센트로 변환하여 출력

        } catch (IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    private static int getPurchaseAmount() {
        System.out.println("구입 금액을 입력해 주세요.");
        return Integer.parseInt(Console.readLine());
    }

    private static void displayPurchasedLottos(List<Lotto> purchasedLottos) {
        System.out.println(purchasedLottos.size() + "개를 구매했습니다.");
        purchasedLottos.forEach(lotto -> System.out.println(lotto.getNumbers()));
    }

    private static List<Integer> getWinningNumbers() {
        System.out.println("당첨 번호를 입력해 주세요.");
        return Arrays.stream(Console.readLine().split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    private static int getBonusNumber() {
        System.out.println("보너스 번호를 입력해 주세요.");
        return Integer.parseInt(Console.readLine());
    }

    private static void displayResults(Map<Rank, Integer> results) {
        System.out.println("당첨 통계\n---");
        System.out.printf("3개 일치 (5,000원) - %d개%n", results.get(Rank.FIFTH));
        System.out.printf("4개 일치 (50,000원) - %d개%n", results.get(Rank.FOURTH));
        System.out.printf("5개 일치 (1,500,000원) - %d개%n", results.get(Rank.THIRD));
        System.out.printf("5개 일치, 보너스 볼 일치 (30,000,000원) - %d개%n", results.get(Rank.SECOND));
        System.out.printf("6개 일치 (2,000,000,000원) - %d개%n", results.get(Rank.FIRST));
    }
}
