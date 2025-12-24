package com.example.personalfinances.entity;

import com.example.personalfinances.entity.enums.CategoryType;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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

  @Id
  @UuidGenerator(style = UuidGenerator.Style.RANDOM)
  @JdbcTypeCode(SqlTypes.UUID)
  @Column(name = "category_id", nullable = false, updatable = false)
  private UUID categoryId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(
      name = "wallet_id",
      nullable = false,
      foreignKey = @ForeignKey(name = "fk_category_wallet"))
  private Wallet wallet;

  @Column(name = "category_name", nullable = false, length = 100)
  private String categoryName;

  @Enumerated(EnumType.STRING)
  @Column(name = "category_type", nullable = false, length = 10)
  private CategoryType categoryType;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Transaction> transactions = new ArrayList<>();

  @OneToOne(
      mappedBy = "category",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.LAZY)
  private Budget budget;

  protected Category() {}

  public Category(Wallet wallet, String categoryName, CategoryType type) {
    this.wallet = wallet;
    this.categoryName = categoryName;
    this.categoryType = type;
    this.createdAt = LocalDateTime.now();
  }

  public void addTransaction(Transaction transaction) {
    transactions.add(transaction);
    transaction.setCategory(this);
  }

  public void setBudget(Budget budget) {
    this.budget = budget;
    budget.setCategory(this);
  }

  public UUID getCategoryId() {
    return categoryId;
  }

  public Wallet getWallet() {
    return wallet;
  }

  public void setWallet(Wallet wallet) {
    this.wallet = wallet;
  }

  public String getCategoryName() {
    return categoryName;
  }

  public CategoryType getCategoryType() {
    return categoryType;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public Budget getBudget() {
    return budget;
  }
}
