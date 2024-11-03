package lotto;

import camp.nextstep.edu.missionutils.Randoms;
import java.util.*;
import java.util.stream.Collectors;

public class LottoMachine {
  private static final int LOTTO_PRICE = 1000;
  private static final int LOTTO_NUMBER_COUNT = 6;
  private static final int MIN_NUMBER = 1;
  private static final int MAX_NUMBER = 45;

  public List<Lotto> purchaseLottos(int purchaseAmount) {
    validatePurchaseAmount(purchaseAmount);

    int numberOfLottos = purchaseAmount / LOTTO_PRICE;
    return generateLottos(numberOfLottos);
  }

  private void validatePurchaseAmount(int amount) {
    if (amount % LOTTO_PRICE != 0) {
      throw new IllegalArgumentException("[ERROR] 구입 금액은 1,000원 단위로 입력해야 합니다.");
    }
  }

  private List<Lotto> generateLottos(int count) {
    List<Lotto> lottos = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      lottos.add(generateLotto());
    }
    return lottos;
  }

  private Lotto generateLotto() {
    List<Integer> numbers = Randoms.pickUniqueNumbersInRange(MIN_NUMBER, MAX_NUMBER, LOTTO_NUMBER_COUNT)
            .stream()
            .sorted()
            .collect(Collectors.toList());
    return new Lotto(numbers);
  }

  public Map<Rank, Integer> calculateResults(List<Lotto> purchasedLottos, List<Integer> winningNumbers, int bonusNumber) {
    Map<Rank, Integer> results = initResults();

    for (Lotto lotto : purchasedLottos) {
      Rank rank = determineRank(lotto, winningNumbers, bonusNumber);
      results.put(rank, results.get(rank) + 1);
    }

    return results;
  }

  private Map<Rank, Integer> initResults() {
    Map<Rank, Integer> results = new EnumMap<>(Rank.class);
    for (Rank rank : Rank.values()) {
      results.put(rank, 0);
    }
    return results;
  }

  private Rank determineRank(Lotto lotto, List<Integer> winningNumbers, int bonusNumber) {
    int matchCount = lotto.getMatchCount(winningNumbers);
    boolean matchBonus = lotto.containsBonusNumber(bonusNumber);
    return Rank.of(matchCount, matchBonus);
  }

  public double calculateYield(int purchaseAmount, Map<Rank, Integer> results) {
    int totalPrize = results.entrySet().stream()
            .mapToInt(entry -> entry.getKey().getPrize() * entry.getValue())
            .sum();
    return (double) totalPrize / purchaseAmount;
  }
}
