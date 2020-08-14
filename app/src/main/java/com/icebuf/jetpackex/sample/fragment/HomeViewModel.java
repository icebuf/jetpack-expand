package com.icebuf.jetpackex.sample.fragment;

import android.app.Application;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableList;
import androidx.lifecycle.AndroidViewModel;
import androidx.navigation.Navigation;

import com.icebuf.jetpackex.sample.pojo.TestItem;
import com.icebuf.jetpackex.sample.repo.TestListRepository;

/**
 * @author IceTang
 * @version 1.0
 * Data: 2020/8/14
 * E-mail：bflyff@hotmail.com
 */
public class HomeViewModel extends AndroidViewModel {

    private TestListRepository mRepository = TestListRepository.getInstance();

    public HomeViewModel(@NonNull Application application) {
        super(application);
    }

    public ObservableList<TestItem> getTestList() {
        return mRepository.getTestList(getApplication());
    }

    public void onItemClick(View view, int position) {
        int destination = getTestList().get(position).getDestination();
        Navigation.findNavController(view).navigate(destination);
//        Toast.makeText(view.getContext(), "onItemClick():: " + position, Toast.LENGTH_LONG).show();
    }

    public void onCheckChanged(View view, boolean checked) {
        Toast.makeText(view.getContext(), "onCheckChanged():: " + checked, Toast.LENGTH_LONG).show();
    }

}
