package com.example.calculatorprojectv2;

import android.content.Intent;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PowerUpViewModel extends ViewModel {
    private final MutableLiveData<Integer> points = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> pointsAdded = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> numFreeze = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> numDouble = new MutableLiveData<>(0);
    private final MutableLiveData<Boolean> isDoublePointsEnabled = new MutableLiveData<>(false);
    private final MutableLiveData<Integer> numClick = new MutableLiveData<>(0);
    private final MutableLiveData<String> prevScreen = new MutableLiveData<>("");

    public Integer getPoints() { return points.getValue(); }
    public Integer getPointsAdded() { return pointsAdded.getValue(); }
    public Integer getNumFreeze() { return numFreeze.getValue(); }
    public Integer getNumDouble() { return numDouble.getValue(); }
    public Boolean isDoublePointsEnabled() { return isDoublePointsEnabled.getValue(); }
    public Integer getNumClick() { return numClick.getValue(); }
    public String getPrevScreen() { return prevScreen.getValue(); }

    public void setPoints(Integer val) { points.setValue(val); }
    public void setPointsAdded(Integer val) { pointsAdded.setValue(val); }
    public void setNumFreeze(Integer val) { numFreeze.setValue(val); }
    public void setNumDouble(Integer val) { numDouble.setValue(val); }
    public void setIsDoublePointsEnabled(Boolean val) { isDoublePointsEnabled.setValue(val); }
    public void setNumClick(Integer val) { numClick.setValue(val); }
    public void setPrevScreen(String val) { prevScreen.setValue(val); }
}
