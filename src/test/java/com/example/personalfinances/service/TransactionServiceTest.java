package com.example.personalfinances.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.personalfinances.entity.Category;
import com.example.personalfinances.entity.User;
import com.example.personalfinances.entity.Wallet;
import com.example.personalfinances.entity.budgetCategory.BudgetCategoryExpense;
import com.example.personalfinances.entity.budgetCategory.BudgetCategoryIncome;
import com.example.personalfinances.entity.enums.TransactionType;
import com.example.personalfinances.repository.CategoryRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
  @Mock private CategoryRepository categoryRepository;

  @InjectMocks private CategoryService categoryService;

  private Wallet testWallet;
  private User testUser;
  private UUID walletId;

  @BeforeEach
  void setUp() {
    this.categoryRepository = mock(CategoryRepository.class);
    this.categoryService = new CategoryService(categoryRepository);
    testUser = new User("testUser", "hashedPassword");
    testWallet = new Wallet(testUser);
    walletId = testWallet.getWalletId();
  }

  @Test
  void findOrCreateExpenseCategory_whenCategoryExists_shouldReturnExistingCategory() {
    // Given
    String categoryName = "Еда";
    BigDecimal limitAmount = new BigDecimal("1000.00");
    BigDecimal currentAmount = new BigDecimal("500.00");

    Category existingCategory =
        new Category(testWallet, categoryName, TransactionType.EXPENSE, limitAmount);

    when(categoryRepository.existsByWalletWalletIdAndCategoryNameAndCategoryType(
            walletId, categoryName, TransactionType.EXPENSE))
        .thenReturn(true);
    when(categoryRepository.findByWalletWalletIdAndCategoryNameAndCategoryType(
            walletId, categoryName, TransactionType.EXPENSE))
        .thenReturn(Optional.of(existingCategory));
    when(categoryRepository.save(any(Category.class))).thenReturn(existingCategory);

    // When
    Category result =
        categoryService.findOrCreateExpenseCategory(
            testWallet, categoryName, limitAmount, currentAmount);

    // Then
    assertThat(result).isEqualTo(existingCategory);
    verify(categoryRepository, times(1)).save(existingCategory);
  }

  @Test
  void findOrCreateIncomeCategory_whenCategoryExists_shouldReturnExistingCategory() {
    // Given
    String categoryName = "Зарплата";

    Category existingCategory = new Category(testWallet, categoryName, TransactionType.INCOME);

    when(categoryRepository.existsByWalletWalletIdAndCategoryNameAndCategoryType(
            walletId, categoryName, TransactionType.INCOME))
        .thenReturn(true);
    when(categoryRepository.findByWalletWalletIdAndCategoryNameAndCategoryType(
            walletId, categoryName, TransactionType.INCOME))
        .thenReturn(Optional.of(existingCategory));
    when(categoryRepository.save(any(Category.class))).thenReturn(existingCategory);

    // When
    Category result = categoryService.findOrCreateIncomeCategory(testWallet, categoryName);

    // Then
    assertThat(result).isEqualTo(existingCategory);
    verify(categoryRepository, times(1)).save(existingCategory);
  }

  @Test
  void addIncomeCategory_withValidIncomeCategory_shouldAddIncome() {
    // Given
    String categoryName = "Зарплата";
    BigDecimal amount = new BigDecimal("50000.00");

    Category incomeCategory = new Category(testWallet, categoryName, TransactionType.INCOME);
    BudgetCategoryIncome initialBudget = (BudgetCategoryIncome) incomeCategory.getBudget();
    BigDecimal initialIncome = initialBudget.getIncome();

    when(categoryRepository.save(any(Category.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    // When
    categoryService.addIncomeCategory(incomeCategory, categoryName, amount);

    // Then
    BudgetCategoryIncome budget = (BudgetCategoryIncome) incomeCategory.getBudget();
    BigDecimal expectedIncome = initialIncome.add(amount);
    assertThat(budget.getIncome()).isEqualByComparingTo(expectedIncome);

    verify(categoryRepository, times(1)).save(incomeCategory);
  }

  @Test
  void getCategoriesByWalletAndType_shouldReturnCategories() {
    // Given
    TransactionType type = TransactionType.EXPENSE;
    List<Category> expectedCategories =
        List.of(
            new Category(testWallet, "Еда", TransactionType.EXPENSE),
            new Category(testWallet, "Транспорт", TransactionType.EXPENSE));

    when(categoryRepository.findAllByWalletWalletIdAndCategoryType(walletId, type))
        .thenReturn(expectedCategories);

    // When
    List<Category> result = categoryService.getCategoriesByWalletAndType(walletId, type);

    // Then
    assertThat(result).hasSize(2);
    assertThat(result).extracting(Category::getCategoryName).containsExactly("Еда", "Транспорт");
  }

  @Test
  void updateLimitAmount_withValidLimit_shouldUpdateLimit() {
    // Given
    String categoryName = "Еда";
    BigDecimal currentExpense = new BigDecimal("300.00");
    BigDecimal newLimit = new BigDecimal("500.00");

    Category expenseCategory = new Category(testWallet, categoryName, TransactionType.EXPENSE);
    BudgetCategoryExpense budget = (BudgetCategoryExpense) expenseCategory.getBudget();
    budget.addExpense(currentExpense);

    when(categoryRepository.findByWalletWalletIdAndCategoryNameAndCategoryType(
            walletId, categoryName, TransactionType.EXPENSE))
        .thenReturn(Optional.of(expenseCategory));
    when(categoryRepository.save(any(Category.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    // When
    categoryService.updateLimitAmount(walletId, categoryName, newLimit);

    // Then
    assertThat(budget.getLimitAmount()).isEqualByComparingTo(newLimit);
    verify(categoryRepository, times(1)).save(expenseCategory);
  }

  @Test
  void deleteCategory_whenCategoryExists_shouldDeleteAndReturnMessage() {
    // Given
    String categoryName = "Еда";
    TransactionType type = TransactionType.EXPENSE;

    when(categoryRepository.existsByWalletWalletIdAndCategoryNameAndCategoryType(
            walletId, categoryName, type))
        .thenReturn(true);

    // When
    String result = categoryService.deleteCategory(walletId, categoryName, type);

    // Then
    assertThat(result).isEqualTo("Категорию удалена!");
    verify(categoryRepository, times(1))
        .deleteByWalletWalletIdAndCategoryNameAndCategoryType(walletId, categoryName, type);
  }

  @Test
  void findOrCreateCategoryInternal_whenCategoryExists_shouldReturnIt() {
    // Given
    String categoryName = "Тест";
    TransactionType type = TransactionType.EXPENSE;

    Category existingCategory = new Category(testWallet, categoryName, type);

    when(categoryRepository.existsByWalletWalletIdAndCategoryNameAndCategoryType(
            walletId, categoryName, type))
        .thenReturn(true);
    when(categoryRepository.findByWalletWalletIdAndCategoryNameAndCategoryType(
            walletId, categoryName, type))
        .thenReturn(Optional.of(existingCategory));
    when(categoryRepository.save(any(Category.class))).thenReturn(existingCategory);

    // When: тестируем через findOrCreateExpenseCategory
    Category result =
        categoryService.findOrCreateExpenseCategory(
            testWallet, categoryName, new BigDecimal("1000.00"), new BigDecimal("500.00"));

    // Then
    assertThat(result).isEqualTo(existingCategory);
  }
}
