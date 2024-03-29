package com.itjuana.itjuanademo.ui;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.itjuana.itjuanademo.R;
import com.itjuana.itjuanademo.viewmodel.MainViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SettingsFragment extends Fragment {

    @BindView(R.id.btSave)
    Button btSave;

    @BindView(R.id.etName)
    EditText etName;

    @BindView(R.id.etDescription)
    EditText etDescription;

    private MainViewModel mainViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        refreshView(container);


        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        ButterKnife.bind(this, view);

        btSave.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String description = etDescription.getText().toString().trim();
            if (name.length() > 1 && description.length() > 1) {
                mainViewModel.addUser(name, description);
                Toast.makeText(requireContext(), "data sent", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "please fill all fiealds with at least 2 chars", Toast.LENGTH_SHORT).show();
            }
        });

        Disposable disposable = mainViewModel.getSelfUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(self -> {
                    if (self != null) {
                        if (!self.getName().isEmpty() && !self.getName().equals(etName.getText().toString())) {
                            etName.setText(self.getName());
                        }
                        if (!self.getDescription().isEmpty() && !self.getDescription().equals(etDescription.getText().toString())) {
                            etDescription.setText(self.getDescription());
                        }
                    }
                }, Throwable::printStackTrace);
        return view;
    }

    private void refreshView(ViewGroup mScrollView) {
        mScrollView.setVisibility(View.GONE);
        mScrollView.setVisibility(View.VISIBLE);
    }

}
