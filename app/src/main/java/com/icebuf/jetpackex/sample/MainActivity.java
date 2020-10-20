package com.icebuf.jetpackex.sample;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.icebuf.jetpackex.databinding.DBActivity;
import com.icebuf.jetpackex.sample.util.ActionBarUtil;
import com.icebuf.jetpackex.sample.util.StatusBarUtil;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends DBActivity<MainViewModel> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StatusBarUtil.setTranslucentStatus(this);
        StatusBarUtil.setRootViewFitsSystemWindows(this, true);
        ActionBarUtil.setTitleInCenter(MainActivity.this, true);
//        ActionBarUtil.with(this).setBackgroundDrawable(getDrawable(R.drawable.bg_actionbar));
        NavController controller = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, controller);
    }

    @NonNull
    @Override
    protected Class<MainViewModel> getVMClass() {
        return MainViewModel.class;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected int getVariableId() {
        return BR.vm;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            return Navigation.findNavController(this, R.id.nav_host_fragment).popBackStack()
                    || super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }
}