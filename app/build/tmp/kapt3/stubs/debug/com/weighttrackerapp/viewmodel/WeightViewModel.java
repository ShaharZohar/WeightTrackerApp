package com.weighttrackerapp.viewmodel;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0004\n\u0002\u0010\b\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0012J\u0006\u0010\u0014\u001a\u00020\u0010J\u000e\u0010\u0015\u001a\u00020\u00102\u0006\u0010\u0016\u001a\u00020\u0017R\u001d\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0018"}, d2 = {"Lcom/weighttrackerapp/viewmodel/WeightViewModel;", "Landroidx/lifecycle/ViewModel;", "application", "Landroid/app/Application;", "(Landroid/app/Application;)V", "allWeightEntries", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/weighttrackerapp/data/WeightEntry;", "getAllWeightEntries", "()Lkotlinx/coroutines/flow/Flow;", "db", "Lcom/weighttrackerapp/database/AppDatabase;", "weightDao", "Lcom/weighttrackerapp/database/WeightDao;", "addWeightEntry", "", "weight", "", "heightCm", "clearAll", "deleteEntry", "id", "", "app_debug"})
public final class WeightViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.weighttrackerapp.database.AppDatabase db = null;
    @org.jetbrains.annotations.NotNull()
    private final com.weighttrackerapp.database.WeightDao weightDao = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<java.util.List<com.weighttrackerapp.data.WeightEntry>> allWeightEntries = null;
    
    public WeightViewModel(@org.jetbrains.annotations.NotNull()
    android.app.Application application) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.weighttrackerapp.data.WeightEntry>> getAllWeightEntries() {
        return null;
    }
    
    public final void addWeightEntry(float weight, float heightCm) {
    }
    
    public final void clearAll() {
    }
    
    public final void deleteEntry(int id) {
    }
}