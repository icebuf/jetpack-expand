package com.icebuf.jetpackex.sample.fragment;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableList;
import androidx.lifecycle.AndroidViewModel;
import androidx.navigation.Navigation;

import com.icebuf.jetpackex.sample.pojo.TestItem;
import com.icebuf.jetpackex.sample.repo.TestCaseRepository;

/**
 * @author IceTang
 * @version 1.0
 * Data: 2020/8/14
 * E-mail：bflyff@hotmail.com
 */
public class HomeViewModel extends AndroidViewModel {

    private TestCaseRepository mRepository = TestCaseRepository.getInstance();

    public HomeViewModel(@NonNull Application application) {
        super(application);
    }

    public ObservableList<TestItem> getTestList() {
        return mRepository.getTestList(getApplication(), "testcase");
    }

    public void onItemClick(View view, int position) {
        int destination = getTestList().get(position).getDestination();
        Navigation.findNavController(view).navigate(destination);
    }

}
