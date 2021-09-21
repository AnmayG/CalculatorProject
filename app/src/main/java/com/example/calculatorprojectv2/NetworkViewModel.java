package com.example.calculatorprojectv2;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NetworkViewModel extends ViewModel {
    private final MutableLiveData<Integer> currentLevel = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> points = new MutableLiveData<>(0);
    private final MutableLiveData<String> leader = new MutableLiveData<>("");

    public MutableLiveData<Integer> getCurrentLevel() {
        return currentLevel;
    }

    public MutableLiveData<Integer> getPoints() {
        return points;
    }

    public MutableLiveData<String> getLeader() {
        return leader;
    }

    public void setCurrentLevel(int level) {
        currentLevel.setValue(level);
    }

    public void setPoints(int point) {
        points.setValue(point);
    }

    public void setLeader(String lead) {
        leader.setValue(lead);
    }

    public void reset() {
        this.setCurrentLevel(0);
        this.setPoints(0);
        this.setLeader("");
    }
}
