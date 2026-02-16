package com.jeture.budget.di

import android.content.Context
import androidx.room.Room
import com.jeture.budget.data.dao.CategoryDao
import com.jeture.budget.data.dao.TransactionDao
import com.jeture.budget.data.db.AppDatabase
import com.jeture.budget.data.repository.BudgetRepositoryImpl
import com.jeture.budget.domain.repo.BudgetRepository
import com.jeture.budget.domain.usecase.AddTransactionUseCase
import com.jeture.budget.domain.usecase.ObserveMonthlyTotalsUseCase
import com.jeture.budget.domain.usecase.ObserveTopExpenseCategoriesUseCase
import com.jeture.budget.domain.usecase.SeedCategoriesIfEmptyUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides @Singleton
    fun provideDb(@ApplicationContext ctx: Context): AppDatabase =
        Room.databaseBuilder(ctx, AppDatabase::class.java, "budget.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides fun provideCategoryDao(db: AppDatabase): CategoryDao = db.categoryDao()
    @Provides fun provideTransactionDao(db: AppDatabase): TransactionDao = db.transactionDao()

    @Provides @Singleton
    fun provideRepo(categoryDao: CategoryDao, transactionDao: TransactionDao): BudgetRepository =
        BudgetRepositoryImpl(categoryDao, transactionDao)

    @Provides fun provideAddTx(repo: BudgetRepository) = AddTransactionUseCase(repo)
    @Provides fun provideTotals(repo: BudgetRepository) = ObserveMonthlyTotalsUseCase(repo)
    @Provides fun provideTopCats(repo: BudgetRepository) = ObserveTopExpenseCategoriesUseCase(repo)
    @Provides fun provideSeed(repo: BudgetRepository) = SeedCategoriesIfEmptyUseCase(repo)
}
