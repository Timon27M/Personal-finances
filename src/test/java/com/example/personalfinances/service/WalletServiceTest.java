package com.example.personalfinances.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

import com.example.personalfinances.entity.BudgetWallet;
import com.example.personalfinances.entity.Category;
import com.example.personalfinances.entity.User;
import com.example.personalfinances.entity.Wallet;
import com.example.personalfinances.entity.enums.TransactionType;
import com.example.personalfinances.repository.WalletRepository;
import com.example.personalfinances.utils.SearchCurrentUserData;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WalletServiceTest {
  @Mock private SearchCurrentUserData searchCurrentUserData;

  @Mock private WalletRepository walletRepository;

  @Mock private CategoryService categoryService;

  @InjectMocks private WalletService walletService;

  private Wallet testWallet;
  private User testUser;
  private BudgetWallet testBudget;

  @BeforeEach
  void setUp() {
    testUser = new User("testUser", "hashedPassword");

    testWallet = new Wallet(testUser);
    testWallet.setUser(testUser);
    testWallet.setBalance(new BigDecimal("500.00"));

    testBudget = new BudgetWallet(testWallet);
    testBudget.setIncome(BigDecimal.ZERO);
    testBudget.setExpense(BigDecimal.ZERO);
    testBudget.setLimitAmount(new BigDecimal("1000.00"));

    testWallet.setBudget(testBudget);
  }

  @Test
  void increaseBalance_withValidPositiveAmount_shouldIncreaseBalanceAndIncome() {
    // Given
    BigDecimal amountToAdd = new BigDecimal("100.00");
    BigDecimal initialBalance = testWallet.getBalance();
    BigDecimal initialIncome = testBudget.getIncome();

    when(walletRepository.save(any(Wallet.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    // When
    walletService.increaseBalance(testWallet, amountToAdd);

    // Then
    BigDecimal expectedBalance = initialBalance.add(amountToAdd);
    assertThat(testWallet.getBalance()).isEqualByComparingTo(expectedBalance);

    BigDecimal expectedIncome = initialIncome.add(amountToAdd);
    assertThat(testBudget.getIncome()).isEqualByComparingTo(expectedIncome);

    verify(walletRepository, times(1)).save(testWallet);
  }

  @Test
  void decreaseBalance_withValidAmount_shouldDecreaseBalanceAndAddExpense() {
    // Given
    BigDecimal amountToWithdraw = new BigDecimal("200.00");
    BigDecimal initialBalance = testWallet.getBalance(); // 500.00
    BigDecimal initialExpense = testBudget.getExpense(); // 0.00

    when(walletRepository.save(any(Wallet.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    // When
    walletService.decreaseBalance(testWallet, amountToWithdraw);

    // Then
    BigDecimal expectedBalance = initialBalance.subtract(amountToWithdraw);
    assertThat(testWallet.getBalance()).isEqualByComparingTo(expectedBalance);

    BigDecimal expectedExpense = initialExpense.add(amountToWithdraw);
    assertThat(testBudget.getExpense()).isEqualByComparingTo(expectedExpense);

    verify(walletRepository, times(1)).save(testWallet);
  }

  @Test
  void addLimitAmount_withValidLimit_shouldUpdateLimit() {
    // Given
    BigDecimal currentExpense = new BigDecimal("200.00");
    BigDecimal newLimit = new BigDecimal("500.00");

    testBudget.setExpense(currentExpense);
    when(searchCurrentUserData.getWallet()).thenReturn(testWallet);

    // When
    String result = walletService.addLimitAmount(newLimit);

    // Then
    assertThat(result).isEqualTo("Лимит кошелька обновлен!");
    assertThat(testBudget.getLimitAmount()).isEqualByComparingTo(newLimit);
  }

  @Test
  void getWalletByLogin_withExistingLogin_shouldReturnWallet() {
    // Given
    String login = "testUser";
    when(walletRepository.existsByUserLogin(login)).thenReturn(true);
    when(walletRepository.findByUserLogin(login)).thenReturn(testWallet);

    // When
    Wallet result = walletService.getWalletByLogin(login);

    // Then
    assertThat(result).isEqualTo(testWallet);
    assertThat(result.getUser().getLogin()).isEqualTo(login);
    verify(walletRepository, times(1)).existsByUserLogin(login);
    verify(walletRepository, times(1)).findByUserLogin(login);
  }

  @Test
  void getCategoriesByType_shouldReturnCategoriesFromCategoryService() {
    // Given
    TransactionType type = TransactionType.EXPENSE;
    UUID walletId = testWallet.getWalletId();

    List<Category> expectedCategories =
        List.of(
            new Category(testWallet, "Еда", TransactionType.EXPENSE),
            new Category(testWallet, "Транспорт", TransactionType.EXPENSE));

    when(searchCurrentUserData.getWallet()).thenReturn(testWallet);
    when(categoryService.getCategoriesByWalletAndType(walletId, type))
        .thenReturn(expectedCategories);

    // When
    List<Category> result = walletService.getCategoriesByType(type);

    // Then
    assertThat(result).hasSize(2);
    assertThat(result).extracting(Category::getCategoryName).containsExactly("Еда", "Транспорт");
    verify(categoryService, times(1)).getCategoriesByWalletAndType(walletId, type);
  }

  @Test
  void getExpenseWallet_shouldReturnCurrentExpense() {
    // Given
    BigDecimal expectedExpense = new BigDecimal("350.00");
    testBudget.setExpense(expectedExpense);

    when(searchCurrentUserData.getWallet()).thenReturn(testWallet);

    // When
    BigDecimal result = walletService.getExpenseWallet();

    // Then
    assertThat(result).isEqualByComparingTo(expectedExpense);
    verify(searchCurrentUserData, times(1)).getWallet();
  }

  @Test
  void getIncomeWallet_shouldReturnCurrentIncome() {
    // Given
    BigDecimal expectedIncome = new BigDecimal("1200.00");
    testBudget.setIncome(expectedIncome);

    when(searchCurrentUserData.getWallet()).thenReturn(testWallet);

    // When
    BigDecimal result = walletService.getIncomeWallet();

    // Then
    assertThat(result).isEqualByComparingTo(expectedIncome);
    verify(searchCurrentUserData, times(1)).getWallet();
  }

  @Test
  void multipleOperations_shouldWorkCorrectly() {
    // Given
    when(walletRepository.save(any(Wallet.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    // When: увеличиваем баланс
    walletService.increaseBalance(testWallet, new BigDecimal("1000.00"));

    // Then: проверяем увеличение
    assertThat(testWallet.getBalance()).isEqualByComparingTo("1500.00"); // 500 + 1000
    assertThat(testBudget.getIncome()).isEqualByComparingTo("1000.00");

    // When: уменьшаем баланс
    walletService.decreaseBalance(testWallet, new BigDecimal("300.00"));

    // Then: проверяем уменьшение
    assertThat(testWallet.getBalance()).isEqualByComparingTo("1200.00"); // 1500 - 300
    assertThat(testBudget.getExpense()).isEqualByComparingTo("300.00");

    // Когда: устанавливаем лимит
    when(searchCurrentUserData.getWallet()).thenReturn(testWallet);
    String result = walletService.addLimitAmount(new BigDecimal("1500.00"));

    // Тогда: проверяем лимит
    assertThat(result).isEqualTo("Лимит кошелька обновлен!");
    assertThat(testBudget.getLimitAmount()).isEqualByComparingTo("1500.00");

    verify(walletRepository, times(2)).save(testWallet);
  }
}
