package lotto;

// Application.java

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

            List<Integer> winningNumbers = getValidWinningNumbers();
            int bonusNumber = getValidBonusNumber();

            Map<Rank, Integer> results = lottoMachine.calculateResults(purchasedLottos, winningNumbers, bonusNumber);
            displayResults(results);

            double yield = lottoMachine.calculateYield(purchaseAmount, results);
            System.out.printf("총 수익률은 %.1f%%입니다.%n", yield * 100);

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

    private static List<Integer> getValidWinningNumbers() {
        while (true) {
            try {
                System.out.println("당첨 번호를 입력해 주세요.");
                List<Integer> winningNumbers = parseWinningNumbers(Console.readLine());
                validateWinningNumbers(winningNumbers);
                return winningNumbers;
            } catch (IllegalArgumentException e) {
                System.out.println("[ERROR] " + e.getMessage());
            }
        }
    }

    private static List<Integer> parseWinningNumbers(String input) {
        return Arrays.stream(input.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    private static void validateWinningNumbers(List<Integer> winningNumbers) {
        if (winningNumbers.size() != 6) {
            throw new IllegalArgumentException("당첨 번호는 6개여야 합니다.");
        }
    }

    private static int getValidBonusNumber() {
        while (true) {
            try {
                System.out.println("보너스 번호를 입력해 주세요.");
                return validateBonusNumber(Integer.parseInt(Console.readLine()));
            } catch (IllegalArgumentException e) {
                System.out.println("[ERROR] " + e.getMessage());
            }
        }
    }

    private static int validateBonusNumber(int bonusNumber) {
        if (bonusNumber < 1 || bonusNumber > 45) {
            throw new IllegalArgumentException("보너스 번호는 1부터 45 사이의 숫자여야 합니다.");
        }
        return bonusNumber;
    }

    private static void displayResults(Map<Rank, Integer> results) {
        System.out.println("당첨 통계\n---");
        displayMatchResult("3개 일치 (5,000원)", results.get(Rank.FIFTH));
        displayMatchResult("4개 일치 (50,000원)", results.get(Rank.FOURTH));
        displayMatchResult("5개 일치 (1,500,000원)", results.get(Rank.THIRD));
        displayMatchResult("5개 일치, 보너스 볼 일치 (30,000,000원)", results.get(Rank.SECOND));
        displayMatchResult("6개 일치 (2,000,000,000원)", results.get(Rank.FIRST));
    }

    private static void displayMatchResult(String matchDescription, int count) {
        System.out.printf("%s - %d개%n", matchDescription, count);
    }
}
