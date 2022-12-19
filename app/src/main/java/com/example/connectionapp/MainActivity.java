package com.example.connectionapp;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.SeekBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import com.example.connectionapp.databinding.MainBinder;
import io.reactivex.rxjava3.functions.Consumer;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private MainBinder binding;
    private MainViewModel viewModel;

    private String[] names ={"A","B","C","D","E","F","G"};
    private int[] icons = {R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_foreground};

    private Boolean isSpinnerTouch = false, isCustomSpinnerTouch = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main); //setContentView(R.layout.activity_main);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class); //ViewModelProviders.of(this).get(MainViewModel.class);

        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        setSpinnerAdapter();
        setLiveDataObservers();
        setEventListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.getViewModel().setData("Test");
    }

    private void setSpinnerAdapter() {
        ArrayAdapter spinnerAdapter = new ArrayAdapter(getBaseContext(),android.R.layout.simple_spinner_item, names);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerSendData.setAdapter(spinnerAdapter); //spinnerSendData.setAdapter(spinnerAdapter);

        CustomAdapter customAdapter=new CustomAdapter(getApplicationContext(),icons, names);
        binding.spinnerCustomSendData.setAdapter(customAdapter);
    }

    private void setLiveDataObservers() {
        viewModel.addDisposable(
            viewModel.observeData().subscribe(new Consumer<String>() {
                @Override
                public void accept(String string) throws Throwable {
                    Log.d(TAG, "accept(Data " + string + ")");
                    binding.textResult.setText(string);
                }
            })
        );

        viewModel.addDisposable(
            viewModel.observeProgressData().subscribe(new Consumer<Integer>() {
                @Override
                public void accept(Integer integer) throws Throwable {
                    Log.d(TAG, "accept(Progress Data " + integer + ")");
                    binding.progressBarResult.setProgress(integer);
                }
            })
        );
    }

    private void setEventListeners() {
        binding.buttonSendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.getViewModel().setData("Button Was Clicked");
            }
        });

        binding.switchSendData.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    binding.getViewModel().setData("Switch is On");
                }
                else if (isChecked == false) {
                    binding.getViewModel().setData("Switch is Off");
                }
            }
        });

        binding.toggleButtonSendData.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    binding.getViewModel().setData("Toggle Button is On");
                }
                else if (isChecked == false) {
                    binding.getViewModel().setData("Toggle Button is Off");
                }
            }
        });

        binding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (findViewById(checkedId).getId()){
                    case R.id.radioOn:
                        binding.getViewModel().setData("Radio Button is On of your selected Radio Group");
                        break;
                    case R.id.radioOff:
                        binding.getViewModel().setData("Radio Button is Off of your selected Radio Group");
                        break;
                }
            }
        });

        binding.checkboxSendData.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    binding.getViewModel().setData("Check Box is On");
                    binding.checkboxSendData.setText("On");
                }
                else if (isChecked == false) {
                    binding.getViewModel().setData("Check Box is Off");
                    binding.checkboxSendData.setText("Off");
                }
            }
        });

        binding.spinnerSendData.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                isSpinnerTouch = true;
                return false;
            }
        });

        binding.spinnerSendData.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isSpinnerTouch) {
                    binding.getViewModel().setData("Spinner value selected is " + names[position]);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                binding.getViewModel().setData("Spinner Nothing selected");
            }
        });

        binding.spinnerCustomSendData.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                isCustomSpinnerTouch = true;
                return false;
            }
        });

        binding.spinnerCustomSendData.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isCustomSpinnerTouch) {
                    binding.getViewModel().setData("Customize Spinner value selected is " + names[position]);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                binding.getViewModel().setData("Customize Spinner Nothing selected");
            }
        });

        binding.ratingBarSendData.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                binding.getViewModel().setData("Rating Bar value is " + rating);
            }
        });

        binding.seekBarSendData.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                binding.getViewModel().setProgressData(progress);
                binding.getViewModel().setData("Seek Bar Value selected is " + progress);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        binding.seekBarSendData.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean isFocus) {
                if (isFocus){
                    //Toast.makeText(getBaseContext(),"On Enter Focus",Toast.LENGTH_SHORT).show();
                    binding.seekBarSendData.setThumb(getResources().getDrawable(R.drawable.ic_seeker_thumb_selected));
                }
                else {
                    //Toast.makeText(getBaseContext(),"On Leave Focus",Toast.LENGTH_SHORT).show();
                    binding.seekBarSendData.setThumb(getResources().getDrawable(R.drawable.ic_seeker_thumb_unselected));
                }
            }
        });

        binding.seekBarDiscreteSendData.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                binding.getViewModel().setProgressData(progress);
                binding.getViewModel().setData("Customize Seek Bar Value selected is " + progress);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        binding.seekBarDiscreteSendData.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean isFocus) {
                if (isFocus){
                    //Toast.makeText(getBaseContext(),"On Enter Focus",Toast.LENGTH_SHORT).show();
                    binding.seekBarDiscreteSendData.setThumb(getResources().getDrawable(R.drawable.ic_lever_selected));
                }
                else {
                    //Toast.makeText(getBaseContext(),"On Leave Focus",Toast.LENGTH_SHORT).show();
                    binding.seekBarDiscreteSendData.setThumb(getResources().getDrawable(R.drawable.ic_lever_unselected));
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding.buttonSendData.setOnClickListener(null);
        binding.switchSendData.setOnCheckedChangeListener(null);
        binding.toggleButtonSendData.setOnCheckedChangeListener(null);
        binding.radioGroup.setOnCheckedChangeListener(null);
        binding.checkboxSendData.setOnCheckedChangeListener(null);
        binding.spinnerSendData.setOnItemSelectedListener(null);
        binding.spinnerCustomSendData.setOnItemSelectedListener(null);
        binding.ratingBarSendData.setOnRatingBarChangeListener(null);
    }
}