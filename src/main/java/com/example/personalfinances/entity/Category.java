package com.example.personalfinances.entity;

import com.example.personalfinances.entity.budgetCategory.AbstractBudgetCategory;
import com.example.personalfinances.entity.budgetCategory.BudgetCategoryExpose;
import com.example.personalfinances.entity.budgetCategory.BudgetCategoryIncome;
import com.example.personalfinances.entity.enums.TransactionType;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

@Entity()
@Table(
    name = "categories",
    uniqueConstraints = {
      @UniqueConstraint(
          name = "uk_wallet_category_name_type",
          columnNames = {"wallet_id", "name", "type"})
    })
public class Category {

  @Getter
  @Id
  @UuidGenerator(style = UuidGenerator.Style.RANDOM)
  @JdbcTypeCode(SqlTypes.UUID)
  @Column(name = "category_id", nullable = false, updatable = false)
  private UUID categoryId;

  @Setter
  @Getter
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(
      name = "wallet_id",
      nullable = false,
      foreignKey = @ForeignKey(name = "fk_category_wallet"))
  private Wallet wallet;

  @Getter
  @Column(name = "category_name", nullable = false, length = 100)
  private String categoryName;

  @Getter
  @Enumerated(EnumType.STRING)
  @Column(name = "category_type", nullable = false, length = 10)
  private TransactionType categoryType;

  @Getter
  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Transaction> transactions = new ArrayList<>();

  @Setter
  @Getter
  @OneToOne(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
  private AbstractBudgetCategory budget;

  protected Category() {}

  public Category(Wallet wallet, String categoryName, TransactionType type) {
    this.wallet = wallet;
    this.categoryName = categoryName;
    this.categoryType = type;
    this.createdAt = LocalDateTime.now();

    this.budget =
        type == TransactionType.EXPENSE
            ? new BudgetCategoryExpose(this)
            : new BudgetCategoryIncome(this);
  }

  public void addTransaction(Transaction transaction) {
    transactions.add(transaction);
    transaction.setCategory(this);
  }
}
