package rs.etf.pmu;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {

    private MutableLiveData<Integer> iteration;

    public MainViewModel() {
        iteration = new MutableLiveData<>();
    }

    public LiveData<Integer> getIteration() {
        return iteration;
    }

    public void setIteration(Integer iteration) {
        this.iteration.postValue(iteration);
    }

}
